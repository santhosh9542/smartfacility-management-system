package com.smartfacility.auth.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartfacility.auth.entity.OrganizationMenuPermission;

public interface OrganizationMenuRepository
extends JpaRepository<OrganizationMenuPermission,Long>{

    List<OrganizationMenuPermission>
    findByOrganizationIdAndRoleName(
        Long organizationId,
        String roleName
    );
}