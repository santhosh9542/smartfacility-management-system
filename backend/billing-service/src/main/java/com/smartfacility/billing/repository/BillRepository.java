package com.smartfacility.billing.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartfacility.billing.entity.Bill;

public interface BillRepository
extends JpaRepository<Bill,Long>{

   List<Bill> findByOrganizationId(Long organizationId);

   List<Bill> findByTenantEmail(String tenantEmail);

   List<Bill> findByOrganizationIdAndTenantEmail(
      Long organizationId,
      String tenantEmail
   );
   List<Bill> findByOrganizationIdAndTenantId(
		   Long organizationId,
		   Long tenantId
		   );
   
   long countByOrganizationId(Long organizationId);

   long countByStatus(String status);
}