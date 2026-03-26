package com.leads.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leads.model.Channel;
import com.leads.model.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    Optional<Lead> findBySenderIdAndChannelAndTenantId(String senderId, Channel channel, Long tenantId);
}
