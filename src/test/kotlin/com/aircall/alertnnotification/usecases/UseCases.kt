package com.aircall.alertnnotification.usecases

import com.aircall.alertnnotification.usecases.base.UseCaseBase
import com.aircall.alertnotification.AlertNotificationApplication
import com.aircall.alertnotification.adapter.AlertAdapter
import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.config.PropertiesConfig
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
        Mockito.`when`(propertiesConfig.targetLevelInterval).thenReturn(15 * 60) // set 15 minutes delay

        // Act
        alertService.receiveAlert()

        // Assert
        val monitoredServiceInRepository = monitoredServiceRepository.findMonitoredService(SERVICE_NAME1)
        Mockito.verify(mailAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET1)
        Mockito.verify(smsAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET1)
        assertAll("givenMonitoredServiceHealthyWhenPagersReceivesAlertThenServiceBecomesUnhealthy",
                { Assertions.assertThat(monitoredServiceInRepository).isNotNull},
                { Assertions.assertThat(monitoredServiceInRepository?.healthy).isFalse()},
                { Assertions.assertThat(timerService.timerList.size).isEqualTo(1)},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]).isNotNull},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]?.isFinished()).isFalse()},
        )
        cancelTimers()
    }

    private fun cancelTimers() {
        timerService.timerList.forEach { (_, u) -> u.cancel() }
    }
}