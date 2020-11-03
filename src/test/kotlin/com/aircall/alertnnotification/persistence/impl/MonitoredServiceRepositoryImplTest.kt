package com.aircall.alertnnotification.persistence.impl

import com.aircall.alertnnotification.usecases.base.UseCaseBase
import com.aircall.alertnotification.persistence.impl.MonitoredServiceRepositoryImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class MonitoredServiceRepositoryImplTest: UseCaseBase() {

    @Spy
    private lateinit var monitoredRepository: MonitoredServiceRepositoryImpl

    @BeforeEach
    fun setUp() {
        monitoredRepository.serviceList.clear()
    }

    @Test
    fun givenMonitoredListEmptyWhenAddMonitoredServiceThenAssertMonitoredServiceList() {
        // Arrange
        val monitoredService = createMonitoredService1()
        // Act
        monitoredRepository.saveMonitoredService(monitoredService)
        // Assert
        monitoredRepository.serviceList
        assertAll("givenMonitoredListEmptyWhenAddMonitoredServiceThenAssertMonitoredServiceList",
                { Assertions.assertThat(monitoredRepository.serviceList).isNotEmpty},
                { Assertions.assertThat(monitoredRepository.serviceList[0]).isEqualTo(monitoredService)},
        )
    }

    @Test
    fun givenMonitoredListNotEmptyWhenAddMonitoredServiceThenAssertMonitoredServiceUpdated() {
        // Arrange
        val monitoredService1 = createMonitoredService1()
        val monitoredService2 = createMonitoredService1()
        monitoredService2.healthy = false
        monitoredRepository.serviceList.add(monitoredService1)
        // Act
        monitoredRepository.saveMonitoredService(monitoredService2)
        // Assert
        monitoredRepository.serviceList
        assertAll("givenMonitoredListNotEmptyWhenAddMonitoredServiceThenAssertMonitoredServiceList",
                { Assertions.assertThat(monitoredRepository.serviceList).isNotEmpty},
                { Assertions.assertThat(monitoredRepository.serviceList.size).isEqualTo(1)},
                { Assertions.assertThat(monitoredRepository.serviceList[0].healthy).isEqualTo(false)},
        )
    }

    @Test
    fun givenMonitoredListNotEmptyWhenFindMonitoredServiceThenAssertMonitoredService() {
        // Arrange
        val monitoredService1 = createMonitoredService1()
        monitoredRepository.serviceList.add(monitoredService1)
        // Act
        val findMonitoredService = monitoredRepository.findMonitoredService(SERVICE_NAME1)
        // Assert
        Assertions.assertThat(findMonitoredService).isEqualTo(monitoredService1)
    }

    @Test
    fun givenMonitoredListEmptyWhenFindMonitoredServiceThenAssertMonitoredServiceNull() {
        // Arrange And Act
        val findMonitoredService = monitoredRepository.findMonitoredService(SERVICE_NAME1)
        // Assert
        Assertions.assertThat(findMonitoredService).isNull()
    }
}