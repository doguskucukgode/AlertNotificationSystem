package com.aircall.alertnotification.model.web

import com.aircall.alertnotification.model.target.Level

/**
 * Services which can be monitored for their status
 *
 * @param currentLevel
 * @param healthy
 * @param levels
 * @param serviceName
 */
class MonitoredService(val serviceName: String, var healthy: Boolean, var levels: List<Level>, var currentLevel: Int = 0) {

    /**
     * Returns next level targets with respect to current level
     *
     */
    fun getNextLevel() : Level? {
        if (levels.size > currentLevel) {
            return levels[currentLevel]
        }
        return null
    }

    /**
     * Increments current level by 1
     *
     */
    fun incrementCurrentLevel() {
        currentLevel += 1
    }

    /**
     * Makes healthy status false
     *
     */
    fun makeUnhealthy() {
        healthy = false
    }

    /**
     * Makes healthy status true and set current level to initial state
     *
     */
    fun makeHealthy() {
        healthy = true
        currentLevel = 0
    }

    /**
     * Equals overridden for only serviceName check as identifier
     *
     */
    override fun equals(other: Any?) = other != null && other is MonitoredService && other.serviceName == serviceName

    /**
     * Hashcode implementation
     *
     */
    override fun hashCode(): Int {
        var result = serviceName.hashCode()
        result = 31 * result + healthy.hashCode()
        result = 31 * result + currentLevel
        return result
    }
}
