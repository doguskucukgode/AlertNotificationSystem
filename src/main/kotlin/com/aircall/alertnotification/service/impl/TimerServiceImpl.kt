package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.config.PropertiesConfig
import com.aircall.alertnotification.model.alert.MonitoredServiceTimer
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.TimerService
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class TimerServiceImpl(private val propertiesConfig: PropertiesConfig) : TimerService {

    val timerList : MutableMap<String, Timer> = HashMap()

    override fun removeTimer(serviceName: String) {
        timerList.remove(serviceName)
    }

    override fun scheduleTimer(serviceName: String, message: String, escalationPolicyService: EscalationPolicyService) {
        val timer = Timer(true)
        timer.scheduleAtFixedRate(MonitoredServiceTimer(
                escalationPolicyService = escalationPolicyService,
                serviceName = serviceName,
                message = message,
                timerService = this), 0, propertiesConfig.targetLevelInterval * 1000L)
        println("TimerTask $message started")
    }

    override fun ackReceived(serviceName: String) {
        if(timerList.containsKey(serviceName)) {
            timerList[serviceName]?.cancel()
            removeTimer(serviceName)
        }
    }
}