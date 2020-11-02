package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.model.target.Target
import com.aircall.alertnotification.service.TargetService
import org.springframework.stereotype.Service

/**
 *  Mail sender service
 *
 *  @param mailAdapter
 */
@Service
class MailService(private val mailAdapter: TargetAdapter) : TargetService {

    /**
     * Sending mail to target
     *
     * @param message
     * @param target
     */
    override fun sendAlert(message: String, target: Target) {
        mailAdapter.sendAlert(message, target)
    }
}
