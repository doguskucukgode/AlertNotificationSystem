package com.aircall.alertnnotification.service.impl

import com.aircall.alertnnotification.usecases.base.UseCaseBase
import com.aircall.alertnotification.adapter.AlertAdapter
import com.aircall.alertnotification.service.PagerService
import com.aircall.alertnotification.service.impl.AlertServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AlertServiceImplTest: UseCaseBase() {

    private lateinit var alertService: AlertServiceImpl

    @Mock
    private lateinit var alertAdapter: AlertAdapter

    @Mock
    private lateinit var pagerService: PagerService

    @BeforeEach
    fun setUp() {
        alertService = Mockito.spy(AlertServiceImpl(alertAdapter = alertAdapter, pagerService = pagerService))
    }

    @Test
    fun givenAlertAdapterWhenReceiveAlertThenAssertPagerService() {
        // Arrange
        val alert = createAlert1()
        Mockito.`when`(alertAdapter.receiveAlert()).thenReturn(alert)

        // Act
        alertService.receiveAlert()

        // Assert
        Mockito.verify(pagerService, Mockito.times(1)).receiveAlert(alert)
    }

}
