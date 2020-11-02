package com.aircall.alertnotification.persistence.impl

import com.aircall.alertnotification.model.web.MonitoredService
import com.aircall.alertnotification.persistence.MonitoredServiceRepository
import org.springframework.stereotype.Component

@Component
class MonitoredServiceRepositoryImpl : MonitoredServiceRepository {

    var serviceList: MutableList<MonitoredService> = ArrayList()

    override fun findMonitoredService(serviceName: String): MonitoredService? =
            serviceList.stream()
                    .filter { a -> a.serviceName == serviceName }
                    .findFirst()
                    .orElse(null)

    override fun saveMonitoredService(monitoredService: MonitoredService) {
        val monitoredServiceInList = findMonitoredService(monitoredService.serviceName)
        if (monitoredServiceInList != null) {
            serviceList.remove(monitoredServiceInList)
        }
        serviceList.add(monitoredService)
    }
}
