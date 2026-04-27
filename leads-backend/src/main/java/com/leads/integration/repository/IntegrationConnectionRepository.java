package com.leads.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.integration.model.IntegrationConnection;
import com.leads.integration.model.ProviderType;

public interface IntegrationConnectionRepository extends JpaRepository<IntegrationConnection, Long> {

    List<IntegrationConnection> findByTenantIdOrderByUpdatedAtDesc(Long tenantId);

    Optional<IntegrationConnection> findByIdAndTenantId(Long id, Long tenantId);

    Optional<IntegrationConnection> findByPublicId(String publicId);

    Optional<IntegrationConnection> findByTenantIdAndProviderTypeAndExternalConnectionId(
        Long tenantId,
        ProviderType providerType,
        String externalConnectionId
    );

    Optional<IntegrationConnection> findFirstByTenantIdAndProviderTypeOrderByUpdatedAtDesc(
        Long tenantId,
        ProviderType providerType
    );

    Optional<IntegrationConnection> findByAuthState(String authState);
}

