package com.smartfacility.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartfacility.auth.entity.UserMenuPermission;

public interface UserMenuRepository
extends JpaRepository<UserMenuPermission,Long>{

 List<UserMenuPermission>
 findByUserIdAndIsEnabledTrue(Long userId);
}