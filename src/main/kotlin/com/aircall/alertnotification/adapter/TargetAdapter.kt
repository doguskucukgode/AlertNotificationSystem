package com.aircall.alertnotification.adapter

import com.aircall.alertnotification.model.target.Target

/**
 * Mocked target adapter which has only one function. Mock mail and Sms services use sendAlert function
 */
interface TargetAdapter {

    fun sendAlert(message: String, target: Target)
}
