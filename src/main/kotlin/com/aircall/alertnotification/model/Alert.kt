package com.aircall.alertnotification.model

import com.aircall.alertnotification.model.types.AlertType
import java.time.LocalDateTime

class Alert(val serviceName: String, val message: String, var alertType: AlertType, val startDateTime: LocalDateTime, var updateDateTime: LocalDateTime) {

    override fun equals(other: Any?) = other != null && other is Alert && other.serviceName == serviceName

}