package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.model.Alert
import com.aircall.alertnotification.model.types.AlertType
import com.aircall.alertnotification.persistence.AlertRepository
import com.aircall.alertnotification.service.EscalationPolicyService
import com.aircall.alertnotification.service.PagerService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PagerServiceImpl(
        @Qualifier("escalationPolicyServiceImpl") private val escalationPolicyService: EscalationPolicyService,
        @Qualifier("alertRepositoryImpl") private val alertRepository: AlertRepository) : PagerService {

    override fun receiveAlert(alert: Alert) {
        if (alert.alertType == AlertType.INCIDENT) {
            if (alertRepository.findAlert(alert.serviceName) == null) {
                alert.updateDateTime = LocalDateTime.now()
                alertRepository.addAlert(alert)

            }
        } else if (alert.alertType == AlertType.ACKNOWLEDGE) {

        } else if (alert.alertType == AlertType.HEALTHY) {

        }
    }
}