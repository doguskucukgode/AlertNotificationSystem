package com.aircall.alertnotification.adapter.impl

import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.model.target.Target
import org.springframework.stereotype.Component

@Component
class SmsAdapter : TargetAdapter {

    override fun sendAlert(message: String, target: Target) {
        TODO("Not yet implemented")
    }
}
