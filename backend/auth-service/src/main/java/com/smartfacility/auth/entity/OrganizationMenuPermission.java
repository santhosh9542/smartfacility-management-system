package com.smartfacility.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name="organization_menu_permission")
public class OrganizationMenuPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long organizationId;
    private String roleName;
    private String menuName;
    private Boolean isEnabled;

    public Long getId() { return id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long x){organizationId=x;}

    public String getRoleName(){return roleName;}
    public void setRoleName(String x){roleName=x;}

    public String getMenuName(){return menuName;}
    public void setMenuName(String x){menuName=x;}

    public Boolean getIsEnabled(){return isEnabled;}
    public void setIsEnabled(Boolean x){isEnabled=x;}
}