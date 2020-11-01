package com.aircall.alertnnotification.usecases.base

import com.aircall.alertnnotification.usecases.base.TargetBase
import com.aircall.alertnotification.model.alert.Alert
import com.aircall.alertnotification.model.types.AlertType
import com.aircall.alertnotification.model.web.MonitoredService
import java.time.LocalDateTime

open class UseCaseBase: TargetBase() {

    protected val SERVICE_NAME1 = "Service1"
    protected val SERVICE_NAME1_MESSAGE = "dysfunction in Service1"
    protected val SERVICE_NAME2 = "Service2"
    protected val SERVICE_NAME2_MESSAGE = "dysfunction in Service2"
    protected val SERVICE_NAME3 = "Service3"
    protected val SERVICE_NAME3_MESSAGE = "dysfunction in Service3"
    protected val SERVICE_NAME4 = "Service4"
    protected val SERVICE_NAME4_MESSAGE = "dysfunction in Service4"
    protected val START_DATE = LocalDateTime.of(2020, 10,10, 12, 55, 0)

    protected fun createAlert1() = Alert(serviceName = SERVICE_NAME1,
                message = SERVICE_NAME1_MESSAGE,
        alertType = AlertType.INCIDENT,
        startDateTime = START_DATE)

    protected fun createAlert1Ack() = Alert(serviceName = SERVICE_NAME1,
            message = SERVICE_NAME1_MESSAGE,
            alertType = AlertType.ACKNOWLEDGE,
            startDateTime = START_DATE)

    protected fun createMonitoredServiceList() = listOf(
            MonitoredService(serviceName = SERVICE_NAME1, healthy = true, levels = createLevels1()),
            MonitoredService(serviceName = SERVICE_NAME2, healthy = true, levels = createLevels2()),
            MonitoredService(serviceName = SERVICE_NAME3, healthy = true, levels = createLevels3()),
            MonitoredService(serviceName = SERVICE_NAME4, healthy = true, levels = createLevels4()),
    )
}