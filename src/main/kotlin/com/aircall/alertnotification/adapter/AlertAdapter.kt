package com.aircall.alertnotification.adapter

import com.aircall.alertnotification.model.Alert

interface AlertAdapter {

    fun receiveAlert() : Alert
}