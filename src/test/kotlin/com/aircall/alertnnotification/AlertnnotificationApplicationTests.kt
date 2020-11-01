package com.aircall.alertnnotification

import com.aircall.alertnotification.AlertNotificationApplication
import com.aircall.alertnotification.adapter.AlertAdapter
import com.aircall.alertnotification.adapter.TargetAdapter
import com.aircall.alertnotification.persistence.AlertRepository
import com.aircall.alertnotification.persistence.MonitoredServiceRepository
import com.aircall.alertnotification.service.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [AlertNotificationApplication::class])
class AlertNotificationApplicationTests {

    @Autowired
    protected lateinit var alertAdapter: AlertAdapter

    @Autowired
    protected lateinit var escalationPolicyService: EscalationPolicyService

    @Autowired
    protected lateinit var pagerService: PagerService

    @Autowired
    protected lateinit var timerService: TimerService

    @Autowired
    protected lateinit var alertService: AlertService

    @Autowired
    protected lateinit var mailService: TargetService

    @Autowired
    protected lateinit var smsService: TargetService

    @Autowired
    protected lateinit var alertRepository: AlertRepository

    @Autowired
    protected lateinit var monitoredServiceRepository: MonitoredServiceRepository

    @Autowired
    protected lateinit var mailAdapter: TargetAdapter

    @Autowired
    protected lateinit var smsAdapter: TargetAdapter

    @Test
    fun contextLoads() {
        assertAll("Context",
                { Assertions.assertThat(alertAdapter).isNotNull},
                { Assertions.assertThat(escalationPolicyService).isNotNull},
                { Assertions.assertThat(pagerService).isNotNull},
                { Assertions.assertThat(timerService).isNotNull},
                { Assertions.assertThat(alertService).isNotNull},
                { Assertions.assertThat(mailService).isNotNull},
                { Assertions.assertThat(smsService).isNotNull},
                { Assertions.assertThat(alertRepository).isNotNull},
                { Assertions.assertThat(monitoredServiceRepository).isNotNull},
                { Assertions.assertThat(mailAdapter).isNotNull},
                { Assertions.assertThat(smsAdapter).isNotNull}
        )
    }

}
