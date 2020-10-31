package com.aircall.alertnotification.service.impl

import com.aircall.alertnotification.adapter.AlertAdapter
import com.aircall.alertnotification.service.AlertService
import com.aircall.alertnotification.service.PagerService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class AlertServiceImpl(@Qualifier("alertAdapterImpl") private val alertAdapter: AlertAdapter,
                       @Qualifier("pagerServiceImpl") private val pagerService: PagerService) : AlertService {

    override fun receiveAlert() {
        val alert = alertAdapter.receiveAlert()
        pagerService.receiveAlert(alert)
    }

}
