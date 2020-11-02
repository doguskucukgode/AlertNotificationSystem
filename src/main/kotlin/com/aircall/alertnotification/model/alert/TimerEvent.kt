package com.aircall.alertnotification.model.alert

import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.TimerService
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Timer event model for scheduling monitored services when incident occurs
 *
 */
class TimerEvent {

    private val logger = LoggerFactory.getLogger(MonitoredServiceTimer::class.java)
    private val timer: Timer = Timer(true)
    private val monitoredServiceTimer : MonitoredServiceTimer

    constructor(serviceName: String, message: String,
                 escalationPolicyService: EscalationPolicyService,
                timerService: TimerService) {
        monitoredServiceTimer = MonitoredServiceTimer(
                escalationPolicyService = escalationPolicyService,
        serviceName = serviceName,
        message = message,
        timerService = timerService)
    }

    /**
     * Schedules timer based on interval in seconds
     *
     * @param interval
     */
    fun scheduleTimer(interval: Int) {
        timer.schedule(monitoredServiceTimer, interval * 1000L, interval * 1000L) // seconds
    }

    /**
     * Cancels timer
     *
     */
    fun cancel() {
        timer.cancel()
        logger.info("TimerTask ${monitoredServiceTimer.serviceName} canceled")
    }

    /**
     * Checks is timer finished
     *
     */
    fun isFinished() = monitoredServiceTimer.finished

    /**
     * Inner class for executing monitored service task
     *
     * @param escalationPolicyService
     * @param message
     * @param serviceName
     * @param timerService
     */
    class MonitoredServiceTimer(val serviceName: String, private val message: String,
                                private val escalationPolicyService: EscalationPolicyService,
                                private val timerService: TimerService) : TimerTask() {
        private val logger = LoggerFactory.getLogger(MonitoredServiceTimer::class.java)
        var finished: Boolean = false

        /**
         * Timer task to execute, when acknowledgement timeout occurs next level targets will be notified
         *
         */
        override fun run() {
            timerService.removeTimer(serviceName)
            cancel()
            escalationPolicyService.notifyTargets(serviceName, message)
            finished = true
            logger.info("TimerTask $serviceName finished")
        }

        /**
         * Equals overridden for only serviceName check as identifier
         *
         */
        override fun equals(other: Any?) = other != null && other is MonitoredServiceTimer && other.serviceName == serviceName

        /**
         * Hashcode implementation
         *
         */
        override fun hashCode(): Int {
            var result = serviceName.hashCode()
            result = 31 * result + message.hashCode()
            return result
        }
    }
}
