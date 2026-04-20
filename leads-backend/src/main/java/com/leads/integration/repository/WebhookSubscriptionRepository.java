package com.leads.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.integration.model.ProviderType;
import com.leads.integration.model.WebhookSubscription;

public interface WebhookSubscriptionRepository extends JpaRepository<WebhookSubscription, Long> {

    List<WebhookSubscription> findByConnectionIdAndActiveTrue(Long connectionId);

    Optional<WebhookSubscription> findByConnectionIdAndExternalSubscriptionId(
        Long connectionId,
        String externalSubscriptionId
    );

    List<WebhookSubscription> findByProviderTypeAndActiveTrue(ProviderType providerType);
}

