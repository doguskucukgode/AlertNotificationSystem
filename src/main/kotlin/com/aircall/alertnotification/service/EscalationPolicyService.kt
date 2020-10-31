package com.aircall.alertnotification.service

interface EscalationPolicyService {

    fun notifyTargets(serviceName: String)
}