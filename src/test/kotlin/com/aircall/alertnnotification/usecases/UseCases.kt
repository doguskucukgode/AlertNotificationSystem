package com.aircall.alertnnotification.usecases

import com.aircall.alertnnotification.usecases.base.UseCaseBase
import com.aircall.alertnotification.AlertNotificationApplication
import com.aircall.alertnotification.adapter.AlertAdapter
import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.config.PropertiesConfig
import com.aircall.alertnotification.persistence.impl.AlertRepositoryImpl
import com.aircall.alertnotification.persistence.impl.MonitoredServiceRepositoryImpl
import com.aircall.alertnotification.service.impl.AlertServiceImpl
import com.aircall.alertnotification.service.impl.EscalationPolicyServiceImpl
import com.aircall.alertnotification.service.impl.TimerServiceImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.TimeUnit

@ExtendWith(MockitoExtension::class)
@SpringBootTest(classes = [AlertNotificationApplication::class])
class UseCases: UseCaseBase() {

    @Mock
    protected lateinit var alertAdapter: AlertAdapter

    @Mock
    protected lateinit var mailAdapter: TargetAdapter

    @Mock
    protected lateinit var smsAdapter: TargetAdapter

    @Mock
    protected lateinit var propertiesConfig: PropertiesConfig

    @Autowired
    protected lateinit var alertService: AlertServiceImpl

    @Autowired
    protected lateinit var monitoredServiceRepository: MonitoredServiceRepositoryImpl

    @Autowired
    protected lateinit var alertRepository: AlertRepositoryImpl

    @Autowired
    protected lateinit var escalationPolicyService: EscalationPolicyServiceImpl

    @Autowired
    protected lateinit var timerService: TimerServiceImpl


    @BeforeEach
    fun setUp() {
        alertService.alertAdapter = alertAdapter;
        escalationPolicyService.mailAdapter = mailAdapter
        escalationPolicyService.smsAdapter = smsAdapter
        timerService.propertiesConfig = propertiesConfig
        monitoredServiceRepository.serviceList = createMonitoredServiceList()
        alertRepository.alertList.clear()
        timerService.timerList.clear()
    }

