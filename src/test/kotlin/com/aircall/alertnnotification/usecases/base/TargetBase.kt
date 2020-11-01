package com.aircall.alertnnotification.usecases.base

import com.aircall.alertnotification.model.target.Email
import com.aircall.alertnotification.model.target.Level
import com.aircall.alertnotification.model.target.Sms
import com.aircall.alertnotification.model.target.Target

open class TargetBase {
    protected val PHONE1 = "+123456789"
    protected val PHONE2 = "+987654321"
    protected val PHONE3 = "+123459876"
    protected val PHONE4 = "+543216789"
    protected val PHONE5 = "+543219876"

    protected val EMAIL1 = "abc1@test.com"
    protected val EMAIL2 = "abc2@test.com"
    protected val EMAIL3 = "abc3@test.com"
    protected val EMAIL4 = "abc4@test.com"
    protected val EMAIL5 = "abc5@test.com"

    protected val SMS_TARGET1 = Sms(phoneNumber = PHONE1)
    protected val SMS_TARGET2 = Sms(phoneNumber = PHONE2)
    protected val SMS_TARGET3 = Sms(phoneNumber = PHONE3)
    protected val SMS_TARGET4 = Sms(phoneNumber = PHONE4)
    protected val SMS_TARGET5 = Sms(phoneNumber = PHONE5)

    protected val EMAIL_TARGET1 = Email(emailAddress = EMAIL1)
    protected val EMAIL_TARGET2 = Email(emailAddress = EMAIL2)
    protected val EMAIL_TARGET3 = Email(emailAddress = EMAIL3)
    protected val EMAIL_TARGET4 = Email(emailAddress = EMAIL4)
    protected val EMAIL_TARGET5 = Email(emailAddress = EMAIL5)

    protected fun createTargets1(): List<Target> = listOf(SMS_TARGET1, EMAIL_TARGET1)
    protected fun createTargets2(): List<Target> = listOf(SMS_TARGET2, EMAIL_TARGET2)
    protected fun createTargets3(): List<Target> = listOf(SMS_TARGET3, EMAIL_TARGET3)
    protected fun createTargets4(): List<Target> = listOf(SMS_TARGET4, EMAIL_TARGET4)
    protected fun createTargets5(): List<Target> = listOf(SMS_TARGET5, EMAIL_TARGET5)


    protected fun createLevels1(): List<Level> = listOf(
            Level(priority = 1, targets = createTargets1()),
            Level(priority = 2, targets = createTargets2()),
            Level(priority = 3, targets = createTargets3()),
    )

    protected fun createLevels2(): List<Level> = listOf(
            Level(priority = 1, targets = createTargets4()),
            Level(priority = 2, targets = createTargets5())
    )

    protected fun createLevels3(): List<Level> = listOf(
            Level(priority = 1, targets = createTargets1()),
            Level(priority = 2, targets = createTargets2()),
            Level(priority = 3, targets = createTargets3()),
            Level(priority = 4, targets = createTargets4()),
            Level(priority = 5, targets = createTargets5())
    )

    protected fun createLevels4(): List<Level> = listOf(
            Level(priority = 1, targets = createTargets1())
    )

}
