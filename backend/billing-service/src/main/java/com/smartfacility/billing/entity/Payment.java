package com.smartfacility.billing.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="payments")
@Data
public class Payment {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long paymentId;

 private Long billId;
 private Double amount;
 private String paymentMethod;
 private String transactionId;
 private String paymentStatus;

 private LocalDateTime paidAt =
 LocalDateTime.now();
}