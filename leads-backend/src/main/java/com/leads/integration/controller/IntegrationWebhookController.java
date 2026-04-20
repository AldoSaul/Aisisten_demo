package com.leads.integration.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@RestController
@RequestMapping("/api/v1/integrations/webhooks")
@RequiredArgsConstructor
public class IntegrationWebhookController {

    private static final String SIGNATURE_HEADER = "X-Signature";

    private final IntegrationWebhookService webhookService;

    @GetMapping("/{provider}")
    public ResponseEntity<String> verifyWebhook(
        @PathVariable String provider,
        @RequestParam Map<String, String> queryParams
    ) {
        String challenge = webhookService.resolveVerificationChallenge(provider, queryParams);
        if (challenge == null || challenge.isBlank()) {
            return ResponseEntity.badRequest().body("Missing verification challenge");
        }
        return ResponseEntity.ok(challenge);
    }

    @PostMapping("/{provider}")
    public ResponseEntity<IntegrationWebhookAckResponse> receiveWebhook(
        @PathVariable String provider,
        @RequestBody JsonNode payload,
        @RequestHeader(name = SIGNATURE_HEADER, required = false) String signature
    ) throws Exception {
        String rawPayload = payload.toString();
        IntegrationWebhookAckResponse response = webhookService.processWebhook(provider, signature, rawPayload, payload);
        return ResponseEntity.ok(response);
    }
}

