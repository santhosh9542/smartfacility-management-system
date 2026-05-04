package com.smartfacility.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="organizations")
@Data
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organizationId;

    private String organizationName;
    private String email;
    private String phone;
    private String plan;
    private String status;
}