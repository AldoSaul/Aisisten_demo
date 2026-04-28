package com.leads.integration.model;

import java.time.LocalDateTime;

import com.leads.model.Tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "integration_connections",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_integration_connection_public_id", columnNames = "public_id"),
        @UniqueConstraint(name = "uq_integration_connection_tenant_provider_external", columnNames = {
            "tenant_id", "provider_type", "external_connection_id"
        })
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntegrationConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, length = 80)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false, length = 50)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    @Builder.Default
    private ConnectionStatus status = ConnectionStatus.NOT_CONNECTED;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_flow_status", nullable = false, length = 40)
    @Builder.Default
    private AuthFlowStatus authFlowStatus = AuthFlowStatus.NOT_STARTED;

    @Column(name = "external_connection_id", length = 255)
    private String externalConnectionId;

    @Column(name = "auth_state", length = 255, unique = true)
    private String authState;

    @Column(name = "auth_started_at")
    private LocalDateTime authStartedAt;

    @Column(name = "auth_completed_at")
    private LocalDateTime authCompletedAt;

    @Column(name = "last_error", length = 500)
    private String lastError;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @Column(name = "connected_at")
    private LocalDateTime connectedAt;

    @Column(name = "disconnected_at")
    private LocalDateTime disconnectedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

