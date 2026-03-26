package com.leads.dto;

import com.leads.model.Channel;
import lombok.*;
import java.time.LocalDateTime;

// ── Evento que viaja por Kafka ─────────────────────────────────────────────
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class IncomingMessageEvent {
    private String externalMessageId;
    private String senderId;
    private String senderName;
    private String senderProfilePic;
    private Channel channel;
    private String contenido;
    private String mediaUrl;
    private String mediaType;
    private String pageId;            // para resolver el tenant
    private String phoneNumberId;     // solo WhatsApp
    private LocalDateTime sentAt;
}
