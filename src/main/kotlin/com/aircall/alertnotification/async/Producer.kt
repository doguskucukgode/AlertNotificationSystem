package com.aircall.alertnotification.async

import com.aircall.alertnotification.MedicalRecordUpdatedEvent
import com.aircall.alertnotification.logger
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component

@Component
@EnableAsync
class Producer(private val publisher: ApplicationEventPublisher) {
    fun create(id: String?) {
        publisher.publishEvent(MedicalRecordUpdatedEvent(id!!))
    }

    @Async
    fun asyncMethod() {
        logger.info("running async method with thread '{}'", Thread.currentThread())
    }

}
