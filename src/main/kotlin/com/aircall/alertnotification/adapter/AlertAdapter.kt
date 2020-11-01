package com.aircall.alertnotification.adapter

import com.aircall.alertnotification.model.alert.Alert

interface AlertAdapter {

    fun receiveAlert() : Alert
}