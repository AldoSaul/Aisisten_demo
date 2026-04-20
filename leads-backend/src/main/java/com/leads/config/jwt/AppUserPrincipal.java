package com.leads.config.jwt;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.leads.dto.auth.UserSessionDto;
import com.leads.model.User;
import com.leads.model.UserRole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AppUserPrincipal implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final UserRole role;
    private final Long tenantId;
    private final String tenantName;
    private final boolean active;

    public static AppUserPrincipal fromUser(User user) {
        return new AppUserPrincipal(
            user.getId(),
            user.getEmail(),
            user.getPasswordHash(),
            user.getRole(),
            user.getTenant().getId(),
            user.getTenant().getNombre(),
            Boolean.TRUE.equals(user.getActive())
        );
    }

    public UserSessionDto toSessionDto() {
        return UserSessionDto.builder()
            .id(id)
            .email(email)
            .role(role)
            .tenantId(tenantId)
            .tenantName(tenantName)
            .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
