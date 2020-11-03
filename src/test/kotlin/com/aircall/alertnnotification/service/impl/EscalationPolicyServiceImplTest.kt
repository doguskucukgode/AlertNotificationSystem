package com.aircall.alertnnotification.service.impl

import com.aircall.alertnnotification.usecases.base.UseCaseBase
import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.persistence.MonitoredServiceRepository
import com.aircall.alertnotification.service.TimerService
import com.aircall.alertnotification.service.impl.EscalationPolicyServiceImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class EscalationPolicyServiceImplTest : UseCaseBase() {

    private lateinit var escalationPolicyService: EscalationPolicyServiceImpl

    @Mock
    private lateinit var monitoredServiceRepository: MonitoredServiceRepository

    @Mock
    private lateinit var mailAdapter: TargetAdapter

    @Mock
    private lateinit var smsAdapter: TargetAdapter

    @Mock
    private lateinit var timerService: TimerService


    @BeforeEach
    fun setUp() {
        escalationPolicyService = Mockito.spy(EscalationPolicyServiceImpl(
                monitoredServiceRepository = monitoredServiceRepository,
                timerService = timerService,
                mailAdapter = mailAdapter,
                smsAdapter = smsAdapter))
    }

    @Test
    fun givenMonitoredServiceUnHealthyWhenNotifyTargetsThenAssertTargetsAndTimer() {
        // Arrange
        val monitoredService = createMonitoredService1()
        monitoredService.healthy = false
        Mockito.`when`(monitoredServiceRepository.findMonitoredService(SERVICE_NAME1)).thenReturn(monitoredService)
        // Act
        escalationPolicyService.notifyTargets(SERVICE_NAME1, SERVICE_NAME1_MESSAGE)
        // Assert
        // Assert targets are notified
        Mockito.verify(mailAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, EMAIL_TARGET1)
        Mockito.verify(smsAdapter, Mockito.times(1)).sendAlert(SERVICE_NAME1_MESSAGE, SMS_TARGET1)
        Mockito.verify(monitoredServiceRepository, Mockito.times(1)).saveMonitoredService(monitoredService)
        Mockito.verify(timerService, Mockito.times(1))
                .scheduleTimer(SERVICE_NAME1, SERVICE_NAME1_MESSAGE, escalationPolicyService)
        // Assert monitored service current level incremented
        Assertions.assertThat(monitoredService.currentLevel).isEqualTo(1)

    }
}
