package com.wwp.mapper;

import com.wwp.entity.SysUser;
import org.apache.ibatis.annotations.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserMapper {
      SysUser getUserByAccount(String account);

      @Select("select * from sys_user where id=#{id} and state =1")
      SysUser queryUserById(String id);

      void reg(SysUser sysUser);

      void updateUser(SysUser sysUser);
      List<Map<String, Object>> queryAllUsers();
      List<String> queryUserIdByDepId(String depId);

      //TODO 了解下 left join  包含where的情况
      // select a.* from (sys_user a where a.state = 1) left join sys_user_depart b on a.id=b.user_id  where  depart_id = #{depId}
      List<SysUser> queryUsersByDepId(@Param("depId") String depId);

      @Select("select * from sys_user ")
      List<SysUser> list ();

      @Update("update sys_user set `login_time`=#{loginTime} where id=#{id}")
      void updateLoginTimeById(@Param("loginTime") Date loginTime,@Param("id") String id);

      @Delete("delete from sys_user where id=#{id}")
      void deleteById(String id);
}
