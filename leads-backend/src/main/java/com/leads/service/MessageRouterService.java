package com.leads.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.leads.dto.IncomingMessageEvent;
import com.leads.model.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageRouterService {

    public List<IncomingMessageEvent> route(JsonNode payload) {
        List<IncomingMessageEvent> events = new ArrayList<>();
        String object = payload.path("object").asText();
        switch (object) {
            case "whatsapp_business_account" -> extractWhatsApp(payload, events);
            case "instagram"                 -> extractInstagram(payload, events);
            case "page"                      -> extractFacebook(payload, events);
            default -> log.warn("Tipo de objeto desconocido: {}", object);
        }
        return events;
    }

    private void extractWhatsApp(JsonNode payload, List<IncomingMessageEvent> events) {
        for (JsonNode entry : payload.path("entry")) {
            for (JsonNode change : entry.path("changes")) {
                JsonNode value = change.path("value");
                String phoneNumberId = value.path("metadata").path("phone_number_id").asText();
                for (JsonNode msg : value.path("messages")) {
                    String type = msg.path("type").asText();
                    String nombre = value.path("contacts").path(0).path("profile").path("name").asText(null);
                    events.add(IncomingMessageEvent.builder()
                        .externalMessageId(msg.path("id").asText())
                        .senderId(msg.path("from").asText())
                        .senderName(nombre)
                        .channel(Channel.WHATSAPP)
                        .contenido("text".equals(type) ? msg.path("text").path("body").asText(null) : null)
                        .mediaUrl(msg.path(type).path("link").asText(null))
                        .mediaType(type)
                        .phoneNumberId(phoneNumberId)
                        .sentAt(epochToLocal(msg.path("timestamp").asLong()))
                        .build());
                }
            }
        }
    }

    private void extractInstagram(JsonNode payload, List<IncomingMessageEvent> events) {
        for (JsonNode entry : payload.path("entry")) {
            String pageId = entry.path("id").asText();
            for (JsonNode messaging : entry.path("messaging")) {
                JsonNode msg = messaging.path("message");
                if (msg.isMissingNode()) continue;
                events.add(IncomingMessageEvent.builder()
                    .externalMessageId(msg.path("mid").asText())
                    .senderId(messaging.path("sender").path("id").asText())
                    .channel(Channel.INSTAGRAM)
                    .contenido(msg.path("text").asText(null))
                    .mediaUrl(attachUrl(msg))
                    .mediaType(attachType(msg))
                    .pageId(pageId)
                    .sentAt(epochMillisToLocal(messaging.path("timestamp").asLong()))
                    .build());
            }
        }
    }

    private void extractFacebook(JsonNode payload, List<IncomingMessageEvent> events) {
        for (JsonNode entry : payload.path("entry")) {
            String pageId = entry.path("id").asText();
            for (JsonNode messaging : entry.path("messaging")) {
                JsonNode msg = messaging.path("message");
                if (msg.isMissingNode()) continue;
                events.add(IncomingMessageEvent.builder()
                    .externalMessageId(msg.path("mid").asText())
                    .senderId(messaging.path("sender").path("id").asText())
                    .channel(Channel.FACEBOOK)
                    .contenido(msg.path("text").asText(null))
                    .mediaUrl(attachUrl(msg))
                    .mediaType(attachType(msg))
                    .pageId(pageId)
                    .sentAt(epochMillisToLocal(messaging.path("timestamp").asLong()))
                    .build());
            }
        }
    }

    private String attachUrl(JsonNode msg) {
        JsonNode a = msg.path("attachments");
        return a.isEmpty() ? null : a.path(0).path("payload").path("url").asText(null);
    }

    private String attachType(JsonNode msg) {
        JsonNode a = msg.path("attachments");
        return a.isEmpty() ? null : a.path(0).path("type").asText(null);
    }

    private LocalDateTime epochToLocal(long sec) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(sec), ZoneId.of("America/Mexico_City"));
    }

    private LocalDateTime epochMillisToLocal(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.of("America/Mexico_City"));
    }
}
