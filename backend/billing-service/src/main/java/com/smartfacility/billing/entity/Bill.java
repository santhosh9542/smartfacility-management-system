package com.smartfacility.billing.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bills")
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private Long organizationId;

    private Long tenantId;

    private String tenantEmail;

    private String unitNo;

    private String billingMonth;

    private Double maintenanceCharge;

    private Double waterCharge;

    private Double penalty;

    private Double totalAmount;

    private String status; // PAID / UNPAID
}