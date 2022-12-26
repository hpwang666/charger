package com.wwp.sevice;



import com.wwp.common.exception.CustomException;
import com.wwp.entity.SysPermission;
import com.wwp.model.TreeModel;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService  {

	public List<SysPermission> queryAllPermissions();
	public List<SysPermission> querySystemMenuList();
	public List<SysPermission> querySysAuthButton();
	public List<SysPermission> querySystemSubmenu(String parentId);
	public List<SysPermission> queryPermissionByIdList(List<String> IdList);
	
	public List<TreeModel> queryListByParentId(String parentId);



	/**逻辑删除*/
	public void deletePermissionLogical(String id) throws CustomException;
	
	public void addPermission(SysPermission sysPermission) throws CustomException;
	
	public void editPermission(SysPermission sysPermission) throws CustomException;
	
	public List<SysPermission> queryByUser(String username);

}
