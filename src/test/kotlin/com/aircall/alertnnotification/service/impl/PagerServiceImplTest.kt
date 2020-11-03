package com.aircall.alertnnotification.service.impl

import com.aircall.alertnnotification.usecases.base.UseCaseBase
import com.aircall.alertnotification.persistence.AlertRepository
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.impl.PagerServiceImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PagerServiceImplTest: UseCaseBase() {

    private lateinit var pagerService: PagerServiceImpl

    @Mock
    private lateinit var escalationPolicyService: EscalationPolicyService
    @Mock
    private lateinit var  alertRepository: AlertRepository

    @BeforeEach
    fun setUp() {
        pagerService = Mockito.spy(PagerServiceImpl(alertRepository = alertRepository,
                escalationPolicyService = escalationPolicyService))
    }

    @Test
    fun givenAlertTypeIncidentAndRepoIsEmptyWhenReceiveAlertThenAssertAlert() {
        // Arrange
        val alert = createAlert2()
        Mockito.`when`(alertRepository.findAlert(SERVICE_NAME2)).thenReturn(null)
        // Act
        pagerService.receiveAlert(alert)
        // Assert
        Mockito.verify(escalationPolicyService, Mockito.times(1)).makeMonitoredServiceUnHealthy(SERVICE_NAME2)
        Mockito.verify(escalationPolicyService, Mockito.times(1)).notifyTargets(SERVICE_NAME2, SERVICE_NAME2_MESSAGE)
        Assertions.assertThat(alert.updateDateTime).isNotNull()
    }

    @Test
    fun givenAlertTypeIncidentAndRepoIsNotEmptyWhenReceiveAlertThenAssertAlert() {
        // Arrange
        val alert = createAlert2()
        Mockito.`when`(alertRepository.findAlert(SERVICE_NAME2)).thenReturn(alert)
        // Act
        pagerService.receiveAlert(alert)
        // Assert
        Mockito.verify(escalationPolicyService, Mockito.times(0)).makeMonitoredServiceUnHealthy(SERVICE_NAME2)
        Mockito.verify(escalationPolicyService, Mockito.times(0)).notifyTargets(SERVICE_NAME2, SERVICE_NAME2_MESSAGE)
        Assertions.assertThat(alert.updateDateTime).isNull()
    }

    @Test
    fun givenAlertTypeAcknowledgeAndRepoIsNotEmptyWhenReceiveAlertThenAssertAlert() {
        // Arrange
        val alert = createAlert2Ack()
        Mockito.`when`(alertRepository.findAlert(SERVICE_NAME2)).thenReturn(alert)
        // Act
        pagerService.receiveAlert(alert)
        // Assert
        Mockito.verify(escalationPolicyService, Mockito.times(1)).ackReceived(SERVICE_NAME2)
        Assertions.assertThat(alert.updateDateTime).isNotNull()
    }

    @Test
    fun givenAlertTypeAcknowledgeAndRepoIsEmptyWhenReceiveAlertThenAssertAlert() {
        // Arrange
        val alert = createAlert2Ack()
        Mockito.`when`(alertRepository.findAlert(SERVICE_NAME2)).thenReturn(null)
        // Act
        pagerService.receiveAlert(alert)
        // Assert
        Mockito.verify(escalationPolicyService, Mockito.times(0)).ackReceived(SERVICE_NAME2)
        Assertions.assertThat(alert.updateDateTime).isNull()
    }

    @Test
    fun givenAlertTypeHealthyAndRepoIsNotEmptyWhenReceiveAlertThenAssertAlert() {
        // Arrange
        val alert = createAlert2Healthy()
        Mockito.`when`(alertRepository.findAlert(SERVICE_NAME2)).thenReturn(alert)
        // Act
        pagerService.receiveAlert(alert)
        // Assert
        Mockito.verify(escalationPolicyService, Mockito.times(1)).healthyReceived(SERVICE_NAME2)
        Assertions.assertThat(alert.updateDateTime).isNotNull()
    }

    @Test
    fun givenAlertTypeHealthyAndRepoIsEmptyWhenReceiveAlertThenAssertAlert() {
        // Arrange
        val alert = createAlert2Healthy()
        Mockito.`when`(alertRepository.findAlert(SERVICE_NAME2)).thenReturn(null)
        // Act
        pagerService.receiveAlert(alert)
        // Assert
        Mockito.verify(escalationPolicyService, Mockito.times(0)).healthyReceived(SERVICE_NAME2)
        Assertions.assertThat(alert.updateDateTime).isNull()
    }

    @Test
    fun givenAlertWhenUpdateAlertThenAssertAlert() {
        // Arrange
        val alert = createAlert2Healthy()
        // Act
        pagerService.updateAlertTime(alert)
        // Assert
        Assertions.assertThat(alert.updateDateTime).isNotNull()
    }
}