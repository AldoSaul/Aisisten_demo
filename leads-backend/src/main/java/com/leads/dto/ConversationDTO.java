package com.leads.dto;

import com.leads.model.*;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ConversationDTO {
    private Long id;
    private Channel channel;
    private String leadNombre;
    private String leadSenderId;
    private String leadProfilePic;
    private String lastMessage;
    private int unreadCount;
    private LocalDateTime lastMessageAt;

    public static ConversationDTO from(Conversation c) {
        Lead lead = c.getLead();
        String lastMsg = "";
        if (c.getMensajes() != null && !c.getMensajes().isEmpty()) {
            lastMsg = c.getMensajes().getLast().getContenido();
            if (lastMsg == null) lastMsg = "[media]";
        }
        return ConversationDTO.builder()
            .id(c.getId())
            .channel(c.getChannel())
            .leadNombre(lead.getNombre() != null ? lead.getNombre() : lead.getSenderId())
            .leadSenderId(lead.getSenderId())
            .leadProfilePic(lead.getProfilePicUrl())
            .lastMessage(lastMsg)
            .unreadCount(c.getUnreadCount())
            .lastMessageAt(c.getLastMessageAt())
            .build();
    }
}
