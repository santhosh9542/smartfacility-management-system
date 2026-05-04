package com.smartfacility.tenant.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smartfacility.tenant.entity.Tenant;
import com.smartfacility.tenant.jwt.JwtUtil;
import com.smartfacility.tenant.repository.TenantRepository;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/tenants")
//@CrossOrigin(origins = "*")
public class TenantController {

    @Autowired
    private TenantRepository repo;
    
    @Autowired
    private RestTemplate restTemplate;
    


    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody Map<String,String> req) {

        restTemplate.postForObject(
          "http://AUTH-SERVICE/auth/reset-password",
          req,
          String.class
        );

        return "Password Reset Success";
    }


    @GetMapping
    public List<Tenant> all(
    @RequestHeader("Authorization") String auth){

       String token = auth.substring(7);

       Claims claims =
       JwtUtil.extractClaims(token);

       String role =
       claims.get("role",String.class);
       
       System.out.println("role -> "+ role);
       
       if("SUPER_ADMIN".equals(role)){
           return repo.findAll();
        }

      
       if("ADMIN".equals(role)){
    	     Long orgId =
    	      Long.valueOf(claims.get("organizationId").toString());

    	     return repo.findByOrganizationId(orgId);
    	   }

      

       Long tenantId =
    		   Long.valueOf(claims.get("tenantId").toString());

       return repo.findByTenantId(tenantId);
    }

   
    @PostMapping
    public Tenant save(
    @RequestBody Tenant tenant,
    @RequestHeader("Authorization") String auth){

       String token = auth.substring(7);

       Claims claims =
       JwtUtil.extractClaims(token);

       Long orgId =
       Long.valueOf(
       claims.get("organizationId").toString()
       );

       tenant.setOrganizationId(orgId);
       tenant.setStatus("ACTIVE");

       Tenant saved = repo.save(tenant);

       Map<String,Object> userReq = Map.of(
          "fullName", tenant.getTenantName(),
          "email", tenant.getEmail(),
          "password", tenant.getPassword(),
          "role", "TENANT",
          "organizationId", orgId
       );

       restTemplate.postForObject(
          "http://AUTH-SERVICE/auth/create-user",
          userReq,
          String.class
       );

       return saved;
    }

    @PutMapping("/{id}")
    public Tenant updateTenant(
            @PathVariable("id") Long id,
            @RequestBody Tenant tenant) {

        Tenant old = repo.findById(id).orElseThrow();

        old.setTenantName(tenant.getTenantName());
        old.setEmail(tenant.getEmail());
        old.setPhone(tenant.getPhone());
        old.setPlanType(tenant.getPlanType());
        old.setStatus(tenant.getStatus());

        return repo.save(old);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id){
        repo.deleteById(id);
        return "Deleted";
    }
    
    @GetMapping("/{id}")
    public Tenant getOne(
    @PathVariable("id") Long id){

       return repo.findById(id).orElseThrow();
    }
}