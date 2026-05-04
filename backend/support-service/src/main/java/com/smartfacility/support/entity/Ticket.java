package com.smartfacility.support.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="support_tickets")
@Data
public class Ticket {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long ticketId;

 private Long tenantId;
 private Long organizationId;

 private String createdBy;
 @Column(nullable = false)
 private String subject;

 @Column(length = 5000, nullable = false)
 private String description;

 @Column(nullable = false)
 private String priority;

 @Column(nullable = false)
 private String status;  // OPEN IN_PROGRESS RESOLVED

 private LocalDateTime createdAt = LocalDateTime.now();
 @PrePersist
 public void prePersist() {
     createdAt = LocalDateTime.now();
 }

 private LocalDateTime resolvedAt;
 
 @Column(length = 2000)
 private String remarks;
}