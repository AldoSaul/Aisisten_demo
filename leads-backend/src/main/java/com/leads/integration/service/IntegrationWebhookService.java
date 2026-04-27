package com.leads.integration.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leads.integration.dto.IntegrationWebhookAckResponse;
import com.leads.integration.dto.NormalizedWebhookEvent;
import com.leads.integration.model.IntegrationConnection;
import com.leads.integration.model.ProviderType;
import com.leads.integration.model.WebhookEventLog;
import com.leads.integration.model.WebhookProcessingStatus;
import com.leads.integration.provider.IntegrationProviderHandler;
import com.leads.integration.provider.IntegrationProviderRegistry;
import com.leads.integration.provider.NormalizedProviderWebhookEvent;
import com.leads.integration.repository.IntegrationConnectionRepository;
import com.leads.integration.repository.WebhookEventLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationWebhookService {

    private final IntegrationProviderRegistry providerRegistry;
    private final WebhookEventLogRepository webhookEventLogRepository;
    private final IntegrationConnectionRepository connectionRepository;
    private final IntegrationEventProducerService integrationEventProducerService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(readOnly = true)
    public String resolveVerificationChallenge(String providerKey, Map<String, String> queryParams) {
        ProviderType providerType = ProviderType.fromKey(providerKey);
        IntegrationProviderHandler provider = providerRegistry.requireByType(providerType);
        return provider.resolveVerificationChallenge(queryParams);
    }

    @Transactional
    public IntegrationWebhookAckResponse processWebhook(
        String providerKey,
        String signatureHeader,
        String rawPayload,
        JsonNode payload
    ) {
        ProviderType providerType = ProviderType.fromKey(providerKey);
        IntegrationProviderHandler provider = providerRegistry.requireByType(providerType);

        WebhookEventLog eventLog = WebhookEventLog.builder()
            .providerType(providerType)
            .requestSignature(signatureHeader)
            .rawPayload(rawPayload)
            .status(WebhookProcessingStatus.RECEIVED)
            .receivedAt(LocalDateTime.now())
            .build();
        eventLog = webhookEventLogRepository.save(eventLog);

        try {
            boolean valid = provider.validateWebhookSignature(signatureHeader, rawPayload);
            if (!valid) {
                eventLog.setStatus(WebhookProcessingStatus.INVALID_SIGNATURE);
                eventLog.setProcessingError("Invalid webhook signature");
                webhookEventLogRepository.save(eventLog);
                return new IntegrationWebhookAckResponse("INVALID_SIGNATURE", providerType.key(), eventLog.getId());
            }

            NormalizedProviderWebhookEvent normalized = provider.normalizeWebhook(payload);
            eventLog.setExternalEventId(normalized.externalEventId());
            eventLog.setNormalizedPayload(objectMapper.writeValueAsString(normalized.normalizedPayload()));
            eventLog.setStatus(WebhookProcessingStatus.NORMALIZED);

            if (normalized.tenantLookupKey() != null && !normalized.tenantLookupKey().isBlank()) {
                resolveConnection(providerType, normalized.tenantLookupKey()).ifPresent(eventLog::setConnection);
            }

            NormalizedWebhookEvent internalEvent = new NormalizedWebhookEvent(
                providerType.key(),
                normalized.externalEventId(),
                normalized.eventType(),
                normalized.tenantLookupKey(),
                objectMapper.writeValueAsString(normalized.normalizedPayload()),
                LocalDateTime.now()
            );
            String normalizedJson = objectMapper.writeValueAsString(internalEvent);
            String kafkaKey = normalized.externalEventId() != null ? normalized.externalEventId() : providerType.key();
            integrationEventProducerService.publishNormalizedWebhook(kafkaKey, normalizedJson);

            eventLog.setStatus(WebhookProcessingStatus.PUBLISHED);
            eventLog.setProcessedAt(LocalDateTime.now());
            webhookEventLogRepository.save(eventLog);
            return new IntegrationWebhookAckResponse("EVENT_RECEIVED", providerType.key(), eventLog.getId());
        } catch (Exception ex) {
            log.error("Error processing integration webhook provider={}", providerType.key(), ex);
            eventLog.setStatus(WebhookProcessingStatus.FAILED);
            eventLog.setProcessingError(ex.getMessage());
            eventLog.setProcessedAt(LocalDateTime.now());
            webhookEventLogRepository.save(eventLog);
            return new IntegrationWebhookAckResponse("EVENT_FAILED", providerType.key(), eventLog.getId());
        }
    }

    private java.util.Optional<IntegrationConnection> resolveConnection(ProviderType providerType, String tenantLookupKey) {
        try {
            Long tenantId = Long.valueOf(tenantLookupKey);
            return connectionRepository.findFirstByTenantIdAndProviderTypeOrderByUpdatedAtDesc(tenantId, providerType);
        } catch (NumberFormatException ignored) {
            return java.util.Optional.empty();
        }
    }
}

