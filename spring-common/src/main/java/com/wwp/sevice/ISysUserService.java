package com.wwp.sevice;

import com.wwp.entity.SysUser;

import java.util.List;
import java.util.Map;

public interface ISysUserService {
    /**
     * 保存用户
     * @param user 用户
     * @param selectedRoles 选择的角色id，多个以逗号隔开
     * @param selectedDeparts 选择的部门id，多个以逗号隔开
     */
    void saveUser(SysUser user, String selectedRoles,String selectedDeparts);
    List<Map<String, Object>> getAllUsers();

    //根据部门id查询用户信息
    List<String> queryUserIdByDepId(String depId);
    SysUser queryUserByUsername(String username);

    List<SysUser> list();
}
