package com.leads.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leads.dto.auth.LoginRequest;
import com.leads.dto.auth.LoginResponse;
import com.leads.dto.auth.UserSessionDto;
import com.leads.model.User;
import com.leads.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new BadCredentialsException("User is inactive");
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!matches) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return LoginResponse.builder()
            .user(toSessionDto(user))
            .build();
    }

    @Transactional(readOnly = true)
    public UserSessionDto getCurrentUserByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new BadCredentialsException("User not found"));
        return toSessionDto(user);
    }

    private UserSessionDto toSessionDto(User user) {
        return UserSessionDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .role(user.getRole())
            .tenantId(user.getTenant().getId())
            .tenantName(user.getTenant().getNombre())
            .build();
    }
}
