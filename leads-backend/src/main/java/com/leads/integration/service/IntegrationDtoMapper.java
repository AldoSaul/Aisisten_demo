package com.leads.integration.service;

import org.springframework.stereotype.Component;

import com.leads.integration.dto.IntegrationAssetDto;
import com.leads.integration.dto.IntegrationConnectionDto;
import com.leads.integration.dto.IntegrationConnectionSummaryDto;
import com.leads.integration.dto.ProviderDescriptorDto;
import com.leads.integration.model.IntegrationAsset;
import com.leads.integration.model.IntegrationConnection;
import com.leads.integration.provider.ProviderDescriptor;

@Component
public class IntegrationDtoMapper {

    public ProviderDescriptorDto toDto(ProviderDescriptor descriptor) {
        return new ProviderDescriptorDto(
            descriptor.key(),
            descriptor.displayName(),
            descriptor.supportsConnect(),
            descriptor.supportsWebhooks(),
            descriptor.supportsAssetSync()
        );
    }

    public IntegrationConnectionSummaryDto toSummaryDto(IntegrationConnection connection) {
        return new IntegrationConnectionSummaryDto(
            connection.getId(),
            connection.getProviderType().key(),
            connection.getStatus().name(),
            connection.getAuthFlowStatus().name()
        );
    }

    public IntegrationConnectionDto toDetailDto(IntegrationConnection connection) {
        return new IntegrationConnectionDto(
            connection.getId(),
            connection.getPublicId(),
            connection.getTenant().getId(),
            connection.getProviderType().key(),
            connection.getStatus().name(),
            connection.getAuthFlowStatus().name(),
            connection.getExternalConnectionId(),
            connection.getLastError(),
            connection.getConnectedAt(),
            connection.getDisconnectedAt(),
            connection.getLastSyncedAt(),
            connection.getCreatedAt(),
            connection.getUpdatedAt()
        );
    }

    public IntegrationAssetDto toAssetDto(IntegrationAsset asset) {
        return new IntegrationAssetDto(
            asset.getId(),
            asset.getConnection().getId(),
            asset.getAccount().getId(),
            asset.getProviderType().key(),
            asset.getAssetType().name(),
            asset.getExternalAssetId(),
            asset.getDisplayName(),
            asset.isActive(),
            asset.getLastSyncedAt()
        );
    }
}

