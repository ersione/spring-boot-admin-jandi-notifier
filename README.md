# spring-boot-admin-jandi-notifier
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
[de.codecentric.boot.admin.server.domain.events.InstanceEvent]https://github.com/codecentric/spring-boot-admin/blob/master/spring-boot-admin-server/src/main/java/de/codecentric/boot/admin/server/domain/events/InstanceEvent.java

### lastStatus
String
