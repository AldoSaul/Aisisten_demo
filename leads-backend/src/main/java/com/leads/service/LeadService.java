package com.leads.service;

import com.leads.dto.IncomingMessageEvent;
import com.leads.dto.MessageDTO;
import com.leads.model.*;
import com.leads.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository       leadRepo;
    private final ConversationRepository convRepo;
    private final MessageRepository    msgRepo;
    private final TenantRepository     tenantRepo;
    private final SimpMessagingTemplate ws;

    /**
     * Procesa un IncomingMessageEvent que llega desde Kafka.
     * 1. Resuelve el Tenant por pageId o phoneNumberId
     * 2. Busca o crea el Lead
     * 3. Busca o crea la Conversation
     * 4. Guarda el Message
     * 5. Notifica por WebSocket al frontend
     */
    @Transactional
    public void processIncoming(IncomingMessageEvent event) {

        // 1. Resolver Tenant
        Tenant tenant = resolveTenant(event);
        if (tenant == null) {
            log.warn("Tenant no encontrado para pageId={} phoneId={}",
                event.getPageId(), event.getPhoneNumberId());
            return;
        }

        // 2. Deduplicar por externalMessageId
        if (event.getExternalMessageId() != null &&
            msgRepo.existsByExternalMessageId(event.getExternalMessageId())) {
            log.debug("Mensaje duplicado ignorado: {}", event.getExternalMessageId());
            return;
        }

        // 3. Buscar o crear Lead
        Lead lead = leadRepo
            .findBySenderIdAndChannelAndTenantId(event.getSenderId(), event.getChannel(), tenant.getId())
            .orElseGet(() -> leadRepo.save(Lead.builder()
                .senderId(event.getSenderId())
                .nombre(event.getSenderName())
                .profilePicUrl(event.getSenderProfilePic())
                .channel(event.getChannel())
                .tenant(tenant)
                .build()));

        // 4. Buscar o crear Conversation
        Conversation conv = convRepo
            .findByLeadIdAndChannel(lead.getId(), event.getChannel())
            .orElseGet(() -> convRepo.save(Conversation.builder()
                .lead(lead)
                .tenant(tenant)
                .channel(event.getChannel())
                .lastMessageAt(LocalDateTime.now())
                .build()));

        // 5. Guardar Message
        Message msg = msgRepo.save(Message.builder()
            .externalMessageId(event.getExternalMessageId())
            .conversation(conv)
            .channel(event.getChannel())
            .contenido(event.getContenido())
            .mediaUrl(event.getMediaUrl())
            .mediaType(event.getMediaType())
            .esEntrante(true)
            .status(MessageStatus.PROCESSED)
            .sentAt(event.getSentAt() != null ? event.getSentAt() : LocalDateTime.now())
            .build());

        // 6. Actualizar contadores en Conversation
        conv.setLastMessageAt(msg.getCreatedAt());
        conv.setUnreadCount(conv.getUnreadCount() + 1);
        convRepo.save(conv);

        // 7. Push por WebSocket → /topic/tenant/{tenantId}/messages
        String destination = "/topic/tenant/" + tenant.getId() + "/messages";
        ws.convertAndSend(destination, MessageDTO.from(msg));
        log.info("Mensaje procesado y enviado por WS → {}", destination);
    }

    @Transactional
    public void markConversationAsRead(Long conversationId) {
        convRepo.findById(conversationId).ifPresent(conv -> {
            conv.setUnreadCount(0);
            convRepo.save(conv);
        });
    }

    private Tenant resolveTenant(IncomingMessageEvent event) {
        if (event.getPhoneNumberId() != null) {
            return tenantRepo.findByWhatsappPhoneNumberId(event.getPhoneNumberId()).orElse(null);
        }
        if (event.getPageId() != null) {
            Tenant t = tenantRepo.findByFacebookPageId(event.getPageId()).orElse(null);
            if (t == null) t = tenantRepo.findByInstagramAccountId(event.getPageId()).orElse(null);
            return t;
        }
        return null;
    }
}
