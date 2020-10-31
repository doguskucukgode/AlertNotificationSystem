package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.model.target.Target
import com.aircall.alertnotification.service.TargetService
import org.springframework.stereotype.Service

@Service
class MailService(private val mailAdapter: TargetAdapter) : TargetService {

    override fun sendAlert(message: String, target: Target) {
        mailAdapter.sendAlert(message, target)
    }
}
