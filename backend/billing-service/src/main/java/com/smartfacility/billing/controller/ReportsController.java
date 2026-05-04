package com.smartfacility.billing.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.smartfacility.billing.entity.Bill;
import com.smartfacility.billing.jwt.JwtUtil;
import com.smartfacility.billing.repository.BillRepository;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/reports")
public class ReportsController {

    @Autowired
    private BillRepository billRepository;

    private List<Bill> getBillsByRole(String auth){

        String token = auth.substring(7);

        Claims c = JwtUtil.extractClaims(token);

        String role = c.get("role", String.class);

        Long orgId =
        Long.valueOf(c.get("organizationId").toString());

        Long tenantId =
        Long.valueOf(c.get("tenantId").toString());

        if("SUPER_ADMIN".equals(role)){
            return billRepository.findAll();
        }

        if("ADMIN".equals(role)){
            return billRepository.findByOrganizationId(orgId);
        }

        return billRepository.findByOrganizationIdAndTenantId(
            orgId, tenantId
        );
    }

    @GetMapping("/summary")
    public Map<String,Object> summary(
    @RequestHeader("Authorization") String auth){

        List<Bill> bills = getBillsByRole(auth);

        double revenue = bills.stream()
        .filter(x -> "PAID".equals(x.getStatus()))
        .mapToDouble(Bill::getTotalAmount)
        .sum();

        double pending = bills.stream()
        .filter(x -> "UNPAID".equals(x.getStatus()))
        .mapToDouble(Bill::getTotalAmount)
        .sum();

        Map<String,Object> map = new HashMap<>();
        map.put("totalBills", bills.size());
        map.put("revenue", revenue);
        map.put("pending", pending);

        return map;
    }

    @GetMapping("/status")
    public Map<String,Object> status(
    @RequestHeader("Authorization") String auth){

        List<Bill> bills = getBillsByRole(auth);

        long paid = bills.stream()
        .filter(x -> "PAID".equals(x.getStatus()))
        .count();

        long unpaid = bills.stream()
        .filter(x -> "UNPAID".equals(x.getStatus()))
        .count();

        Map<String,Object> map = new HashMap<>();
        map.put("paid", paid);
        map.put("unpaid", unpaid);

        return map;
    }

    @GetMapping("/monthly")
    public List<Map<String,Object>> monthly(
    @RequestHeader("Authorization") String auth){

        List<Bill> bills = getBillsByRole(auth);

        Map<String,Double> monthly = new HashMap<>();

        for(Bill b : bills){

            if(b.getBillingMonth()==null) continue;

            monthly.put(
                b.getBillingMonth(),
                monthly.getOrDefault(
                    b.getBillingMonth(),0.0
                ) + b.getTotalAmount()
            );
        }

        List<Map<String,Object>> list = new ArrayList<>();

        for(String key : monthly.keySet()){

            Map<String,Object> row = new HashMap<>();

            row.put("month", key);
            row.put("revenue", monthly.get(key));

            list.add(row);
        }

        return list;
    }
}