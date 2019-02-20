package org.ersione.springframework.boot.server.admin.notifier;

import de.codecentric.boot.admin.server.config.AdminServerNotifierAutoConfiguration;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore({AdminServerNotifierAutoConfiguration.NotifierTriggerConfiguration.class, AdminServerNotifierAutoConfiguration.CompositeNotifierConfiguration.class})
@ConditionalOnProperty(prefix = "spring.boot.admin.notify.jandi", name = "webhook-url")
public class JandiNotificationConfiguration {

    @Bean
    @ConfigurationProperties("spring.boot.admin.notify.jandi")
    public JandiNotifier jandiNotifier(InstanceRepository repository) {
        return new JandiNotifier(repository);
    }
}
