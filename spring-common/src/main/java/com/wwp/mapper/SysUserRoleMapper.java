package com.wwp.mapper;

import com.wwp.entity.SysRole;
import com.wwp.entity.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {
    List<SysRole> getRoleByUserName(@Param("username") String username);

    public List<SysRole> queryUserRoles(@Param("userId") String userId);


    void insert(@Param("id") String id,@Param("userId") String userId,@Param("roleId") String roleId);

    @Delete("delete from sys_user_role where role_id = #{roleId}")
    void deleteRoleUserRelation(@Param("roleId") String roleId);

    @Select("select * from sys_user_role ")
    List<SysUserRole> list();

    @Delete("delete from sys_user_role where user_id=#{userId}")
    void deleteByUserId(String userId);

}
