package com.wwp.mapper;


import com.wwp.entity.SysUserDepart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserDepartMapper {
	
	List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);

	void save(SysUserDepart sysUserDepart);

	@Delete("delete from sys_user_depart where user_id=#{userId}")
	void deleteByUserId(String userId);
}
