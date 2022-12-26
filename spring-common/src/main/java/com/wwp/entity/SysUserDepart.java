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
	private String depId;
	public SysUserDepart(String id, String userId, String depId) {
		super();
		this.id = id;
		this.userId = userId;
		this.depId = depId;
	}

	public SysUserDepart(String id, String departId) {
		this.userId = id;
		this.depId = departId;
	}
}
