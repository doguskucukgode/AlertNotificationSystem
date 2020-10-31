package com.aircall.alertnotification

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component


@SpringBootApplication
@EnableAsync
class AlertNotificationApplication

fun main(args: Array<String>) {
    runApplication<AlertNotificationApplication>(*args)
}

val logger: Logger = LoggerFactory.getLogger(AlertNotificationApplication::class.java)


@Bean
fun taskExecutor(): TaskExecutor? {
    return SimpleAsyncTaskExecutor()
}

internal class MedicalRecordUpdatedEvent(private val id: String) {
    override fun toString(): String {
        return "MedicalRecordUpdatedEvent{" +
                "id='" + id + '\'' +
                '}'
    }
}




