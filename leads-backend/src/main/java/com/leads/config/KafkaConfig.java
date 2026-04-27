package com.leads.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka topic definitions.
 *
 * Only active when {@code app.messaging.kafka-enabled=true}.
 * When Kafka is disabled, no topic beans are created and Kafka autoconfiguration
 * remains excluded via {@code spring.autoconfigure.exclude}.
 *
 * Phase 1 active topics:
 *   - integrations.webhook.normalized  (normalized inbound webhook events)
 *
 * Defined but currently unused (no consumer yet — reserved for future phases):
 *   - messages.incoming    (Phase 2+: legacy inbound message events)
 *   - messages.processed   (Phase 2+: processed message events)
 *   - leads.new            (Phase 2+: new lead events)
 */
@Configuration
@ConditionalOnProperty(name = "app.messaging.kafka-enabled", havingValue = "true")
public class KafkaConfig {

    @Value("${kafka.topics.messages-incoming}")
    private String messagesIncoming;

    @Value("${kafka.topics.messages-processed}")
    private String messagesProcessed;

    @Value("${kafka.topics.leads-new}")
    private String leadsNew;

    @Value("${kafka.topics.integrations-webhook-normalized}")
    private String integrationsWebhookNormalized;

    @Bean
    public NewTopic topicIntegrationsWebhookNormalized() {
        return TopicBuilder.name(integrationsWebhookNormalized)
            .partitions(3)
            .replicas(1)
            .build();
    }

    /**
     * Reserved for Phase 2+ when the legacy inbound message flow is migrated
     * to the provider-normalized path.
     */
    @Bean
    public NewTopic topicMessagesIncoming() {
        return TopicBuilder.name(messagesIncoming)
            .partitions(3)
            .replicas(1)
            .build();
    }

    /** Reserved for Phase 2+ message processing pipeline. */
    @Bean
    public NewTopic topicMessagesProcessed() {
        return TopicBuilder.name(messagesProcessed)
            .partitions(3)
            .replicas(1)
            .build();
    }

    /** Reserved for Phase 2+ lead creation pipeline. */
    @Bean
    public NewTopic topicLeadsNew() {
        return TopicBuilder.name(leadsNew)
            .partitions(1)
            .replicas(1)
            .build();
    }
}
