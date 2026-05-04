package com.smartfacility.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartfacility.auth.entity.OrganizationMenuPermission;
import com.smartfacility.auth.entity.RoleMenuPermission;
import com.smartfacility.auth.repository.OrganizationMenuRepository;
import com.smartfacility.auth.repository.RoleMenuRepository;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private RoleMenuRepository repo;
    
    @Autowired
    private OrganizationMenuRepository orgRepo;

    @GetMapping
    public List<RoleMenuPermission> all() {
        return repo.findAll();
    }

    @PutMapping("/{id}")
    public RoleMenuPermission update(
            @PathVariable("id") Long id,
            @RequestBody RoleMenuPermission req) {

        RoleMenuPermission p =
                repo.findById(id).orElseThrow();

        p.setIsEnabled(req.getIsEnabled());

        return repo.save(p);
    }
    
    @GetMapping("/organization/{orgId}/{role}")
    public List<OrganizationMenuPermission> orgMenus(
    @PathVariable("orgId") Long orgId,
    @PathVariable("role") String role){

       return orgRepo.findByOrganizationIdAndRoleName(
          orgId, role
       );
    }

    @PutMapping("/organization/{id}")
    public OrganizationMenuPermission updateOrg(
    @PathVariable("id") Long id,
    @RequestBody OrganizationMenuPermission req){

       OrganizationMenuPermission p =
       orgRepo.findById(id).orElseThrow();

       p.setIsEnabled(req.getIsEnabled());

       return orgRepo.save(p);
    }
}