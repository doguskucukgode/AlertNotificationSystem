package com.aircall.alertnotification.async

import com.aircall.alertnotification.MedicalRecordUpdatedEvent
import com.aircall.alertnotification.logger
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component

@Component
@EnableAsync
internal class Receiver {
    @EventListener
    fun handleSync(event: MedicalRecordUpdatedEvent?) {
        logger.info("thread '{}' handling '{}' event", Thread.currentThread(), event)
    }

    @Async
    @EventListener
    fun handleAsync(event: MedicalRecordUpdatedEvent?) {
        logger.info("thread '{}' handling '{}' event", Thread.currentThread(), event)
    }
}
