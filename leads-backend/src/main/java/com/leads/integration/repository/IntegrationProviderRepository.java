package com.leads.integration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.integration.model.IntegrationProvider;
import com.leads.integration.model.ProviderType;

public interface IntegrationProviderRepository extends JpaRepository<IntegrationProvider, Long> {

    Optional<IntegrationProvider> findByProviderType(ProviderType providerType);
}

