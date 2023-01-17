package com.wwp.mapper;

import com.wwp.entity.SysUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface SysUserMapper {
      SysUser getUserByAccount(String account);
      void reg(SysUser sysUser);
      List<Map<String, Object>> queryAllUsers();
      List<String> queryUserIdByDepId(String depId);

      @Select("select * from sys_user ")
      List<SysUser> list ();
}
