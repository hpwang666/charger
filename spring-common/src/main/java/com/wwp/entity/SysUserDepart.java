package com.wwp.entity;



import com.wwp.common.aspect.annotation.Id;
import java.io.Serializable;


public class SysUserDepart implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**主键id*/
    @Id
	private String id;
	/**用户id*/
	private String userId;
	/**部门id*/
	private String departId;

	public SysUserDepart(String userId,String departId)
	{
		this.userId = userId;
		this.departId = departId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
