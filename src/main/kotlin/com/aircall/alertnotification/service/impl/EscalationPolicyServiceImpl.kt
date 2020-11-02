package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.model.target.Email
import com.aircall.alertnotification.model.target.Sms
import com.aircall.alertnotification.model.web.MonitoredService
import com.aircall.alertnotification.persistence.MonitoredServiceRepository
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.TimerService
import org.springframework.stereotype.Service

/**
 * Escalation Policy Service Implementation responsible notifying targets with respect to levels,
 * updating monitored service (healthy/unhealthy), triggering timer service
 *
 * @param monitoredServiceRepository
 * @param mailAdapter
 * @param smsAdapter
 * @param timerService
 */
@Service
class EscalationPolicyServiceImpl(private val monitoredServiceRepository: MonitoredServiceRepository,
                                  var mailAdapter: TargetAdapter,
                                  var smsAdapter: TargetAdapter,
                                  private val timerService: TimerService) : EscalationPolicyService {

    /**
     *
     * @param serviceName
     * @param message
     */
    override fun notifyTargets(serviceName: String, message: String) {
        val monitoredService = monitoredServiceRepository.findMonitoredService(serviceName)
        if(monitoredService != null && !monitoredService.healthy) {
            val nextLevel = monitoredService.getNextLevel()
            if (nextLevel != null) {
                // send notifications
                nextLevel.targets.forEach { t ->
                    if (t is Email) {
                        mailAdapter.sendAlert(message, t)
                    } else if (t is Sms) {
                        smsAdapter.sendAlert(message, t)
                    }
                }
                incrementMonitoredService(monitoredService)
                // set timer
                timerService.scheduleTimer(serviceName, message, this)
            }
        }
    }

    override fun makeMonitoredServiceUnHealthy(serviceName: String) {
        val monitoredService = monitoredServiceRepository.findMonitoredService(serviceName)
        if(monitoredService != null) {
            monitoredService.makeUnhealthy()
            monitoredServiceRepository.saveMonitoredService(monitoredService)
        }
    }

    override fun ackReceived(serviceName: String) {
        val monitoredService = monitoredServiceRepository.findMonitoredService(serviceName)
        if (monitoredService != null) {
            // stop timer
            timerService.ackReceived(serviceName)
        }
    }

    override fun healthyReceived(serviceName: String) {
        val monitoredService = monitoredServiceRepository.findMonitoredService(serviceName)
        if (monitoredService != null) {
            monitoredService.makeHealthy()
            monitoredServiceRepository.saveMonitoredService(monitoredService)
        }
    }

    fun incrementMonitoredService(monitoredService: MonitoredService) {
        // set current level
        monitoredService.incrementCurrentLevel()
        monitoredServiceRepository.saveMonitoredService(monitoredService)
    }
}
