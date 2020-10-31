package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.model.target.Target
import com.aircall.alertnotification.service.TargetService
import org.springframework.stereotype.Service

@Service
class SmsService(private val smsAdapter: TargetAdapter) : TargetService {

    override fun sendAlert(message: String, target: Target) {
        smsAdapter.sendAlert(message, target)
    }

}
