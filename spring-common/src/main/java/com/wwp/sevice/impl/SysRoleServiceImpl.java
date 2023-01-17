package com.wwp.sevice.impl;


import com.wwp.sevice.ISysRoleService;
import com.wwp.entity.SysRole;
import com.wwp.mapper.SysRoleMapper;
import com.wwp.mapper.SysRolePermissionMapper;
import com.wwp.mapper.SysUserMapper;
import com.wwp.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Resource
    SysRoleMapper sysRoleMapper;
    @Resource
    SysUserRoleMapper sysUserRoleMapper;
    @Resource
    SysUserMapper sysUserMapper;
    @Resource
    SysRolePermissionMapper SysRolePermissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(String roleid) {
        //1.删除角色和用户关系
        sysUserRoleMapper.deleteRoleUserRelation(roleid);
        //2.删除角色和权限关系
        SysRolePermissionMapper.deleteRolePermissionRelation(roleid);
        //3.删除角色
        sysRoleMapper.remove(roleid);
        return true;
    }



    @Override
    public List<SysRole> queryUserSysRoles(String userId) {
        return sysUserRoleMapper.queryUserRoles(userId);
    }

    @Override
    public List<String> queryUserRoles(String userId){
        List<SysRole> sysRolesList = sysUserRoleMapper.queryUserRoles(userId);
        List<String> rolesList = new ArrayList<String>();

        for(SysRole role :sysRolesList ){
            rolesList.add(role.getRole());
        }
        return rolesList;
    }


}
