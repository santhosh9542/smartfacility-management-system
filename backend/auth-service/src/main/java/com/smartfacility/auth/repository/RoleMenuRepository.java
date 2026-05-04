package com.smartfacility.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartfacility.auth.entity.RoleMenuPermission;

public interface RoleMenuRepository
extends JpaRepository<RoleMenuPermission,Long>{

 List<RoleMenuPermission>
 findByRoleName(String roleName);

 List<RoleMenuPermission>
 findByRoleNameAndIsEnabledTrue(String roleName);
}