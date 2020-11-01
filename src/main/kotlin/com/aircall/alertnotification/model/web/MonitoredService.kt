package com.aircall.alertnotification.model.web

import com.aircall.alertnotification.model.target.Level

class MonitoredService(val serviceName: String, var healthy: Boolean, var levels: List<Level>, var currentLevel: Int = 0) {

    fun getNextLevel() : Level? {
        if (levels.size > currentLevel) {
            return levels[currentLevel]
        }
        return null
    }

    fun incrementCurrentLevel() = currentLevel++

    fun makeUnhealthy() {
        healthy = false
        currentLevel = 0
    }

    fun makeHealthy() {
        healthy = true
    }
}