    /**
     * Given a Monitored Service in a Healthy State,
     * when the Pager receives an Alert related to this Monitored Service,
     * then the Monitored Service becomes Unhealthy,
     * the Pager notifies all targets of the first level of the escalation policy,
     * and sets a 15-minutes acknowledgement delay
     */
    @Test
    fun givenMonitoredServiceHealthyWhenPagerReceivesAlertThenServiceBecomesUnhealthy() {
        // Arrange
        Mockito.`when`(alertAdapter.receiveAlert()).thenReturn(createAlert1())
        Mockito.`when`(propertiesConfig.targetLevelInterval).thenReturn(15 * 60) // set interval 15 minutes delay

        // Act
        alertService.receiveAlert()

        // Assert
        val monitoredServiceInRepository = monitoredServiceRepository.findMonitoredService(SERVICE_NAME1)
        val alertInRepository = alertRepository.findAlert(SERVICE_NAME1)
        // Assert targets are notified
        Mockito.verify(mailAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET1)
        Mockito.verify(smsAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET1)
        // Assert service is unhealthy and timer is started
        assertAll("givenMonitoredServiceHealthyWhenPagerReceivesAlertThenServiceBecomesUnhealthy",
                { Assertions.assertThat(monitoredServiceInRepository).isNotNull()},
                { Assertions.assertThat(monitoredServiceInRepository?.healthy).isFalse()},
                { Assertions.assertThat(alertInRepository).isNotNull()},
                { Assertions.assertThat(alertInRepository?.updateDateTime).isNotNull()},
                { Assertions.assertThat(timerService.timerList.size).isEqualTo(1)},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]).isNotNull()},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]?.isFinished()).isFalse()},
        )
        cancelTimers()
    }

    /**
     * Given a Monitored Service in an Unhealthy State,
     * the corresponding Alert is not Acknowledged
     * and the last level has not been notified,
     * when the Pager receives the Acknowledgement Timeout,
     * then the Pager notifies all targets of the next level of the escalation policy
     * and sets a 15-minutes acknowledgement delay.
     */
    @Test
    fun givenMonitoredServiceUnHealthyWhenPagerReceivesAckTimeOutThenServiceNotifiesNextLevel() {
        // Arrange
        Mockito.`when`(alertAdapter.receiveAlert()).thenReturn(createAlert1())
        Mockito.`when`(propertiesConfig.targetLevelInterval).thenReturn(10) // set interval 10 second delay
        alertService.receiveAlert()

        // Act (Wait 5 seconds and set interval 15 minutes not to notify 3rd level)
        Thread.sleep(5 * 1000)
        Mockito.`when`(propertiesConfig.targetLevelInterval).thenReturn(15 * 60) // set interval 15 minutes delay
        Thread.sleep(10 * 1000)

        // Assert
        val monitoredServiceInRepository = monitoredServiceRepository.findMonitoredService(SERVICE_NAME1)
        // Assert first level targets are notified
        Mockito.verify(mailAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET1)
        Mockito.verify(smsAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET1)
        // Assert second level targets are notified
        Mockito.verify(mailAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET2)
        Mockito.verify(smsAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET2)
        // Assert service is unhealthy and second timer is started
        assertAll("givenMonitoredServiceUnHealthyWhenPagerReceivesAckTimeOutAlertThenServiceNotifiesNextLevel",
                { Assertions.assertThat(monitoredServiceInRepository).isNotNull()},
                { Assertions.assertThat(monitoredServiceInRepository?.healthy).isFalse()},
                { Assertions.assertThat(timerService.timerList.size).isEqualTo(1)},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]).isNotNull()},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]?.isFinished()).isFalse()}
        )
        cancelTimers()
    }

    /**
     * Given a Monitored Service in an Unhealthy State
     * when the Pager receives the Acknowledgement
     * and later receives the Acknowledgement Timeout,
     * then the Pager doesn't notify any Target
     * and doesn't set an acknowledgement delay.
     */
    @Test
    fun givenMonitoredServiceUnHealthyWhenPagerReceivesAckAlertThenServiceNotNotifiesNextLevel() {
        // Arrange
        Mockito.`when`(alertAdapter.receiveAlert()).thenReturn(createAlert1())
        Mockito.`when`(propertiesConfig.targetLevelInterval).thenReturn(15 * 60) // set interval 15 minutes delay
        alertService.receiveAlert() //making unhealthy state with level1 targets alerted
        Mockito.`when`(alertAdapter.receiveAlert()).thenReturn(createAlert1Ack()) // set ack alert

        // Act
        alertService.receiveAlert()

        // Assert
        val monitoredServiceInRepository = monitoredServiceRepository.findMonitoredService(SERVICE_NAME1)
        // Assert first level targets are notified only once when becoming initial state to be unhealthy
        Mockito.verify(mailAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET1)
        Mockito.verify(smsAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET1)
        // Assert second level targets are not notified after ack message
        Mockito.verify(mailAdapter, Mockito.times(0)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET2)
        Mockito.verify(smsAdapter, Mockito.times(0)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET2)
        // Assert service is unhealthy and timer list is empty
        assertAll("givenMonitoredServiceUnHealthyWhenPagerReceivesAckAlertThenServiceNotNotifiesNextLevel",
                { Assertions.assertThat(monitoredServiceInRepository).isNotNull()},
                { Assertions.assertThat(monitoredServiceInRepository?.healthy).isFalse()},
                { Assertions.assertThat(timerService.timerList.size).isEqualTo(0)},
        )
        cancelTimers()
    }

    /**
     * Given a Monitored Service in an Unhealthy State,
     * when the Pager receives an Alert related to this Monitored Service,
     * then the Pager doesn’t notify any Target
     * and doesn’t set an acknowledgement delay
     */
    @Test
    fun givenMonitoredServiceUnHealthyWhenPagersReceivesAlertThenServiceNotNotifiesNextLevel() {
        // Arrange
        Mockito.`when`(alertAdapter.receiveAlert()).thenReturn(createAlert1())
        Mockito.`when`(propertiesConfig.targetLevelInterval).thenReturn(15 * 60) // set interval 15 minutes delay
        alertService.receiveAlert() //making unhealthy state with level1 targets alerted

        // Act
        alertService.receiveAlert() // get same alert 4 times
        alertService.receiveAlert()
        alertService.receiveAlert()
        alertService.receiveAlert()

        // Assert
        val monitoredServiceInRepository = monitoredServiceRepository.findMonitoredService(SERVICE_NAME1)
        // Assert first level targets are notified only once when becoming initial state to be unhealthy
        Mockito.verify(mailAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET1)
        Mockito.verify(smsAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET1)
        // Assert service is unhealthy and timer is running alert is ignored
        assertAll("givenMonitoredServiceUnHealthyWhenPagerReceivesAckTimeOutAlertThenServiceNotifiesNextLevel",
                { Assertions.assertThat(monitoredServiceInRepository).isNotNull()},
                { Assertions.assertThat(monitoredServiceInRepository?.healthy).isFalse()},
                { Assertions.assertThat(timerService.timerList.size).isEqualTo(1)},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]).isNotNull()},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]?.isFinished()).isFalse()}
        )
        cancelTimers()
    }

    /**
     * Given a Monitored Service in an Unhealthy State,
     * when the Pager receives a Healthy event related to this Monitored Service
     * and later receives the Acknowledgement Timeout,
     * then the Monitored Service becomes Healthy,
     * the Pager doesn’t notify any Target
     * and doesn’t set an acknowledgement delay
     */
    @Test
    fun givenMonitoredServiceUnHealthyWhenPagersReceivesHealthyAndAckTimeoutThenServiceNotNotifiesNextLevel() {
        // Arrange
        Mockito.`when`(alertAdapter.receiveAlert()).thenReturn(createAlert1())
        Mockito.`when`(propertiesConfig.targetLevelInterval).thenReturn(10) // set interval 10 second delay
        alertService.receiveAlert()
        Mockito.`when`(alertAdapter.receiveAlert()).thenReturn(createAlert1Healthy())

        // Act (Send healthy alert and wait 15 seconds to get timeout ack)
        Thread.sleep(5 * 1000)
        alertService.receiveAlert()
        Thread.sleep(10 * 1000)

        // Assert
        val monitoredServiceInRepository = monitoredServiceRepository.findMonitoredService(SERVICE_NAME1)
        // Assert first level targets are notified only once when becoming initial state to be unhealthy
        Mockito.verify(mailAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET1)
        Mockito.verify(smsAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET1)
        // Assert second level targets are not notified after timeout
        Mockito.verify(mailAdapter, Mockito.times(0)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET2)
        Mockito.verify(smsAdapter, Mockito.times(0)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET2)
        // Assert service is healthy and timer is running alert is ignored
        assertAll("givenMonitoredServiceUnHealthyWhenPagerReceivesAckTimeOutAlertThenServiceNotifiesNextLevel",
                { Assertions.assertThat(monitoredServiceInRepository).isNotNull()},
                { Assertions.assertThat(monitoredServiceInRepository?.healthy).isTrue()},
                { Assertions.assertThat(monitoredServiceInRepository?.currentLevel).isEqualTo(0)},
                { Assertions.assertThat(timerService.timerList.size).isEqualTo(0)},
        )

    }

    private fun cancelTimers() {
        timerService.timerList.forEach { (_, u) -> u.cancel() }
    }
}