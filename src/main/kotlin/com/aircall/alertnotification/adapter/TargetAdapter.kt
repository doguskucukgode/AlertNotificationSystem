package com.aircall.alertnotification.adapter

import com.aircall.alertnotification.model.target.Target

interface TargetAdapter {

    fun sendAlert(message: String, target: Target)
}
