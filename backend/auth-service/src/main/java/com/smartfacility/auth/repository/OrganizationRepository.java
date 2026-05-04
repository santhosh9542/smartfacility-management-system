package com.smartfacility.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartfacility.auth.entity.Organization;

public interface OrganizationRepository
extends JpaRepository<Organization, Long> {
}