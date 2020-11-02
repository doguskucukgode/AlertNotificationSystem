package com.aircall.alertnotification.model.target

/**
 * Email target with email address
 *
 * @param emailAddress
 */
open class Email (private val emailAddress: String): Target {
    override fun getContact() = this.emailAddress
}
