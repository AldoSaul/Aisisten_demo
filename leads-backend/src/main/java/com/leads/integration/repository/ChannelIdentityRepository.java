package com.leads.integration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.integration.model.ChannelIdentity;
import com.leads.model.Channel;

public interface ChannelIdentityRepository extends JpaRepository<ChannelIdentity, Long> {

    Optional<ChannelIdentity> findByTenantIdAndChannelAndExternalId(Long tenantId, Channel channel, String externalId);
}

