package com.smartfacility.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartfacility.auth.entity.Tenant;

public interface TenantRepository
extends JpaRepository<Tenant, Long>{

 Optional<Tenant> findByEmail(String email);
}