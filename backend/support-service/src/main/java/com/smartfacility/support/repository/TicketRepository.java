package com.smartfacility.support.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartfacility.support.entity.Ticket;

public interface TicketRepository
extends JpaRepository<Ticket,Long>{

 List<Ticket> findByOrganizationId(Long organizationId);

 List<Ticket> findByTenantId(Long tenantId);
 
 List<Ticket> findByOrganizationIdOrderByTicketIdDesc(Long organizationId);
 List<Ticket> findByTenantIdOrderByTicketIdDesc(Long tenantId);
 List<Ticket> findBySubjectContainingIgnoreCase(String subject);
}