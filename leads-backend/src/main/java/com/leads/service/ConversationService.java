package com.leads.service;

import com.leads.dto.ConversationDTO;
import com.leads.dto.MessageDTO;
import com.leads.model.Channel;
import com.leads.repository.ConversationRepository;
import com.leads.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository convRepo;
    private final MessageRepository      msgRepo;
    private final LeadService            leadService;

    public Page<ConversationDTO> getConversations(Long tenantId, Channel channel, int page, int size) {
        var pageable = PageRequest.of(page, size);
        if (channel != null) {
            return convRepo
                .findByTenantIdAndChannelAndArchivadaFalseOrderByLastMessageAtDesc(tenantId, channel, pageable)
                .map(ConversationDTO::from);
        }
        return convRepo
            .findByTenantIdAndArchivadaFalseOrderByLastMessageAtDesc(tenantId, pageable)
            .map(ConversationDTO::from);
    }

    public Page<MessageDTO> getMessages(Long conversationId, int page, int size) {
        return msgRepo
            .findByConversationIdOrderByCreatedAtAsc(conversationId, PageRequest.of(page, size))
            .map(MessageDTO::from);
    }

    @Transactional
    public void markAsRead(Long conversationId) {
        leadService.markConversationAsRead(conversationId);
    }

    public Long totalUnread(Long tenantId) {
        Long total = convRepo.totalUnreadByTenant(tenantId);
        return total != null ? total : 0L;
    }

    @Transactional
    public MessageDTO sendMessage(Long conversationId, String contenido) {
        return leadService.sendOutgoing(conversationId, contenido);
    }
}
