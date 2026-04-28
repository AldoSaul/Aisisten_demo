package com.leads.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.leads.dto.IncomingMessageEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Legacy Kafka producer for the old IncomingMessageEvent path.
 *
 * @deprecated This producer is coupled to the legacy /webhook POST route and the
 *     MessageRouterService flow. It will be removed once the provider-normalized
 *     webhook path (Phase 2) fully replaces it. Use IntegrationEventProducerService
 *     for new normalized event publishing.
 */
@Deprecated(since = "Phase 1", forRemoval = true)
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.messaging.kafka-enabled", havingValue = "true")
public class KafkaProducerService {

    private final KafkaTemplate<String, IncomingMessageEvent> kafkaTemplate;

    @Value("${kafka.topics.messages-incoming}")
    private String topicIncoming;

    /**
     * Publica un mensaje entrante en Kafka.
     * La key es el senderId para que mensajes del mismo lead
     * siempre vayan a la misma partición (orden garantizado).
     */
    public void publishIncomingMessage(IncomingMessageEvent event) {
        kafkaTemplate.send(topicIncoming, event.getSenderId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Error publicando mensaje en Kafka: {}", ex.getMessage());
                } else {
                    log.debug("Mensaje publicado → topic={} partition={} offset={}",
                        topicIncoming,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }
            });
    }
}
