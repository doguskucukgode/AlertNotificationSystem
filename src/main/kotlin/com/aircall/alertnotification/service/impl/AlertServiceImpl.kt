package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.AlertAdapter
import com.aircall.alertnotification.service.AlertService
import com.aircall.alertnotification.service.PagerService
import org.springframework.stereotype.Service

@Service
class AlertServiceImpl(var alertAdapter: AlertAdapter,
                       var pagerService: PagerService) : AlertService {

    override fun receiveAlert() {
        val alert = alertAdapter.receiveAlert()
        pagerService.receiveAlert(alert)
    }

}
