package com.leads.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.leads.dto.IncomingMessageEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Legacy Kafka consumer for the old IncomingMessageEvent path.
 *
 * @deprecated Coupled to the legacy /webhook POST route. Will be removed in Phase 2
 *     when inbound event processing moves to the provider-normalized consumer.
 */
@Deprecated(since = "Phase 1", forRemoval = true)
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.messaging.kafka-enabled", havingValue = "true")
public class KafkaConsumerService {

    private final LeadService leadService;

    @KafkaListener(
        topics     = "${kafka.topics.messages-incoming}",
        groupId    = "${spring.kafka.consumer.group-id}",
        concurrency = "3"
    )
    public void consumeIncomingMessage(
            @Payload IncomingMessageEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Consumiendo mensaje Kafka — canal={} senderId={} partition={} offset={}",
            event.getChannel(), event.getSenderId(), partition, offset);
        try {
            leadService.processIncoming(event);
        } catch (Exception ex) {
            log.error("Error procesando mensaje de Kafka: {}", ex.getMessage(), ex);
        }
    }
}
