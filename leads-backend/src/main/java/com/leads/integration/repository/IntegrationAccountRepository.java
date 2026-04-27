package com.leads.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.integration.model.IntegrationAccount;
import com.leads.integration.model.ProviderType;

public interface IntegrationAccountRepository extends JpaRepository<IntegrationAccount, Long> {

    List<IntegrationAccount> findByTenantIdOrderByUpdatedAtDesc(Long tenantId);

    List<IntegrationAccount> findByConnectionIdOrderByUpdatedAtDesc(Long connectionId);

    Optional<IntegrationAccount> findByTenantIdAndProviderTypeAndExternalAccountId(
        Long tenantId,
        ProviderType providerType,
        String externalAccountId
    );
}

