package com.leads.integration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.integration.model.OAuthConnection;

public interface OAuthConnectionRepository extends JpaRepository<OAuthConnection, Long> {

    Optional<OAuthConnection> findByConnectionId(Long connectionId);
}

