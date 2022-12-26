package com.wwp.sevice.impl;

import com.wwp.mapper.*;
import com.wwp.sevice.ISysUserService;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SysUserService implements ISysUserService {

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

    @Resource
    SysUserDepartMapper sysUserDepartMapper;

    @Resource
    SysDepartMapper sysDepartMapper;

    @Override
    public void saveUser(SysUser user, String selectedRoles,String selectedDeparts) {
        //step.1 保存用户
        sysUserMapper.reg(user);
        //step.2 保存角色

        String[] arr = selectedRoles.split(",");
        for (String role : arr) {
            sysUserRoleMapper.insert(UUID.randomUUID().toString().replaceAll("-", ""),user.getId(),sysRoleMapper.getRoleIdByRole(role));
        }

        //step.3 保存所属部门

        if(oConvertUtils.isNotEmpty(selectedDeparts)) {
            arr = selectedDeparts.split(",");
            for (String deaprtId : arr) {
                SysDepart depart = sysDepartMapper.queryDepartById(deaprtId);
                if(depart == null) throw new CustomException("没找到机构");
                sysUserDepartMapper.save(user.getId(), deaprtId);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getAllUsers()
    {
        return sysUserMapper.queryAllUsers();
    }

    @Override
    public  List<String> queryUserIdByDepId(String depId){
        return sysUserMapper.queryUserIdByDepId(depId);
    }

    @Override
    public List<SysUser> list()
    {
        return sysUserMapper.list();
    }

    @Override
    public  SysUser queryUserByUsername(String username)
    {
        return  sysUserMapper.getUserByUsername(username);
    }
}
