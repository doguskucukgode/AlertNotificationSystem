package com.aircall.alertnotification.main

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class AlertSystem : CommandLineRunner {

    @Autowired
    private val appContext: ApplicationContext? = null

    override fun run(vararg args: String?) {
        SpringApplication.exit(appContext, ExitCodeGenerator { 0 })
    }
}
