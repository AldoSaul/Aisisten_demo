package com.leads.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.integration.model.AssetType;
import com.leads.integration.model.IntegrationAsset;

public interface IntegrationAssetRepository extends JpaRepository<IntegrationAsset, Long> {

    List<IntegrationAsset> findByTenantIdOrderByUpdatedAtDesc(Long tenantId);

    List<IntegrationAsset> findByConnectionIdOrderByUpdatedAtDesc(Long connectionId);

    Optional<IntegrationAsset> findByAccountIdAndAssetTypeAndExternalAssetId(
        Long accountId,
        AssetType assetType,
        String externalAssetId
    );
}

