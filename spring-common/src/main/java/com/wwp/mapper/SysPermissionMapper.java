package com.wwp.mapper;

import com.wwp.entity.SysPermission;
import com.wwp.model.TreeModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SysPermissionMapper {
     void add(SysPermission sysPermission);
     void update(SysPermission sysPermission);

     List<SysPermission> queryByUser(@Param("username") String username);

    @Update("update sys_permission set is_leaf=#{leaf} where id = #{id}")
    int setMenuLeaf(@Param("id") String id,@Param("leaf") int leaf);

    @Update("update sys_permission set del_flag=#{delFlag} where id = #{id}")
    int setDelFlag(@Param("id") String id,@Param("delFlag") int delFlag);

    @Select("select * from sys_permission where del_flag != 1 and resource_type = 'button'")
    List<SysPermission> querySysAuthButton();

    List<TreeModel> queryListByParentId(@Param("parentId") String parentId);

    @Select("select * from sys_permission where id = #{id}")
    SysPermission queryById(@Param("id") String id);

    @Select("select * from sys_permission where del_flag != 1 order by sort_no ASC" )
    List<SysPermission> queryAllPermissions();

    @Select("select * from sys_permission where del_flag != 1 and resource_type = 'menu0' order by sort_no ASC")
    List<SysPermission> querySystemMenuList();

    @Select("select * from sys_permission where del_flag != 1 and parent_id = #{parentId} order by sort_no ASC")
    List<SysPermission> querySystemSubmenu(@Param("parentId") String parentId);

    List<SysPermission> queryPermissionByIdList(@Param("ids") List<String> ids);

    int countByParentId(@Param("parentId") String parentId);
}
