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
 *   - integrations.webhook.normalized
 *     Partition count (3): allows parallel processing by up to 3 consumer instances per group.
 *     Replica count   (1): local/dev default — increase to 2+ for staging and production.
 *     Both values are development defaults and can be made configurable via
 *     {@code kafka.topics.integrations-webhook-normalized.*} properties when needed.
 */
@Configuration
@ConditionalOnProperty(name = "app.messaging.kafka-enabled", havingValue = "true")
public class KafkaConfig {

    @Value("${kafka.topics.integrations-webhook-normalized}")
    private String integrationsWebhookNormalized;

    @Bean
    public NewTopic topicIntegrationsWebhookNormalized() {
        return TopicBuilder.name(integrationsWebhookNormalized)
            .partitions(3)
            .replicas(1)
            .build();
    }
}
