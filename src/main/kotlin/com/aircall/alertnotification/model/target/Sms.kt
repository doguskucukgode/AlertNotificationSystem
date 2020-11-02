package com.aircall.alertnotification.model.target

/**
 * Sms target with phone number
 *
 * @param phoneNumber
 */
class Sms(private val phoneNumber: String) : Target {
    override fun getContact() = this.phoneNumber
}
