package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.AlertAdapter
import com.aircall.alertnotification.service.AlertService
import com.aircall.alertnotification.service.PagerService
import org.springframework.stereotype.Service

/**
 * Alert Service Implementation responsible for receiving alerts
 *
 * @param alertAdapter
 * @param pagerService
 */
@Service
class AlertServiceImpl(var alertAdapter: AlertAdapter,
                       var pagerService: PagerService) : AlertService {

    /**
     * Receives any kind of alerts with respect to alert adapter. It could be implemented in rest protocol
     * or queue based event driven architecture and this function can be called by other threads.
     */
    override fun receiveAlert() {
        val alert = alertAdapter.receiveAlert()
        pagerService.receiveAlert(alert)
    }
}
