package com.aircall.alertnotification.persistence

import com.aircall.alertnotification.model.web.MonitoredService

interface MonitoredServiceRepository {

    fun findMonitoredService(serviceName: String) : MonitoredService?
    fun saveMonitoredService(monitoredService: MonitoredService)
}
