package com.leads.dto;

import com.leads.model.*;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private String externalMessageId;
    private String contenido;
    private String mediaUrl;
    private String mediaType;
    private Channel channel;
    private MessageStatus status;
    private boolean esEntrante;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;

    public static MessageDTO from(Message m) {
        return MessageDTO.builder()
            .id(m.getId())
            .conversationId(m.getConversation().getId())
            .externalMessageId(m.getExternalMessageId())
            .contenido(m.getContenido())
            .mediaUrl(m.getMediaUrl())
            .mediaType(m.getMediaType())
            .channel(m.getChannel())
            .status(m.getStatus())
            .esEntrante(m.isEsEntrante())
            .sentAt(m.getSentAt())
            .createdAt(m.getCreatedAt())
            .build();
    }
}
