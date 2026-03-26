package com.leads.repository;

import com.leads.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId, Pageable pageable);
    Optional<Message> findByExternalMessageId(String externalMessageId);
    boolean existsByExternalMessageId(String externalMessageId);
}
