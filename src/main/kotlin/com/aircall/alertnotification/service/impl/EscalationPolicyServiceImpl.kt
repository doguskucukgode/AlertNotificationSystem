package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.model.target.Email
import com.aircall.alertnotification.model.target.Sms
import com.aircall.alertnotification.model.web.MonitoredService
import com.aircall.alertnotification.persistence.MonitoredServiceRepository
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.TimerService
import org.springframework.stereotype.Service

@Service
class EscalationPolicyServiceImpl(private val monitoredServiceRepository: MonitoredServiceRepository,
                                  var mailAdapter: TargetAdapter,
                                  var smsAdapter: TargetAdapter,
                                  private val timerService: TimerService) : EscalationPolicyService {

    override fun notifyTargets(serviceName: String, message: String) {
        val monitoredService = monitoredServiceRepository.findMonitoredService(serviceName)
        if(monitoredService != null) {
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

    fun incrementMonitoredService(monitoredService: MonitoredService) {
            // set current level
            monitoredService.incrementCurrentLevel()
            monitoredService.makeUnhealthy()
            monitoredServiceRepository.saveMonitoredService(monitoredService)
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
            // stop timer if there is any
            timerService.ackReceived(serviceName)
            monitoredService.makeHealthy()
            monitoredServiceRepository.saveMonitoredService(monitoredService)
        }
    }

}
