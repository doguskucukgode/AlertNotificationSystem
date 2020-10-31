package com.aircall.alertnotification.model.target

class Sms(private val phoneNumber: String) : Target {
    override fun getContact() = this.phoneNumber
}
