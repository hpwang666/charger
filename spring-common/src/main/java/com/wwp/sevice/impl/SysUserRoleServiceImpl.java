package com.wwp.sevice.impl;

import com.wwp.sevice.ISysUserRoleService;
import com.wwp.sevice.ISysUserService;
import com.wwp.entity.SysRole;
import com.wwp.entity.SysUser;
import com.wwp.entity.SysUserRole;
import com.wwp.mapper.SysRoleMapper;
import com.wwp.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysUserRoleServiceImpl  implements ISysUserRoleService {

	@Resource
	private ISysUserService userService;
	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysUserRoleMapper sysUserRoleMapper;
	
	/**
	 * 查询所有用户对应的角色信息
	 */
	@Override
	public Map<String,String> queryUserRole() {
		List<SysUserRole> uRoleList = sysUserRoleMapper.list();
		List<SysUser> userList = userService.list();
		List<SysRole> roleList = sysRoleMapper.list();
		Map<String,String> map = new IdentityHashMap<>();
		String userId = "";
		String roleId = "";
		String roleName = "";
		if(uRoleList != null && uRoleList.size() > 0) {
			for(SysUserRole uRole : uRoleList) {
				roleId = uRole.getRoleId();
				for(SysUser user : userList) {
					userId = user.getId();
					if(uRole.getUserId().equals(userId)) {
						roleName = this.searchByRoleId(roleList,roleId);
						map.put(userId, roleName);
					}
				}
			}
			return map;
		}
		return map;
	}
	
	/**
	 * queryUserRole调用的方法
	 * @param roleList
	 * @param roleId
	 * @return
	 */
	private String searchByRoleId(List<SysRole> roleList, String roleId) {
		while(true) {
			for(SysRole role : roleList) {
				if(roleId.equals(role.getId())) {
					return role.getDescription();
				}
			}
		}
	}

	@Override
	public List<SysRole> getRoleByUserName(String userName)
	{
		List<SysRole> list = sysUserRoleMapper.getRoleByUserName(userName);
		if(list.size()>0){
			return list;//
		}
		else
			return null;
	}

}
