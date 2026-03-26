package com.leads.repository;

import com.leads.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByFacebookPageId(String pageId);
    Optional<Tenant> findByInstagramAccountId(String accountId);
    Optional<Tenant> findByWhatsappPhoneNumberId(String phoneNumberId);
    List<Tenant> findByTokenExpiraBeforeAndActivoTrue(LocalDateTime fecha);
    List<Tenant> findByActivoTrue();
}
