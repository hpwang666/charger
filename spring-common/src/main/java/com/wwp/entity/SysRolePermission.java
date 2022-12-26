package com.wwp.entity;

import com.wwp.common.aspect.annotation.Id;

import java.io.Serializable;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */


public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    private String id;
    
    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;

    public void setId(String id)
    {
        this.id = id;
    }
    public String getId()
    {
        return this.id;
    }

    
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }
    public String getRoleId()
    {
        return this.roleId;
    }

    public void setPermissionId(String permissionId)
    {
        this.permissionId = permissionId;
    }
    public String getPermissionId()
    {
        return this.permissionId;
    }

    public SysRolePermission() {
   	}
       
   	public SysRolePermission(String roleId, String permissionId) {
   		this.roleId = roleId;
   		this.permissionId = permissionId;
   	}

}
