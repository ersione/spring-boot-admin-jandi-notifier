package org.ersione.springframework.boot.server.admin.notifier;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JandiNotifier extends AbstractStatusChangeNotifier {

    private static final String DEFAULT_MESSAGE = "**#{instance.registration.name}** (#{instance.id}) is **#{event.statusInfo.status}**";

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Webhook url for Jandi API (i.e. https://wh.jandi.com/connect-api/webhook/xxxx/xxxx)
     */
    private URI webhookUrl;

    private Expression body;

    private Expression title;

    private Expression description;

    public JandiNotifier(InstanceRepository repository) {
        super(repository);
        this.body = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        if (webhookUrl == null) {
            return Mono.error(new IllegalStateException("'webhookUrl' must not be null."));
        }
        return Mono.fromRunnable(
                () -> restTemplate.postForEntity(webhookUrl, createMessage(event, instance), Void.class));
    }

    private HttpEntity<Map<String, Object>> createMessage(InstanceEvent event, Instance instance) {
        Map<String, Object> messageJson = new HashMap<>();
        List<Map> connectInfoList = new ArrayList<>();
        Map<String, Object> connectInfo = new HashMap<>();
        messageJson.put("body", getText(body, event, instance));

        messageJson.put("connectColor", getColor(event));

        if (title != null) {
            connectInfo.put("title", getText(title, event, instance));
            if (description != null) {
                connectInfo.put("description", getText(description, event, instance));
            }
        }
        if (!connectInfo.isEmpty()) {
            connectInfoList.add(connectInfo);
            messageJson.put("connectInfo", connectInfoList);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/vnd.tosslab.jandi-v2+json");
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(messageJson, headers);
    }

    private String getText(Expression message, InstanceEvent event, Instance instance) {
        Map<String, Object> root = new HashMap<>();
        root.put("event", event);
        root.put("instance", instance);
        root.put("lastStatus", getLastStatus(event.getInstance()));
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        return message.getValue(context, String.class);
    }

    private String getColor(InstanceEvent event) {
        if (event instanceof InstanceStatusChangedEvent) {
            return StatusInfo.STATUS_UP.equals(((InstanceStatusChangedEvent) event).getStatusInfo().getStatus()) ? "#2EA44F" : "#D50000";
        } else {
            return "#439FE0";
        }
    }

    public URI getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(URI webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public Expression getBody() {
        return body;
    }

    public void setBody(Expression body) {
        this.body = body;
    }

    public Expression getTitle() {
        return title;
    }

    public void setTitle(Expression title) {
        this.title = title;
    }

    public Expression getDescription() {
        return description;
    }

    public void setDescription(Expression description) {
        this.description = description;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
