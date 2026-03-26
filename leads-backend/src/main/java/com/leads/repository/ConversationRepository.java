package com.leads.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leads.model.Channel;
import com.leads.model.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByLeadIdAndChannel(Long leadId, Channel channel);

    // Todas las conversaciones de un tenant, ordenadas por último mensaje
    Page<Conversation> findByTenantIdAndArchivadaFalseOrderByLastMessageAtDesc(Long tenantId, Pageable pageable);

    // Filtrar por canal
    Page<Conversation> findByTenantIdAndChannelAndArchivadaFalseOrderByLastMessageAtDesc(
            Long tenantId, Channel channel, Pageable pageable);

    // Total de no leídos por tenant
    @Query("SELECT SUM(c.unreadCount) FROM Conversation c WHERE c.tenant.id = :tenantId AND c.archivada = false")
    Long totalUnreadByTenant(Long tenantId);
}
