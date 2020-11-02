package com.aircall.alertnotification.adapter

import com.aircall.alertnotification.model.alert.Alert

/**
 * Mocked alert adapter which has only one function. It can be implemented various ways TCP/IP, REST, GRPC, Event Based
 * systems AMQP
 */
interface AlertAdapter {

    fun receiveAlert() : Alert
}
