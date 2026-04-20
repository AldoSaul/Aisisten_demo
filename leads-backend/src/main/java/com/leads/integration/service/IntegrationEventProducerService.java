package com.leads.integration.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationEventProducerService {

    private final Optional<KafkaTemplate<Object, Object>> kafkaTemplate;

    @Value("${kafka.topics.integrations-webhook-normalized:integrations.webhook.normalized}")
    private String integrationWebhookNormalizedTopic;

    public void publishNormalizedWebhook(String key, String payloadJson) {
        if (kafkaTemplate.isEmpty()) {
            log.warn("Kafka template unavailable, skipping normalized webhook publish");
            return;
        }
        String safeKey = key == null || key.isBlank() ? "integration-event" : key;
        kafkaTemplate.get().send(integrationWebhookNormalizedTopic, safeKey, payloadJson);
        log.debug("Published normalized integration webhook to topic={}", integrationWebhookNormalizedTopic);
    }
}

