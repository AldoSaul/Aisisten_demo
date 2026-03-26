package com.leads.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.leads.dto.IncomingMessageEvent;
import com.leads.service.KafkaProducerService;
import com.leads.service.MessageRouterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    @Value("${meta.verify-token}")
    private String verifyToken;

    private final MessageRouterService router;
    private final Optional<KafkaProducerService> kafka;

    /** Meta verifica que la URL es tuya con este GET */
    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam("hub.mode")         String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge")    String challenge) {

        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            log.info("Webhook verificado correctamente");
            return ResponseEntity.ok(challenge);
        }
        log.warn("Verificación de webhook fallida — token inválido");
        return ResponseEntity.status(403).build();
    }

    /** Meta envía los eventos aquí — se parsean y publican a Kafka inmediatamente */
    @PostMapping
    public ResponseEntity<String> receive(@RequestBody JsonNode payload) {
        //log.debug("Webhook recibido: {}", payload.path("object").asText());
        try {
            List<IncomingMessageEvent> events = router.route(payload);
            if (kafka.isPresent()) {
                events.forEach(kafka.get()::publishIncomingMessage);
                log.info("{} evento(s) publicados en Kafka", events.size());
            } else {
                log.warn("Kafka no está habilitado. {} evento(s) se descartaron.", events.size());
            }
        } catch (Exception ex) {
            log.error("Error procesando webhook: {}", ex.getMessage(), ex);
        }
        // Meta requiere 200 rápido — el procesamiento real es asíncrono via Kafka
        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}
