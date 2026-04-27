package com.leads.integration.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leads.integration.dto.IntegrationAssetDto;
import com.leads.integration.dto.IntegrationSyncResultDto;
import com.leads.integration.model.ProviderType;
import com.leads.integration.provider.AssetSyncResult;
import com.leads.integration.provider.IntegrationProviderRegistry;
import com.leads.integration.repository.IntegrationAssetRepository;
import com.leads.integration.repository.IntegrationConnectionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IntegrationAssetService {

    private final IntegrationAssetRepository assetRepository;
    private final IntegrationConnectionRepository connectionRepository;
    private final IntegrationProviderRegistry providerRegistry;
    private final IntegrationDtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public List<IntegrationAssetDto> listAssets(Long tenantId) {
        return assetRepository.findByTenantIdOrderByUpdatedAtDesc(tenantId).stream()
            .map(dtoMapper::toAssetDto)
            .toList();
    }

    @Transactional
    public IntegrationSyncResultDto syncAssets(String providerKey, Long tenantId) {
        ProviderType providerType = ProviderType.fromKey(providerKey);
        AssetSyncResult result = providerRegistry.requireByType(providerType).syncAssets(tenantId);

        connectionRepository.findFirstByTenantIdAndProviderTypeOrderByUpdatedAtDesc(tenantId, providerType)
            .ifPresent(connection -> {
                connection.setLastSyncedAt(LocalDateTime.now());
                connectionRepository.save(connection);
            });

        return new IntegrationSyncResultDto(
            providerType.key(),
            result.discoveredAccounts(),
            result.discoveredAssets(),
            result.message()
        );
    }
}

