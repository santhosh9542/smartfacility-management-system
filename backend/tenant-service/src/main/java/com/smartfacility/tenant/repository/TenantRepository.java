package com.smartfacility.tenant.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartfacility.tenant.entity.Tenant;

public interface TenantRepository
extends JpaRepository<Tenant,Long>{

   List<Tenant> findByOrganizationId(Long organizationId);

List<Tenant> findByTenantId(Long tenantId);
}