package com.leads.integration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Phase 1 — Task 1.6: Verify IntegrationEventProducerService uses Kafka when enabled
 * and falls back to synchronous processing when disabled.
 */
class IntegrationEventProducerServiceTest {

    private org.springframework.kafka.core.KafkaTemplate<Object, Object> kafkaTemplate;
    private IntegrationEventProducerService serviceWithKafka;
    private IntegrationEventProducerService serviceWithoutKafka;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        kafkaTemplate = mock(org.springframework.kafka.core.KafkaTemplate.class);

        serviceWithKafka = buildService(Optional.of(kafkaTemplate), true);
        serviceWithoutKafka = buildService(Optional.empty(), false);
    }

    @Test
    void publishNormalizedWebhook_whenKafkaEnabled_publishesToKafka() {
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
            .thenReturn(mock(java.util.concurrent.CompletableFuture.class));

        serviceWithKafka.publishNormalizedWebhook("event-key-1", "{\"provider\":\"meta\"}");

        verify(kafkaTemplate, times(1)).send(anyString(), eq("event-key-1"), anyString());
    }

    @Test
    void publishNormalizedWebhook_whenKafkaDisabled_doesNotCallKafka() {
        // No exception should be thrown; fallback logs synchronously
        serviceWithoutKafka.publishNormalizedWebhook("event-key-2", "{\"provider\":\"meta\"}");

        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void publishNormalizedWebhook_withNullKey_usesDefaultKey() {
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
            .thenReturn(mock(java.util.concurrent.CompletableFuture.class));

        serviceWithKafka.publishNormalizedWebhook(null, "{\"provider\":\"meta\"}");

        verify(kafkaTemplate, times(1)).send(anyString(), eq("integration-event"), anyString());
    }

    @SuppressWarnings("unchecked")
    private IntegrationEventProducerService buildService(
        Optional<org.springframework.kafka.core.KafkaTemplate<Object, Object>> template,
        boolean kafkaEnabled
    ) throws Exception {
        var service = new IntegrationEventProducerService(template);

        var topicField = IntegrationEventProducerService.class.getDeclaredField("integrationWebhookNormalizedTopic");
        topicField.setAccessible(true);
        topicField.set(service, "integrations.webhook.normalized");

        var enabledField = IntegrationEventProducerService.class.getDeclaredField("kafkaEnabled");
        enabledField.setAccessible(true);
        enabledField.set(service, kafkaEnabled);

        return service;
    }
}
