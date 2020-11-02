package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.model.target.Target
import com.aircall.alertnotification.service.TargetService
import org.springframework.stereotype.Service

/**
 * Sms sender service
 *
 * @param smsAdapter
 */
@Service
class SmsService(private val smsAdapter: TargetAdapter) : TargetService {

    /**
     * Sending sms to target
     *
     * @param message
     * @param target
     */
    override fun sendAlert(message: String, target: Target) {
        smsAdapter.sendAlert(message, target)
    }
}
