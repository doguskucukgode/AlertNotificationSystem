package com.aircall.alertnotification.model.alert

import com.aircall.alertnotification.model.types.AlertType
import java.time.LocalDateTime

/**
 * Alert model
 *
 * @param serviceName
 * @param alertType
 * @param message
 * @param startDateTime
 * @param updateDateTime
 */
class Alert(val serviceName: String, val message: String, var alertType: AlertType,
            val startDateTime: LocalDateTime, var updateDateTime: LocalDateTime? = null) {

    override fun equals(other: Any?) = other != null && other is Alert && other.serviceName == serviceName

    override fun hashCode(): Int {
        var result = serviceName.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + alertType.hashCode()
        return result
    }

}
