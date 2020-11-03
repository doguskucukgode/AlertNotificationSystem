# AlertNotificationSystem
Implementation of Domain logic an in-house alert notification system. Project is written in Kotlin lannguage with Spring Boot framework. This is only domain logic with creating timers in terms of alerts. After adapters are implemented (it can be TCP/IP, REST, GRPC...) project can be run with Spring Boot application. Use cases and unit tests can be run with gradle. 

## Running with gradle
`gradle test --info`

## Circle CI
Project is configured with Circle CI from [this link](https://app.circleci.com/pipelines/github/doguskucukgode/AlertNotificationSystem?branch=master)

## Use Case Scenarios
```
Given a Monitored Service in a Healthy State,
when the Pager receives an Alert related to this Monitored Service,
then the Monitored Service becomes Unhealthy,
the Pager notifies all targets of the first level of the escalation policy,
and sets a 15-minutes acknowledgement delay
```

```
Given a Monitored Service in an Unhealthy State,
the corresponding Alert is not Acknowledged
and the last level has not been notified,
when the Pager receives the Acknowledgement Timeout,
then the Pager notifies all targets of the next level of the escalation policy
and sets a 15-minutes acknowledgement delay.
```

```
Given a Monitored Service in an Unhealthy State
when the Pager receives the Acknowledgement
and later receives the Acknowledgement Timeout,
then the Pager doesn't notify any Target
and doesn't set an acknowledgement delay.
```

```
Given a Monitored Service in an Unhealthy State,
when the Pager receives an Alert related to this Monitored Service,
then the Pager doesn’t notify any Target
and doesn’t set an acknowledgement delay
```

```
Given a Monitored Service in an Unhealthy State,
when the Pager receives a Healthy event related to this Monitored Service
and later receives the Acknowledgement Timeout,
then the Monitored Service becomes Healthy,
the Pager doesn’t notify any Target
and doesn’t set an acknowledgement delay
```
