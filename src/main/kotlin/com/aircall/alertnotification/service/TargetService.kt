package com.aircall.alertnotification.service

import com.aircall.alertnotification.model.target.Target

interface TargetService {
    fun sendAlert(message: String, target: Target)
}
