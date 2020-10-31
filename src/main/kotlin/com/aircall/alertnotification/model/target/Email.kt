package com.aircall.alertnotification.model.target

open class Email (private val emailAddress: String): Target {
    override fun getContact() = this.emailAddress
}
