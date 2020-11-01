package com.aircall.alertnotification.service

import com.aircall.alertnotification.model.alert.Alert

interface PagerService {

    fun receiveAlert(alert: Alert)
}