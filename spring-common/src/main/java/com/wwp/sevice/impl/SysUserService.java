package com.wwp.sevice.impl;

import com.wwp.entity.SysRole;
import com.wwp.entity.SysUserDepart;
import com.wwp.mapper.*;
import com.wwp.sevice.ISysUserService;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUser user, String depId) {
        //step.1 保存用户
        sysUserMapper.reg(user);

        String[] roles = {"city","group","com","prj"};

        System.out.println("生成的"+ user.getId());

        //step.2 保存所属部门
        if(!oConvertUtils.isEmpty(depId)){
            SysDepart depart = sysDepartMapper.queryDepartById(depId);
            if(oConvertUtils.isEmpty(depart)) throw new CustomException("没找到机构");
            Integer orgCategory =depart.getOrgCategory()+1;//可管理的部门层级

            //添加角色
            for(int i=orgCategory;i<5;i++){
                String roleId =sysRoleMapper.getRoleIdByRole(roles[i-1]);
                if(oConvertUtils.isEmpty(roleId)) throw new CustomException("未找到role："+roles[i-1]);
                sysUserRoleMapper.insert(UUID.randomUUID().toString().replaceAll("-", ""),user.getId(),roleId);
            }

            SysUserDepart sysUserDepart = new SysUserDepart(user.getId(),depId );

            sysUserDepartMapper.save(sysUserDepart);
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
    public  SysUser queryUserByAccount(String account)
    {
        return  sysUserMapper.getUserByAccount(account);
    }
}
