package com.wwp.mapper;

import com.wwp.entity.SysRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper {
    String getRoleIdByRole(String role);

    @Delete("delete from sys_role where id = #{roleId}")
    void remove(@Param("roleId") String roleId);

    @Select("select * from sys_role ")
    List<SysRole> list();
}
