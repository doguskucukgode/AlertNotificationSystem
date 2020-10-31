package com.aircall.alertnotification.service

import com.aircall.alertnotification.model.Alert

interface PagerService {

    fun receiveAlert(alert: Alert)
}