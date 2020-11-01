package com.aircall.alertnotification.persistence.impl

import com.aircall.alertnotification.model.alert.Alert
import com.aircall.alertnotification.persistence.AlertRepository
import org.springframework.stereotype.Component

@Component
class AlertRepositoryImpl : AlertRepository {

    var alertList: MutableList<Alert> = ArrayList()

    override fun addAlert(alert: Alert): Boolean {
        if(findAlert(alert.serviceName) == null) {
            alertList.add(alert)
            return true
        }
        return false
    }

    override fun removeAlert(alert: Alert): Boolean {
        if(findAlert(alert.serviceName) != null) {
            alertList.remove(alert)
            return true
        }
        return false
    }

    override fun findAlert(serviceName: String): Alert? = alertList
            .stream()
            .filter { a -> a.serviceName == serviceName }
            .findFirst()
            .orElse(null)
}
