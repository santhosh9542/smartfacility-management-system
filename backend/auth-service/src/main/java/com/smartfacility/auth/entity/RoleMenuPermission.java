package com.smartfacility.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="role_menu_permission")
@Data
public class RoleMenuPermission {
 @Id
 @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;

 private String roleName;
 private String menuName;
 private Boolean isEnabled;
}