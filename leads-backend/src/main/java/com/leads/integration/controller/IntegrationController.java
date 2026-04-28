package com.leads.integration.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leads.config.jwt.AppUserPrincipal;
import com.leads.integration.dto.AssetSyncRequest;
import com.leads.integration.dto.DisconnectIntegrationRequest;
import com.leads.integration.dto.IntegrationAssetDto;
import com.leads.integration.dto.IntegrationConnectCallbackResponse;
import com.leads.integration.dto.IntegrationConnectStartRequest;
import com.leads.integration.dto.IntegrationConnectStartResponse;
import com.leads.integration.dto.IntegrationConnectionDto;
import com.leads.integration.dto.IntegrationConnectionSummaryDto;
import com.leads.integration.dto.IntegrationSyncResultDto;
import com.leads.integration.dto.ProviderDescriptorDto;
import com.leads.integration.dto.ReconnectIntegrationRequest;
import com.leads.integration.service.IntegrationAssetService;
import com.leads.integration.service.IntegrationConnectionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v1/integrations")
@RequiredArgsConstructor
public class IntegrationController {

    private final IntegrationConnectionService connectionService;
    private final IntegrationAssetService assetService;

    @GetMapping("/providers")
    public ResponseEntity<List<ProviderDescriptorDto>> listProviders() {
        return ResponseEntity.ok(connectionService.listProviders());
    }

    @GetMapping
    public ResponseEntity<List<IntegrationConnectionSummaryDto>> listIntegrations(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @RequestParam(required = false) Long tenantId
    ) {
        Long resolvedTenantId = resolveTenant(principal, tenantId);
        return ResponseEntity.ok(connectionService.listConnections(resolvedTenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntegrationConnectionDto> getIntegration(
        @PathVariable Long id,
        @AuthenticationPrincipal AppUserPrincipal principal,
        @RequestParam(required = false) Long tenantId
    ) {
        Long resolvedTenantId = resolveTenant(principal, tenantId);
        return ResponseEntity.ok(connectionService.getConnection(id, resolvedTenantId));
    }

    @PostMapping("/{provider}/connect/start")
    public ResponseEntity<IntegrationConnectStartResponse> startConnect(
        @PathVariable String provider,
        @Valid @RequestBody IntegrationConnectStartRequest request,
        @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        enforceTenant(principal, request.tenantId());
        return ResponseEntity.ok(connectionService.startConnect(provider, request));
    }

    @GetMapping("/{provider}/connect/callback")
    public ResponseEntity<IntegrationConnectCallbackResponse> connectCallback(
        @PathVariable String provider,
        @RequestParam Map<String, String> queryParams
    ) {
        return ResponseEntity.ok(connectionService.handleCallback(provider, queryParams));
    }

    @GetMapping("/assets")
    public ResponseEntity<List<IntegrationAssetDto>> listAssets(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @RequestParam(required = false) Long tenantId
    ) {
        Long resolvedTenantId = resolveTenant(principal, tenantId);
        return ResponseEntity.ok(assetService.listAssets(resolvedTenantId));
    }

    @PostMapping("/{provider}/assets/sync")
    public ResponseEntity<IntegrationSyncResultDto> syncAssets(
        @PathVariable String provider,
        @Valid @RequestBody AssetSyncRequest request,
        @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        enforceTenant(principal, request.tenantId());
        return ResponseEntity.ok(assetService.syncAssets(provider, request.tenantId()));
    }

    @PostMapping("/{id}/disconnect")
    public ResponseEntity<IntegrationConnectionDto> disconnect(
        @PathVariable Long id,
        @Valid @RequestBody DisconnectIntegrationRequest request,
        @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        enforceTenant(principal, request.tenantId());
        return ResponseEntity.ok(connectionService.disconnect(id, request.tenantId()));
    }

    @PostMapping("/{id}/reconnect")
    public ResponseEntity<IntegrationConnectionDto> reconnect(
        @PathVariable Long id,
        @Valid @RequestBody ReconnectIntegrationRequest request,
        @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        enforceTenant(principal, request.tenantId());
        return ResponseEntity.ok(connectionService.reconnect(id, request.tenantId()));
    }

    private Long resolveTenant(AppUserPrincipal principal, Long tenantId) {
        if (principal == null) {
            throw new IllegalArgumentException("Unauthorized");
        }
        if (tenantId == null) {
            return principal.getTenantId();
        }
        if (!principal.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Forbidden tenant access");
        }
        return tenantId;
    }

    private void enforceTenant(AppUserPrincipal principal, Long tenantId) {
        if (principal == null) {
            throw new IllegalArgumentException("Unauthorized");
        }
        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (!principal.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Forbidden tenant access");
        }
    }
}

