package com.wwp.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;
import com.wwp.entity.SysDepart;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 * 
 * @Author: Steve
 * @Since：   2019-01-22
 */
public interface SysDepartMapper {


	public void save(SysDepart sysDepart);
	public void removeByIds(List<String> ids);

	@Delete("delete from sys_depart where id =#{id}")
	public boolean removeById(String id);
	/**
	 * 根据用户ID查询部门集合
	 */
	public List<SysDepart> queryUserDeparts(@Param("userId") String userId);

	/**
	 * 根据用户名查询部门
	 *
	 * @param username
	 * @return
	 */
	public List<SysDepart> queryDepartsByUsername(@Param("username") String username);

	@Select("select id from sys_depart where org_code=#{orgCode} and del_flag='0'")
	public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

	@Select("select id,parent_id from sys_depart where id=#{departId} and del_flag='0'")
	public SysDepart getParentDepartId(@Param("departId") String departId);

	@Select("select * from sys_depart where parent_id=#{parentId} and del_flag='0'")
	public List<SysDepart>  queryDepartsByParentId (@Param("parentId") String parentId);

	@Select("select * from sys_depart where id=#{id}")
	public SysDepart  queryDepartById (@Param("id") String id);

	public boolean update(SysDepart depart);

	List<SysDepart> queryDepartsByIdList(Map<String, Object> map);

	//@Select("select * from sys_depart where FIND_IN_SET(id,getChildList(#{id})) and del_flag='0'")
	//List<SysDepart> queryChildDepartById(@Param("id") String id);

	//@Select("select * from sys_depart where FIND_IN_SET(id,getChildList(#{id})) and del_flag='0' and org_category=4 ")
	//List<SysDepart> queryChildParkById(@Param("id") String id);

	@Select("select * from sys_depart where parent_id in (#{ids}) and del_flag='0'")
	List<SysDepart> queryChildParkByIds(@Param("ids") String ids);

	@Select("select * from sys_depart where  del_flag='0' and org_category=4 ")
	List<SysDepart> queryChildAllPark();

	@Select("select * from sys_depart where  del_flag='0' ")
	List<SysDepart> queryAllDeparts();

	//@Select("select * from sys_depart where FIND_IN_SET(id,getParentList(#{id})) and id <> #{id} and del_flag='0'")
	//List<SysDepart> queryParentDepartById(@Param("id") String id);

	SysDepart queryOneByParentIdAndName(Map<String, Object> map);

	List<SysDepart> queryDepartsByNamePy(String departNamePy);

}
