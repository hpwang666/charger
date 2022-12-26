package com.wwp.mapper;


import com.wwp.entity.SysRolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface SysRolePermissionMapper  {

    @Select("select * from sys_role_permission where role_id = #{roleId}")
    public List<SysRolePermission> queryByRoleId(String roleId);

    @Delete("delete from sys_role_permission where id = #{id}")
    public void remove(String id);

    public void saveBatch(List<SysRolePermission> sysRolePermissions);

    @Delete("delete from sys_role_permission where role_id = #{roleId}")
    void deleteRolePermissionRelation(@Param("roleId") String roleId);

}
