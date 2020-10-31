package com.aircall.alertnnotification

import com.aircall.alertnotification.async.Producer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.jvm.Throws


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [AlertNotificationApplicationTests::class])
class AlertNotificationApplicationTests {


    @Autowired
    private lateinit var producer: Producer

    //@Test
    fun contextLoads() {
    }


    @Test
    @Throws(InterruptedException::class)
    fun createEvent() {
        producer.create("foo")

        producer.asyncMethod();

        // A chance to see the logging messages before the JVM exists.
        Thread.sleep(2000)
    }

}
