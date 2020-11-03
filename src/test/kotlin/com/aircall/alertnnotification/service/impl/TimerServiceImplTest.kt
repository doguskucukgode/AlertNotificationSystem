package com.aircall.alertnnotification.service.impl

import com.aircall.alertnnotification.usecases.base.UseCaseBase
import com.aircall.alertnotification.config.PropertiesConfig
import com.aircall.alertnotification.model.alert.TimerEvent
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.impl.TimerServiceImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class TimerServiceImplTest: UseCaseBase() {

    private lateinit var timerService: TimerServiceImpl

    @Mock
    private lateinit var propertiesConfig: PropertiesConfig

    @Mock
    private lateinit var escalationPolicyService: EscalationPolicyService

    @BeforeEach
    fun setUp() {
        timerService = Mockito.spy(TimerServiceImpl(propertiesConfig = propertiesConfig))
        timerService.timerList.clear()
    }

    @Test
    fun givenTimerListEmptyWhenScheduleTimerThenAssertTimerStarted() {
        // Arrange
        Mockito.`when`(propertiesConfig.targetLevelInterval).thenReturn(15 * 60) // set interval 15 min delay
        // Act
        timerService.scheduleTimer(SERVICE_NAME1, SERVICE_NAME1_MESSAGE, escalationPolicyService)
        // Assert
        assertAll("givenTimerListEmptyWhenScheduleTimerThenAssertTimerStarted",
                { Assertions.assertThat(timerService.timerList).isNotEmpty},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME1]?.isFinished()).isFalse()},
        )
        cancelTimers()
    }

    @Test
    fun givenTimerListNotEmptyWhenScheduleTimerThenAssertTimerNotStarted() {
        // Arrange
        val createTimerEvent2 = createTimerEvent()
        timerService.timerList[SERVICE_NAME2] = createTimerEvent2
        // Act
        timerService.scheduleTimer(SERVICE_NAME2, SERVICE_NAME1_MESSAGE, escalationPolicyService)
        // Assert
        assertAll("givenTimerListEmptyWhenScheduleTimerThenAssertTimerStarted",
                { Assertions.assertThat(timerService.timerList).isNotEmpty},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME2]).isNotNull()},
                { Assertions.assertThat(timerService.timerList[SERVICE_NAME2]).isEqualTo(createTimerEvent2)},
        )
    }

    @Test
    fun givenTimerListNotEmptyWhenRemoveTimerThenAssertTimerList() {
        // Arrange
        val timerEvent = createTimerEvent()
        timerService.timerList[SERVICE_NAME2] = timerEvent
        // Act
        timerService.removeTimer(SERVICE_NAME2)
        // Assert
        Assertions.assertThat(timerService.timerList).isEmpty()
    }

    @Test
    fun givenTimerListNotEmptyWhenAckReceivedThenAssertTimerList() {
        // Arrange
        val timerEvent = createTimerEvent()
        timerService.timerList[SERVICE_NAME2] = timerEvent
        // Act
        timerService.ackReceived(SERVICE_NAME2)
        // Assert
        Assertions.assertThat(timerService.timerList).isEmpty()
    }

    @Test
    fun givenTimerListDifferentTimerEventWhenAckReceivedThenAssertTimerList() {
        // Arrange
        val timerEvent = createTimerEvent()
        timerService.timerList[SERVICE_NAME1] = timerEvent
        // Act
        timerService.ackReceived(SERVICE_NAME2)
        // Assert
        Assertions.assertThat(timerService.timerList).isNotEmpty
    }

    /**
     * Cancels timers
     */
    private fun cancelTimers() {
        timerService.timerList.forEach { (_, u) -> u.cancel() }
    }

    /**
     * Creates timer event
     */
    private fun createTimerEvent(): TimerEvent {
        return TimerEvent(serviceName = SERVICE_NAME2, timerService = timerService,
                escalationPolicyService = escalationPolicyService, message = SERVICE_NAME2_MESSAGE)
    }
}