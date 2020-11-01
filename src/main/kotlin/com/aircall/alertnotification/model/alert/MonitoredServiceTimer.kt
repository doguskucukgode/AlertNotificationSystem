package com.aircall.alertnotification.model.alert

import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.TimerService
import java.util.*

class MonitoredServiceTimer(private val serviceName: String, private val message: String,
                            private val escalationPolicyService: EscalationPolicyService,
                            private val timerService: TimerService) : TimerTask() {


    override fun run() {
        timerService.removeTimer(serviceName)
        escalationPolicyService.notifyTargets(serviceName, message)
    }

    override fun equals(other: Any?) = other != null && other is MonitoredServiceTimer && other.serviceName == serviceName

    override fun hashCode(): Int {
        var result = serviceName.hashCode()
        result = 31 * result + message.hashCode()
        return result
    }

}