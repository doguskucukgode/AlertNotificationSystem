package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.config.PropertiesConfig
import com.aircall.alertnotification.model.alert.TimerEvent
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.TimerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Schedules timer for monitored services when alert occurs
 *
 * @param propertiesConfig
 */
@Service
class TimerServiceImpl(var propertiesConfig: PropertiesConfig) : TimerService {

    private val logger = LoggerFactory.getLogger(TimerServiceImpl::class.java)
    val timerList : MutableMap<String, TimerEvent> = HashMap()

    /**
     * Removes service timer from list
     *
     * @param serviceName
     */
    override fun removeTimer(serviceName: String) {
        timerList.remove(serviceName)
    }

    /**
     * Schedules timer for monitored services when alert occurs and saves in list
     *
     * @param escalationPolicyService
     * @param message
     * @param serviceName
     */
    override fun scheduleTimer(serviceName: String, message: String, escalationPolicyService: EscalationPolicyService) {
        if(!timerList.containsKey(serviceName)) {
            val timerEvent = TimerEvent(serviceName = serviceName, timerService = this,
                    escalationPolicyService = escalationPolicyService, message = message)
            timerEvent.scheduleTimer(propertiesConfig.targetLevelInterval)
            timerList[serviceName] = timerEvent
            logger.info("TimerTask $message started")
        }
    }

    /**
     * Stops timer and removes from list when acknowledgement alert receives
     */
    override fun ackReceived(serviceName: String) {
        if(timerList.containsKey(serviceName)) {
            timerList[serviceName]?.cancel()
            removeTimer(serviceName)
        }
    }
}
