package com.aircall.alertnotification.persistence

import com.aircall.alertnotification.model.target.Level
import com.aircall.alertnotification.model.web.MonitoredService

interface MonitoredServiceRepository {

    fun findMonitoredService(serviceName: String) : MonitoredService
    fun updateMonitoredService(serviceName: String, levels: List<Level>)
}