package com.wwp.entity;



import com.wwp.common.aspect.annotation.Id;

import java.io.Serializable;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */

public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    
    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private String roleId;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }
    public String getRoleId()
    {
        return this.roleId;
    }

	public SysUserRole() {
	}

	public SysUserRole(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

    

}
