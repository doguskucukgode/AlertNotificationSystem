package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.config.PropertiesConfig
import com.aircall.alertnotification.model.alert.TimerEvent
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.TimerService
import org.springframework.stereotype.Service
import kotlin.collections.HashMap

@Service
class TimerServiceImpl(var propertiesConfig: PropertiesConfig) : TimerService {

    val timerList : MutableMap<String, TimerEvent> = HashMap()

    override fun removeTimer(serviceName: String) {
        timerList.remove(serviceName)
    }

    override fun scheduleTimer(serviceName: String, message: String, escalationPolicyService: EscalationPolicyService) {
        if(!timerList.containsKey(serviceName)) {
            val timerEvent = TimerEvent(serviceName = serviceName, timerService = this,
                    escalationPolicyService = escalationPolicyService, message = message)
            timerEvent.scheduleTimer(propertiesConfig.targetLevelInterval)
            timerList[serviceName] = timerEvent
            println("TimerTask $message started")
        }
    }

    override fun ackReceived(serviceName: String) {
        if(timerList.containsKey(serviceName)) {
            timerList[serviceName]?.cancel()
            removeTimer(serviceName)
        }
    }
}