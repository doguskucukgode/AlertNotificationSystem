package com.aircall.alertnotification.persistence.impl

import com.aircall.alertnotification.model.web.MonitoredService
import com.aircall.alertnotification.persistence.MonitoredServiceRepository
import org.springframework.stereotype.Component

/**
 * Monitored service repository implementation at runtime
 *
 */
@Component
class MonitoredServiceRepositoryImpl : MonitoredServiceRepository {

    var serviceList: MutableList<MonitoredService> = ArrayList()

    /**
     * Finds monitored service based on service name
     * If not found then returns null
     *
     * @param serviceName
     */
    override fun findMonitoredService(serviceName: String): MonitoredService? =
            serviceList.stream()
                    .filter { a -> a.serviceName == serviceName }
                    .findFirst()
                    .orElse(null)

    /**
     * Saves new monitored service if not exists
     * Updates monitored service if exists
     *
     * @param monitoredService
     */
    override fun saveMonitoredService(monitoredService: MonitoredService) {
        val monitoredServiceInList = findMonitoredService(monitoredService.serviceName)
        if (monitoredServiceInList != null) {
            serviceList.remove(monitoredServiceInList)
        }
        serviceList.add(monitoredService)
    }
}
