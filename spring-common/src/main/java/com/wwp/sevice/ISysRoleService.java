package com.wwp.sevice;



import com.wwp.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
public interface ISysRoleService  {

    /**
     * 删除角色
     * @param roleid
     * @return
     */
    public boolean deleteRole(String roleid);


    List<SysRole> queryUserSysRoles(String userId);

    List<String> queryUserRoles(String userId);
}
