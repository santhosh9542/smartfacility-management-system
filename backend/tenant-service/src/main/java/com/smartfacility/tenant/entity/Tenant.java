package com.smartfacility.tenant.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="tenants")
@Data
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenantId;

    private String tenantName;
    private String email;
    private String phone;
    private String planType;
    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();
    private Long organizationId;
    @Transient
    private String password;
}