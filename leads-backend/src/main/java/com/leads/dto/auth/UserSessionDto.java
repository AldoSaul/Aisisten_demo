package com.leads.dto.auth;

import com.leads.model.UserRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSessionDto {
    private Long id;
    private String email;
    private UserRole role;
    private Long tenantId;
    private String tenantName;
}
