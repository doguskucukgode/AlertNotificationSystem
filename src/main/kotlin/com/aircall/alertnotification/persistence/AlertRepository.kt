package com.aircall.alertnotification.persistence

import com.aircall.alertnotification.model.Alert

interface AlertRepository {

    fun addAlert(alert: Alert): Boolean
    fun removeAlert(alert: Alert): Boolean
    fun findAlert(serviceName: String): Alert?
}
