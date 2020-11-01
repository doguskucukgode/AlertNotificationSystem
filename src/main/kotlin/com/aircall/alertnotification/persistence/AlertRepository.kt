package com.aircall.alertnotification.persistence

import com.aircall.alertnotification.model.alert.Alert

interface AlertRepository {

    fun addAlert(alert: Alert): Boolean
    fun removeAlert(alert: Alert): Boolean
    fun findAlert(serviceName: String): Alert?
}
