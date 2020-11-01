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
        monitoredServiceRepository.serviceList.addAll(createMonitoredServiceList())
    }

    /**
     * Given a Monitored Service in a Healthy State,
     * when the Pager receives an Alert related to this Monitored Service,
     * then the Monitored Service becomes Unhealthy,
     * the Pager notifies all targets of the first level of the escalation policy,
     * and sets a 15-minutes acknowledgement delay
     */
    @Test
    fun givenMonitoredServiceHealthyWhenPagersReceivesAlertThenServiceBecomesUnhealthy() {
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
        assertAll("givenMonitoredServiceHealthyWhenPagersReceivesAlertThenServiceBecomesUnhealthy",
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
    fun givenMonitoredServiceUnHealthyWhenPagersReceivesAckAlertThenServiceNotifiesNextLevel() {
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
        assertAll("givenMonitoredServiceUnHealthyWhenPagersReceivesAckAlertThenServiceNotifiesNextLevel",
                { Assertions.assertThat(monitoredServiceInRepository).isNotNull()},
                { Assertions.assertThat(monitoredServiceInRepository?.healthy).isFalse()},
                { Assertions.assertThat(timerService.timerList.size).isEqualTo(1)},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]).isNotNull()},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]?.isFinished()).isFalse()}
        )
        cancelTimers()
    }


    private fun cancelTimers() {
        timerService.timerList.forEach { (_, u) -> u.cancel() }
    }
}