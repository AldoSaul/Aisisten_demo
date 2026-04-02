package com.leads.controller;

import com.leads.dto.ConversationDTO;
import com.leads.dto.MessageDTO;
import com.leads.model.Channel;
import com.leads.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService convService;

    /** Lista todas las conversaciones de un tenant (paginadas, opcionalmente filtradas por canal) */
    @GetMapping("/tenants/{tenantId}/conversations")
    public ResponseEntity<Page<ConversationDTO>> getConversations(
            @PathVariable Long tenantId,
            @RequestParam(required = false) Channel channel,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(convService.getConversations(tenantId, channel, page, size));
    }

    /** Mensajes de una conversación (paginados) */
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<Page<MessageDTO>> getMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "50") int size) {

        return ResponseEntity.ok(convService.getMessages(conversationId, page, size));
    }

    /** Marca como leída una conversación */
    @PatchMapping("/conversations/{conversationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long conversationId) {
        convService.markAsRead(conversationId);
        return ResponseEntity.ok().build();
    }

    /** Total de mensajes no leídos del tenant */
    @GetMapping("/tenants/{tenantId}/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount(@PathVariable Long tenantId) {
        return ResponseEntity.ok(Map.of("total", convService.totalUnread(tenantId)));
    }

    /** Envía un mensaje saliente (agente → lead) */
    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<MessageDTO> sendMessage(
            @PathVariable Long conversationId,
            @RequestBody SendMessageRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convService.sendMessage(conversationId, req.contenido()));
    }

    record SendMessageRequest(String contenido) {}
}
