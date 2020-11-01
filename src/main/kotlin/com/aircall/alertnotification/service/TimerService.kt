package com.aircall.alertnotification.service

interface TimerService {

    fun removeTimer(serviceName: String)
    fun scheduleTimer(serviceName: String, message: String, escalationPolicyService: EscalationPolicyService)
    fun ackReceived(serviceName: String)
}