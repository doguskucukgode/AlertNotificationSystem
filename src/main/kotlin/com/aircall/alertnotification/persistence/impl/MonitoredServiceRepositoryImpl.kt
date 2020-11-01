package com.aircall.alertnotification.persistence.impl

import com.aircall.alertnotification.model.web.MonitoredService
import com.aircall.alertnotification.persistence.MonitoredServiceRepository
import org.springframework.stereotype.Component

@Component
class MonitoredServiceRepositoryImpl: MonitoredServiceRepository {

    override fun findMonitoredService(serviceName: String): MonitoredService {
        TODO("Not yet implemented")
    }

    override fun saveMonitoredService(monitoredService: MonitoredService) {
        TODO("Not yet implemented")
    }
}
