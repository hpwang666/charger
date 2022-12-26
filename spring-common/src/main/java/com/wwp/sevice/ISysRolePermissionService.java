package com.wwp.sevice;


import com.wwp.entity.SysRolePermission;

import java.util.List;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysRolePermissionService {
	
	public List<SysRolePermission> queryRolePermissionByRoleId(String roleId);

	/**
	 * 保存授权/先删后增
	 * @param roleId
	 * @param permissionIds
	 */
	public void saveRolePermission(String roleId, String permissionIds);

	/**
	 * 保存授权 将上次的权限和这次作比较 差异处理提高效率
	 * @param roleId
	 * @param permissionIds
	 * @param lastPermissionIds
	 */
	//public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds);

}
