package com.wwp.sevice;



import com.wwp.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysUserRoleService  {
	
	/**
	 * 查询所有的用户角色信息
	 * @return
	 */
	Map<String,String> queryUserRole();

	public List<SysRole> getRoleByUserName(String userName);

}
