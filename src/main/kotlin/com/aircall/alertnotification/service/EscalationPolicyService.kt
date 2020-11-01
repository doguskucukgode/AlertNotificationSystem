package com.aircall.alertnotification.service

interface EscalationPolicyService {

    fun notifyTargets(serviceName: String, message: String)
    fun ackReceived(serviceName: String)
    fun healthyReceived(serviceName: String)
}