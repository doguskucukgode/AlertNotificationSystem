package com.aircall.alertnnotification.persistence.impl

import com.aircall.alertnnotification.usecases.base.UseCaseBase
import com.aircall.alertnotification.persistence.impl.AlertRepositoryImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AlertRepositoryImplTest: UseCaseBase() {

    @Spy
    private lateinit var alertRepository: AlertRepositoryImpl

    @BeforeEach
    fun setUp() {
        alertRepository.alertList.clear()
    }

    @Test
    fun givenAlertListEmptyWhenAddAlertThenAssertAlertList() {
        // Arrange
        val alert = createAlert1()
        // Act
        val result = alertRepository.addAlert(alert)
        // Assert
        assertAll("givenAlertListEmptyWhenAddAlertThenAssertAlertList",
                { Assertions.assertThat(result).isTrue()},
                { Assertions.assertThat(alertRepository.alertList).isNotEmpty},
                { Assertions.assertThat(alertRepository.alertList[0]).isEqualTo(alert)},
        )
    }

    @Test
    fun givenAlertListNotEmptyWhenAddAlertThenAssertAlertList() {
        // Arrange
        val alert = createAlert1()
        alertRepository.alertList.add(alert)
        // Act
        val result = alertRepository.addAlert(alert)
        // Assert
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun givenAlertListNotEmptyWhenRemoveAlertThenAssertAlertList() {
        // Arrange
        val alert = createAlert1()
        alertRepository.alertList.add(alert)
        // Act
        val result = alertRepository.removeAlert(alert)
        // Assert
        assertAll("givenAlertListNotEmptyWhenRemoveAlertThenAssertAlertList",
                { Assertions.assertThat(result).isTrue()},
                { Assertions.assertThat(alertRepository.alertList).isEmpty()},
        )
    }

    @Test
    fun givenAlertListEmptyWhenRemoveAlertThenAssertResult() {
        // Arrange
        val alert1 = createAlert1()
        val alert2 = createAlert2()
        alertRepository.alertList.add(alert1)
        // Act
        val result = alertRepository.removeAlert(alert2)
        // Assert
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun givenAlertListNotEmptyWhenFindAlertThenAssertResult() {
        // Arrange
        val alert = createAlert1()
        alertRepository.alertList.add(alert)
        // Act
        val result = alertRepository.findAlert(SERVICE_NAME1)
        // Assert
        assertAll("givenAlertListEmptyWhenAddAlertThenAssertAlertList",
                { Assertions.assertThat(result).isEqualTo(alert)},
                { Assertions.assertThat(alertRepository.alertList).isNotEmpty()},
        )
    }

    @Test
    fun givenAlertListEmptyWhenFindAlertThenAssertNull() {
        //  Arrange And Act
        val result = alertRepository.findAlert(SERVICE_NAME1)
        // Assert
        Assertions.assertThat(result).isNull()
    }
}