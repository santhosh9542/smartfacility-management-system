package com.smartfacility.support.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartfacility.support.dto.TicketStatusRequest;
import com.smartfacility.support.entity.Ticket;
import com.smartfacility.support.jwt.JwtUtil;
import com.smartfacility.support.repository.TicketRepository;
import com.smartfacility.support.service.TicketService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/tickets")
//@CrossOrigin(origins="*")
public class TicketController {

 @Autowired
 private TicketRepository repo;
 
 @Autowired
 private TicketService service;

 // CREATE TICKET
 @PostMapping
 public Ticket create(
 @RequestBody Ticket ticket,
 @RequestHeader("Authorization") String auth){

   String token = auth.substring(7);
   Claims c = JwtUtil.extractClaims(token);

   String role = c.get("role", String.class);
   Long orgId =
   Long.valueOf(c.get("organizationId").toString());

   ticket.setOrganizationId(orgId);
   ticket.setCreatedBy(c.getSubject());
   ticket.setStatus("OPEN");

   if("TENANT".equals(role)){
     ticket.setTenantId(
       Long.valueOf(c.get("tenantId").toString())
     );
   }

   return repo.save(ticket);
 }

 // VIEW TICKETS
 @GetMapping
 public List<Ticket> all(
 @RequestHeader("Authorization") String auth){

   String token = auth.substring(7);
   Claims c = JwtUtil.extractClaims(token);

   String role = c.get("role", String.class);

   if("SUPER_ADMIN".equals(role)){
     return repo.findAll();
   }

   if("ADMIN".equals(role)){
     Long orgId =
      Long.valueOf(c.get("organizationId").toString());

     return repo.findByOrganizationIdOrderByTicketIdDesc(orgId);
   }

   Long tenantId =
   Long.valueOf(c.get("tenantId").toString());

   return repo.findByTenantId(tenantId);
 }

 // UPDATE STATUS
 @PutMapping("/status/{id}/{status}")
 public Ticket updateStatus1(
  @PathVariable("id") Long id,
  @PathVariable("status") String status){

   Ticket t = repo.findById(id).orElseThrow();

   t.setStatus(status);

   if(status.equals("RESOLVED")){
      t.setResolvedAt(LocalDateTime.now());
   }

   return repo.save(t);
 }
 
 

 @PutMapping("/update-status/{id}")
 public Ticket updateStatus(
 @PathVariable("id") Long id,
 @RequestBody TicketStatusRequest req){

    return service.updateStatus(id, req);
 }
 
 @GetMapping("/search/{text}")
 public List<Ticket> search(
 @PathVariable("text") String text){

    return repo.findBySubjectContainingIgnoreCase(text);
 }
}