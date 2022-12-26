package com.wwp.mapper;


import com.wwp.entity.SysUserDepart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserDepartMapper {
	
	List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);

	void save(@Param("userId") String userId ,@Param("depart_id") String departId);
}
