package com.smartfacility.billing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smartfacility.billing.dto.TenantDto;
import com.smartfacility.billing.entity.Bill;
import com.smartfacility.billing.entity.Payment;
import com.smartfacility.billing.jwt.JwtUtil;
import com.smartfacility.billing.repository.BillRepository;
import com.smartfacility.billing.repository.PaymentRepository;
import com.smartfacility.billing.service.BillService;
import com.smartfacility.billing.service.EmailService;
import com.smartfacility.billing.service.InvoiceService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillRepository repo;
    
    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private BillService service;
    
    @Autowired
    private PaymentRepository paymentRepo;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private RestTemplate restTemplate;

    // CREATE BILL
    @PostMapping
    public Bill create(
    @RequestBody Bill bill,
    @RequestHeader("Authorization") String auth){

       String token = auth.substring(7);

       Claims claims =
       JwtUtil.extractClaims(token);

       Long orgId =
       Long.valueOf(
         claims.get("organizationId").toString()
       );

       double total =
         bill.getMaintenanceCharge()
       + bill.getWaterCharge()
       + bill.getPenalty();

       bill.setTotalAmount(total);
       bill.setStatus("UNPAID");
       bill.setOrganizationId(orgId);

       return repo.save(bill);
    }

    // GET BILLS
    @GetMapping
    public List<Bill> getBills(
    @RequestHeader("Authorization") String authHeader){

       String token = authHeader.substring(7);

       Claims claims =
       JwtUtil.extractClaims(token);

       String email = claims.getSubject();

       String role =
       claims.get("role", String.class);

       Long orgId =
       Long.valueOf(
         claims.get("organizationId").toString()
       );

       if("SUPER_ADMIN".equals(role)){
          return repo.findAll();
       }

       if("ADMIN".equals(role)){
          return repo.findByOrganizationId(orgId);
       }

       Long tenantId =
    		   Long.valueOf(
    		   claims.get("tenantId").toString()
    		   );

    		   return repo.findByOrganizationIdAndTenantId(
    		   orgId,
    		   tenantId
    		   );
    }

    // PAY BILL
    @PutMapping("/pay/{id}")
    public Bill payBill(
    @PathVariable("id") Long id
    ){
       return service.payBill(id);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> invoice(
    @PathVariable("id") Long id
    )throws Exception{

       Bill bill =
       repo.findById(id).orElseThrow();

       byte[] pdf =
       invoiceService.generate(bill);

       return ResponseEntity.ok()
       .header("Content-Disposition",
       "attachment; filename=invoice_"+id+".pdf")
       .header("Content-Type","application/pdf")
       .body(pdf);
    }
    
    @PostMapping("/generate-monthly")
    public String generateBills(
    @RequestHeader("Authorization") String auth
    ){

       // later fetch tenants by org
       // create bills for each tenant

       return "Bills Generated";
    }
    
    @PostMapping("/pay-simulate/{id}")
    public String simulatePay(
    @PathVariable("id") Long id,
    @RequestBody Payment payment){

     Bill bill =
     repo.findById(id).orElseThrow();

     bill.setStatus("PAID");
     repo.save(bill);

     payment.setBillId(id);
     payment.setAmount(bill.getTotalAmount());
     payment.setPaymentStatus("SUCCESS");

     payment.setTransactionId(
     "TXN" + System.currentTimeMillis()
     );

     paymentRepo.save(payment);

     return "Payment Success";
    }
    
    @PostMapping("/send-invoice/{id}")
    public String sendInvoice(
    @PathVariable("id") Long id
    )throws Exception{

     Bill bill =
     repo.findById(id).orElseThrow();

     // call tenant-service
     TenantDto tenant =
     restTemplate.getForObject(
    		 "http://TENANT-SERVICE/tenants/" + bill.getTenantId(),
     TenantDto.class
     );

     if(tenant == null || tenant.getEmail()==null){
       return "Tenant Email Not Found";
     }

     byte[] pdf =
     invoiceService.generate(bill);

     emailService.sendInvoice(
     tenant.getEmail(),
     pdf
     );

     return "Invoice Sent Successfully";
    }
    
    
}