package com.smartfacility.billing.service;

import java.util.List;
import com.smartfacility.billing.entity.Bill;

public interface BillService {

    Bill saveBill(Bill bill);

    List<Bill> getAllBills();

    Bill payBill(Long id);
}