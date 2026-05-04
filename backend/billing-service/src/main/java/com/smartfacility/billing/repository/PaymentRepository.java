package com.smartfacility.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartfacility.billing.entity.Payment;

public interface PaymentRepository
extends JpaRepository<Payment,Long>{
}