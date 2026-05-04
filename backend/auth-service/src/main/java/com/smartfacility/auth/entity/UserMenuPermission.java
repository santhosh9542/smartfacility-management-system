package com.smartfacility.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="user_menu_permission")
@Data
public class UserMenuPermission {
 @Id
 @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;

 private Long userId;
 private Long organizationId;
 private String menuName;
 private Boolean isEnabled;
}