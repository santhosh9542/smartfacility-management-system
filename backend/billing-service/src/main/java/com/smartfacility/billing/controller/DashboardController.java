package com.smartfacility.billing.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartfacility.billing.jwt.JwtUtil;
import com.smartfacility.billing.repository.BillRepository;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private BillRepository repo;

    @GetMapping("/summary")
    public Map<String,Object> summary(
    @RequestHeader("Authorization") String auth){

        String token = auth.substring(7);

        Claims c = JwtUtil.extractClaims(token);

        String role = c.get("role", String.class);

        Long orgId =
        Long.valueOf(
        c.get("organizationId").toString()
        );

        Map<String,Object> map =
        new HashMap<>();

        if("SUPER_ADMIN".equals(role)){

            map.put("totalBills",
            repo.count());

        }else{

            map.put("totalBills",
            repo.countByOrganizationId(orgId));

        }

        map.put("paidBills",
        repo.countByStatus("PAID"));

        map.put("unpaidBills",
        repo.countByStatus("UNPAID"));

        return map;
    }
}