package com.aircall.alertnotification.persistence.impl

import com.aircall.alertnotification.model.alert.Alert
import com.aircall.alertnotification.persistence.AlertRepository
import org.springframework.stereotype.Component

/**
 * Alert repository implementation at runtime
 *
 */
@Component
class AlertRepositoryImpl : AlertRepository {

    var alertList: MutableList<Alert> = ArrayList()

    /**
     * Adds new alert if not exists
     *
     * @param alert
     */
    override fun addAlert(alert: Alert): Boolean {
        if(findAlert(alert.serviceName) == null) {
            alertList.add(alert)
            return true
        }
        return false
    }

    /**
     * Removes alert if exists
     *
     * @param alert
     */
    override fun removeAlert(alert: Alert): Boolean {
        if(findAlert(alert.serviceName) != null) {
            alertList.remove(alert)
            return true
        }
        return false
    }

    /**
     * Finds alert based on service name
     * If not found then returns null
     *
     * @param serviceName
     */
    override fun findAlert(serviceName: String): Alert? = alertList
            .stream()
            .filter { a -> a.serviceName == serviceName }
            .findFirst()
            .orElse(null)
}
