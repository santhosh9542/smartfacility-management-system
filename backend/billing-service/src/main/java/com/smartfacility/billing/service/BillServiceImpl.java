package com.smartfacility.billing.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartfacility.billing.entity.Bill;
import com.smartfacility.billing.repository.BillRepository;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository repo;

    @Override
    public Bill saveBill(Bill bill) {
        return repo.save(bill);
    }

    @Override
    public List<Bill> getAllBills() {
        return repo.findAll();
    }

    @Override
    public Bill payBill(Long id){

       Bill bill = repo.findById(id).orElseThrow();

       bill.setStatus("PAID");

       return repo.save(bill);
    }
}