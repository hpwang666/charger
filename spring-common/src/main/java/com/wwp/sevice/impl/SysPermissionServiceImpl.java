package com.wwp.sevice.impl;


import com.wwp.sevice.ISysPermissionService;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.SysPermission;
import com.wwp.mapper.SysPermissionMapper;
import com.wwp.model.TreeModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysPermissionServiceImpl implements ISysPermissionService {

	@Resource
	private SysPermissionMapper sysPermissionMapper;
	

	
	@Override
	public List<TreeModel> queryListByParentId(String parentId) {
		return sysPermissionMapper.queryListByParentId(parentId);
	}

	@Override
	public List<SysPermission> queryAllPermissions(){
		return sysPermissionMapper.queryAllPermissions();
	}

	public List<SysPermission> querySysAuthButton()
	{
		return sysPermissionMapper.querySysAuthButton();
	}

	@Override
	public List<SysPermission> querySystemMenuList()
	{
		return sysPermissionMapper.querySystemMenuList();
	}

	@Override
	public List<SysPermission> querySystemSubmenu(String parentId)
	{
		return sysPermissionMapper.querySystemSubmenu(parentId);
	}

	@Override
	public List<SysPermission> queryPermissionByIdList(List<String> ids)
	{
		return sysPermissionMapper.queryPermissionByIdList(ids);
	}
	
	/**
	  * 逻辑删除
	 */
	@Override
	public void deletePermissionLogical(String id) throws CustomException {
		SysPermission sysPermission = sysPermissionMapper.queryById(id);
		if(sysPermission==null) {
			throw new CustomException("未找到菜单信息");
		}
		String pid = sysPermission.getParentId();
		int count = sysPermissionMapper.countByParentId(pid);
		if(count==1) {
			//若父节点无其他子节点，则该父节点是叶子节点
			this.sysPermissionMapper.setMenuLeaf(pid, 1);
		}
		sysPermissionMapper.setDelFlag(pid, 1);
	}

	@Override
	public void addPermission(SysPermission sysPermission) throws CustomException {
		//----------------------------------------------------------------------
		//判断是否是一级菜单，是的话清空父菜单
		if(sysPermission.getResourceType().equals("menu0")) {
			sysPermission.setParentId(null);
		}
		//----------------------------------------------------------------------
		String pid = sysPermission.getParentId();
		if(oConvertUtils.isNotEmpty(pid)) {
			//设置父节点不为叶子节点
			this.sysPermissionMapper.setMenuLeaf(pid, 0);
		}

		sysPermission.setDelFlag(false);
		sysPermission.setIsLeaf(true);
		sysPermissionMapper.add(sysPermission);
	}

	@Override

	public void editPermission(SysPermission sysPermission) throws CustomException {
		SysPermission p = sysPermissionMapper.queryById(sysPermission.getId());
		//TODO 该节点判断是否还有子节点
		if(p==null) {
			throw new CustomException("未找到菜单信息");
		}else {

			//----------------------------------------------------------------------
			//Step1.判断是否是一级菜单，是的话清空父菜单ID
			if(sysPermission.getResourceType().equals("menu0")) {
				sysPermission.setParentId("");
			}
			//Step2.判断菜单下级是否有菜单，无则设置为叶子节点
			int count = sysPermissionMapper.countByParentId(sysPermission.getId());
			if(count==0) {
				sysPermission.setIsLeaf(true);
			}
			//----------------------------------------------------------------------
			sysPermissionMapper.update(sysPermission);
			
			//如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
			String pid = sysPermission.getParentId();
			if((oConvertUtils.isNotEmpty(pid) && !pid.equals(p.getParentId())) || oConvertUtils.isEmpty(pid)&&oConvertUtils.isNotEmpty(p.getParentId())) {
				//a.设置新的父菜单不为叶子节点
				this.sysPermissionMapper.setMenuLeaf(pid, 0);
				//b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
				int cc = sysPermissionMapper.countByParentId(p.getParentId());
				if(cc==0) {
					if(oConvertUtils.isNotEmpty(p.getParentId())) {
						this.sysPermissionMapper.setMenuLeaf(p.getParentId(), 1);
					}
				}
				
			}
		}
		
	}

	@Override
	public List<SysPermission> queryByUser(String username) {
		return this.sysPermissionMapper.queryByUser(username);
	}

}
