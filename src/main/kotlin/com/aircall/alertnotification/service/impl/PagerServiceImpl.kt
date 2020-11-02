package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.model.alert.Alert
import com.aircall.alertnotification.model.types.AlertType
import com.aircall.alertnotification.persistence.AlertRepository
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.PagerService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Pager Service Implementation responsible for identifying alerts and
 * forwarding to Escalation Policy Service
 *
 * @param escalationPolicyService
 * @param alertRepository
 */
@Service
class PagerServiceImpl(private val escalationPolicyService: EscalationPolicyService,
                       private val alertRepository: AlertRepository) : PagerService {

    @Synchronized
    override fun receiveAlert(alert: Alert) {
        if (alert.alertType == AlertType.INCIDENT) {
            if (alertRepository.findAlert(alert.serviceName) == null) {
                updateAlertTime(alert)
                escalationPolicyService.makeMonitoredServiceUnHealthy(alert.serviceName)
                escalationPolicyService.notifyTargets(alert.serviceName, alert.message)
            }
        } else if (alert.alertType == AlertType.ACKNOWLEDGE) {
            if (alertRepository.findAlert(alert.serviceName) != null) {
                updateAlertTime(alert)
                escalationPolicyService.ackReceived(alert.serviceName)
            }
        } else if (alert.alertType == AlertType.HEALTHY) {
            if (alertRepository.findAlert(alert.serviceName) != null) {
                updateAlertTime(alert)
                escalationPolicyService.healthyReceived(alert.serviceName)
            }
        } else {
            throw UnsupportedOperationException("Unknown alert type")
        }
    }

    fun updateAlertTime(alert: Alert) {
        alert.updateDateTime = LocalDateTime.now()
        alertRepository.addAlert(alert)
    }
}
