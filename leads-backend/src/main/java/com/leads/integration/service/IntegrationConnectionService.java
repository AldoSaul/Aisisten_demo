package com.leads.integration.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leads.integration.dto.IntegrationConnectCallbackResponse;
import com.leads.integration.dto.IntegrationConnectStartRequest;
import com.leads.integration.dto.IntegrationConnectStartResponse;
import com.leads.integration.dto.IntegrationConnectionDto;
import com.leads.integration.dto.IntegrationConnectionSummaryDto;
import com.leads.integration.dto.ProviderDescriptorDto;
import com.leads.integration.model.AuthFlowStatus;
import com.leads.integration.model.ConnectionStatus;
import com.leads.integration.model.IntegrationConnection;
import com.leads.integration.model.ProviderType;
import com.leads.integration.provider.ConnectCallbackResult;
import com.leads.integration.provider.ConnectStartResult;
import com.leads.integration.provider.IntegrationProviderHandler;
import com.leads.integration.provider.IntegrationProviderRegistry;
import com.leads.integration.repository.IntegrationConnectionRepository;
import com.leads.model.Tenant;
import com.leads.repository.TenantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IntegrationConnectionService {

    private final IntegrationProviderRegistry providerRegistry;
    private final IntegrationConnectionRepository connectionRepository;
    private final TenantRepository tenantRepository;
    private final IntegrationDtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public List<ProviderDescriptorDto> listProviders() {
        return providerRegistry.listProviders().stream().map(dtoMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<IntegrationConnectionSummaryDto> listConnections(Long tenantId) {
        return connectionRepository.findByTenantIdOrderByUpdatedAtDesc(tenantId).stream()
            .map(dtoMapper::toSummaryDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public IntegrationConnectionDto getConnection(Long connectionId, Long tenantId) {
        IntegrationConnection connection = connectionRepository.findByIdAndTenantId(connectionId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Integration connection not found"));
        return dtoMapper.toDetailDto(connection);
    }

    @Transactional
    public IntegrationConnectStartResponse startConnect(String providerKey, IntegrationConnectStartRequest request) {
        ProviderType providerType = ProviderType.fromKey(providerKey);
        IntegrationProviderHandler provider = providerRegistry.requireByType(providerType);
        Tenant tenant = tenantRepository.findById(request.tenantId())
            .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        IntegrationConnection connection = connectionRepository
            .findFirstByTenantIdAndProviderTypeOrderByUpdatedAtDesc(tenant.getId(), providerType)
            .orElseGet(() -> IntegrationConnection.builder()
                .publicId(UUID.randomUUID().toString())
                .tenant(tenant)
                .providerType(providerType)
                .status(ConnectionStatus.NOT_CONNECTED)
                .authFlowStatus(AuthFlowStatus.NOT_STARTED)
                .build());

        ConnectStartResult result = provider.startConnect(tenant.getId());
        connection.setStatus(result.connectionStatus());
        connection.setAuthFlowStatus(result.authFlowStatus());
        connection.setAuthState(result.state());
        connection.setAuthStartedAt(LocalDateTime.now());
        connection.setLastError(null);
        connection = connectionRepository.save(connection);

        return new IntegrationConnectStartResponse(
            connection.getId(),
            providerType.key(),
            result.state(),
            result.redirectUrl(),
            connection.getStatus().name(),
            connection.getAuthFlowStatus().name()
        );
    }

    @Transactional
    public IntegrationConnectCallbackResponse handleCallback(String providerKey, Map<String, String> queryParams) {
        ProviderType providerType = ProviderType.fromKey(providerKey);
        IntegrationProviderHandler provider = providerRegistry.requireByType(providerType);

        String state = queryParams.get("state");
        if (state == null || state.isBlank()) {
            throw new IllegalArgumentException("Missing callback state");
        }

        IntegrationConnection connection = connectionRepository.findByAuthState(state)
            .orElseThrow(() -> new IllegalArgumentException("Connection state not found"));

        if (!connection.getProviderType().equals(providerType)) {
            throw new IllegalArgumentException("Callback provider mismatch");
        }

        ConnectCallbackResult result = provider.handleCallback(queryParams);
        connection.setAuthFlowStatus(result.authFlowStatus());
        connection.setStatus(result.connectionStatus());
        connection.setAuthCompletedAt(LocalDateTime.now());
        connection.setAuthState(null);
        if (result.success()) {
            connection.setConnectedAt(LocalDateTime.now());
            connection.setDisconnectedAt(null);
            connection.setLastError(null);
        } else {
            connection.setLastError(result.message());
        }
        connectionRepository.save(connection);

        return new IntegrationConnectCallbackResponse(
            result.success(),
            providerType.key(),
            result.message(),
            connection.getStatus().name(),
            connection.getAuthFlowStatus().name()
        );
    }

    @Transactional
    public IntegrationConnectionDto disconnect(Long connectionId, Long tenantId) {
        IntegrationConnection connection = connectionRepository.findByIdAndTenantId(connectionId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Integration connection not found"));

        connection.setStatus(ConnectionStatus.DISCONNECTED);
        connection.setDisconnectedAt(LocalDateTime.now());
        connection.setLastError(null);
        connection = connectionRepository.save(connection);
        return dtoMapper.toDetailDto(connection);
    }

    @Transactional
    public IntegrationConnectionDto reconnect(Long connectionId, Long tenantId) {
        IntegrationConnection connection = connectionRepository.findByIdAndTenantId(connectionId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Integration connection not found"));

        connection.setStatus(ConnectionStatus.PENDING);
        connection.setAuthFlowStatus(AuthFlowStatus.STARTED);
        connection.setAuthStartedAt(LocalDateTime.now());
        connection.setLastError(null);
        connection = connectionRepository.save(connection);
        return dtoMapper.toDetailDto(connection);
    }
}

