package com.aircall.alertnotification.model.web

import com.aircall.alertnotification.model.target.Level

class MonitoredService(val serviceName: String, var healthy: Boolean, var levels: List<Level>, var currentLevel: Number) {

}