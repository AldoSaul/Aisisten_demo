package com.leads.integration.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Publishes normalized integration webhook events.
 *
 * Behavior:
 * - When {@code app.messaging.kafka-enabled=true}: publishes to Kafka topic
 *   {@code integrations.webhook.normalized}.
 * - When Kafka is disabled: falls back to synchronous in-process handling.
 *   In Phase 1 this logs the event and marks it as processed. In Phase 2+
 *   the synchronous path will route the event directly into the domain.
 *
 * No consumer for the Kafka topic exists yet. The consumer will be added in
 * Phase 2 when the Lead/Conversation/Message domain processing is wired up.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationEventProducerService {

    private final Optional<KafkaTemplate<Object, Object>> kafkaTemplate;

    @Value("${kafka.topics.integrations-webhook-normalized:integrations.webhook.normalized}")
    private String integrationWebhookNormalizedTopic;

    @Value("${app.messaging.kafka-enabled:false}")
    private boolean kafkaEnabled;

    /**
     * Publishes a normalized webhook event.
     * Falls back to synchronous processing if Kafka is disabled.
     *
     * @param key         Kafka partition key (typically the external event ID)
     * @param payloadJson Serialized {@link com.leads.integration.dto.NormalizedWebhookEvent}
     */
    public void publishNormalizedWebhook(String key, String payloadJson) {
        String safeKey = (key == null || key.isBlank()) ? "integration-event" : key;

        if (kafkaEnabled && kafkaTemplate.isPresent()) {
            kafkaTemplate.get().send(integrationWebhookNormalizedTopic, safeKey, payloadJson);
            log.debug("Published normalized webhook event to Kafka topic={} key={}", integrationWebhookNormalizedTopic, safeKey);
        } else {
            handleSynchronousFallback(safeKey, payloadJson);
        }
    }

    /**
     * Synchronous fallback for when Kafka is disabled.
     *
     * Phase 1: logs the normalized event and marks it as processed synchronously.
     * Phase 2: this method will be extended to directly invoke the domain processing
     * service (Lead/Conversation/Message creation) without Kafka.
     */
    private void handleSynchronousFallback(String key, String payloadJson) {
        log.info("Kafka disabled — processing normalized webhook event synchronously key={}", key);
        log.debug("Normalized webhook payload: {}", payloadJson);
        // Phase 2+: inject and call InboundMessageProcessorService.process(payloadJson) here
    }
}

