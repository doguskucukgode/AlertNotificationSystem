package com.aircall.alertnotification.model.alert

import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.TimerService
import java.util.*

class TimerEvent {

    private val timer: Timer = Timer(true)
    val monitoredServiceTimer : MonitoredServiceTimer

    constructor(serviceName: String, message: String,
                 escalationPolicyService: EscalationPolicyService,
                timerService: TimerService) {
        monitoredServiceTimer = MonitoredServiceTimer(
                escalationPolicyService = escalationPolicyService,
        serviceName = serviceName,
        message = message,
        timerService = timerService)
    }

    fun scheduleTimer(interval: Int) {
        timer.schedule(monitoredServiceTimer, interval * 1000L, interval * 1000L) // seconds
    }

    fun cancel() {
        timer.cancel()
    }

    fun isFinished() = monitoredServiceTimer.finished

    class MonitoredServiceTimer(private val serviceName: String, private val message: String,
                                 private val escalationPolicyService: EscalationPolicyService,
                                 private val timerService: TimerService) : TimerTask() {

        var finished: Boolean = false

        override fun run() {
            timerService.removeTimer(serviceName)
            escalationPolicyService.incrementMonitoredService(serviceName)
            cancel()
            escalationPolicyService.notifyTargets(serviceName, message)
            finished = true
            println("TimerTask $serviceName finished")
        }

        override fun equals(other: Any?) = other != null && other is MonitoredServiceTimer && other.serviceName == serviceName

        override fun hashCode(): Int {
            var result = serviceName.hashCode()
            result = 31 * result + message.hashCode()
            return result
        }

    }
}

