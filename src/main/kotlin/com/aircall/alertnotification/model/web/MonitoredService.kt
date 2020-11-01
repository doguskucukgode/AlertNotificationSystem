package com.aircall.alertnotification.model.web

import com.aircall.alertnotification.model.target.Level
class MonitoredService(val serviceName: String, var healthy: Boolean, var levels: List<Level>, var currentLevel: Int = 0) {

    override fun equals(other: Any?) = other != null && other is MonitoredService && other.serviceName == serviceName

    fun getNextLevel() : Level? {
        if (levels.size > currentLevel) {
            return levels[currentLevel]
        }
        return null
    }

    fun incrementCurrentLevel() {
        currentLevel += 1
    }

    fun makeUnhealthy() {
        healthy = false
    }

    fun makeHealthy() {
        healthy = true
        currentLevel = 0
    }

    override fun hashCode(): Int {
        var result = serviceName.hashCode()
        result = 31 * result + healthy.hashCode()
        result = 31 * result + currentLevel
        return result
    }
}

