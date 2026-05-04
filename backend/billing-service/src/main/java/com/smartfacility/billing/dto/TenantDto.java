package com.smartfacility.billing.dto;

import lombok.Data;

@Data
public class TenantDto {

 private Long tenantId;
 private String tenantName;
 private String email;
 private String phone;
 private String planType;
 private String status;
 private Long organizationId;
}