package com.leads.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tenants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "facebook_page_id", unique = true)
    private String facebookPageId;

    @Column(name = "instagram_account_id", unique = true)
    private String instagramAccountId;

    @Column(name = "whatsapp_phone_number_id", unique = true)
    private String whatsappPhoneNumberId;

    @Column(name = "access_token", length = 1000)
    private String accessToken;

    @Column(name = "token_expira")
    private LocalDateTime tokenExpira;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
/* @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }*/
    
}
