package com.leads.integration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.integration.model.ProviderType;
import com.leads.integration.model.WebhookEventLog;

public interface WebhookEventLogRepository extends JpaRepository<WebhookEventLog, Long> {

    Optional<WebhookEventLog> findByProviderTypeAndExternalEventId(ProviderType providerType, String externalEventId);
}

