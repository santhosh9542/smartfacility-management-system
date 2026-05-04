package com.smartfacility.billing.scheduler;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smartfacility.billing.entity.Bill;
import com.smartfacility.billing.repository.BillRepository;
import org.springframework.web.client.RestTemplate;

@Component
public class BillScheduler {

    @Autowired
    private BillRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void runMonthly(){

      System.out.println("Generating Bills...");
      // call service here
    }
    
    @Scheduled(cron = "0 0 1 1 * ?")
    public void generateBills() {

        String month =
            LocalDate.now()
            .getMonth()
            .getDisplayName(
                TextStyle.SHORT,
                Locale.ENGLISH
            ) + "-" + LocalDate.now().getYear();

        List<Map> tenants =
            restTemplate.getForObject(
              "http://TENANT-SERVICE/tenants",
              List.class
            );

        for(Map t : tenants) {

            String status =
                String.valueOf(
                    t.get("status")
                );

            if(!status.equals("ACTIVE"))
                continue;

            Bill bill = new Bill();

            bill.setTenantId(
              Long.valueOf(
               t.get("tenantId").toString()
              )
            );

            bill.setTenantEmail(
              t.get("email").toString()
            );

            bill.setOrganizationId(
              Long.valueOf(
               t.get("organizationId")
               .toString()
              )
            );

            bill.setBillingMonth(month);

            bill.setMaintenanceCharge(2000.0);
            bill.setWaterCharge(500.0);
            bill.setPenalty(0.0);

            bill.setTotalAmount(2500.0);

            bill.setStatus("UNPAID");

            repo.save(bill);
        }

        System.out.println(
          "Monthly Bills Generated"
        );
    }
}