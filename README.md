


spring-boot-admin-jandi-notifier
===============================
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Build Status](https://travis-ci.org/ersione/spring-boot-admin-jandi-notifier.svg?branch=master)](https://travis-ci.org/ersione/spring-boot-admin-jandi-notifier)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ersione/spring-boot-admin-jandi-notifier/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ersione/spring-boot-admin-jandi-notifier/)

<!--
[![Coverage Status](https://coveralls.io/repos/github/codecentric/spring-boot-admin/badge.svg?branch=master)](https://coveralls.io/github/codecentric/spring-boot-admin?branch=master)
[![Gitter](https://badges.gitter.im/codecentric/spring-boot-admin.svg)](https://gitter.im/codecentric/spring-boot-admin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
-->

jandi notifier for spring boot admin 2.x


```properties
# replace your webhook url
spring.boot.admin.notify.jandi.webhook-url=https://wh.jandi.com/connect-api/webhook/xxxx
spring.boot.admin.notify.jandi.body=**#{instance.registration.name}** (#{instance.id}) is changed status.
spring.boot.admin.notify.jandi.title=from #{lastStatus} to #{event.statusInfo.status}
spring.boot.admin.notify.jandi.description=Health URL : [#{instance.registration.healthUrl}](#{instance.registration.healthUrl})
```

## SpEL

### instance
[de.codecentric.boot.admin.server.domain.entities.Instance](https://github.com/codecentric/spring-boot-admin/blob/master/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/domain/entities/Instance.java)

### event
[de.codecentric.boot.admin.server.domain.events.InstanceEvent](https://github.com/codecentric/spring-boot-admin/blob/master/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/domain/events/InstanceEvent.java)

### lastStatus
String
