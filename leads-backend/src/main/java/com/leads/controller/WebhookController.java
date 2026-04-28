package com.leads.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.leads.integration.dto.IntegrationWebhookAckResponse;
import com.leads.integration.service.IntegrationWebhookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Legacy webhook compatibility controller.
 *
 * @deprecated This controller exists only for backward compatibility with webhook URLs
 *     registered before Phase 1. The canonical webhook route is:
 *     {@code GET/POST /api/v1/integrations/webhooks/{provider}}
 *     handled by {@link com.leads.integration.controller.IntegrationWebhookController}.
 *
 *     Do not add new functionality here. All logic is delegated to
 *     {@link IntegrationWebhookService} using "meta" as the default provider key.
 *     This controller will be removed once all registered Meta webhook URLs are
 *     updated to the canonical route.
 */
@Deprecated(since = "Phase 1", forRemoval = true)
@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private static final String DEFAULT_PROVIDER = "meta";
    private static final String SIGNATURE_HEADER = "X-Hub-Signature-256";

    private final IntegrationWebhookService integrationWebhookService;

    /**
     * Legacy Meta webhook challenge verification.
     * Delegates to the provider-based service using the "meta" provider key.
     *
     * @deprecated Use GET /api/v1/integrations/webhooks/meta instead.
     */
    @Deprecated(since = "Phase 1", forRemoval = true)
    @GetMapping
    public ResponseEntity<String> verifyMeta(@RequestParam Map<String, String> queryParams) {
        log.warn("Legacy webhook GET /webhook called — prefer /api/v1/integrations/webhooks/meta");
        String challenge = integrationWebhookService.resolveVerificationChallenge(DEFAULT_PROVIDER, queryParams);
        if (challenge == null || challenge.isBlank()) {
            return ResponseEntity.status(403).body("Verification failed");
        }
        return ResponseEntity.ok(challenge);
    }

    /**
     * Legacy Meta inbound event receiver.
     * Delegates to the provider-based service using the "meta" provider key.
     *
     * @deprecated Use POST /api/v1/integrations/webhooks/meta instead.
     */
    @Deprecated(since = "Phase 1", forRemoval = true)
    @PostMapping
    public ResponseEntity<String> receiveMeta(
        @RequestBody JsonNode payload,
        @RequestHeader(name = SIGNATURE_HEADER, required = false) String signature
    ) throws Exception {
        log.warn("Legacy webhook POST /webhook called — prefer /api/v1/integrations/webhooks/meta");
        String rawPayload = payload.toString();
        IntegrationWebhookAckResponse ack = integrationWebhookService.processWebhook(
            DEFAULT_PROVIDER, signature, rawPayload, payload);
        return ResponseEntity.ok(ack.status());
    }
}

