package com.aircall.alertnotification.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * Configuration services reading from application.properties
 */
@Configuration
class PropertiesConfig (

        @Value(value = "\${target.level.interval}")
        val targetLevelInterval: Int,
)
