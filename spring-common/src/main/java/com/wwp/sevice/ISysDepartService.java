package com.wwp.sevice;

import com.wwp.entity.DepartIdModel;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysDepartTreeModel;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @Author:Steve
 * @Since：   2019-01-22
 */
public interface ISysDepartService {

    /**
     * 根据用户的部门查出所以子部门，以及相关的单线父级部门
     * */
    List<SysDepart> queryUserDepartIdList(SysDepart depart);

    /**
     * 查询所有部门DepartId信息,并分节点进行显示
     * @return
     */
     List<DepartIdModel> queryAllDepartIdList();

    SysDepart queryDepartById(String id);

    boolean updateDepart(SysDepart depart);

    /**
     * 保存部门数据
     * @param sysDepart
     */
    void saveDepartData(SysDepart sysDepart);

    /**
     * 删除depart数据
     * @param id
     * @return
     */
	/* boolean removeDepartDataById(String id); */
    
    /**
     * 根据关键字搜索相关的部门数据
     * @param keyWord
     * @return
     */
    List<SysDepartTreeModel> searhBy(String keyWord);
    
    /**
     * 根据部门id删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
    boolean delete(String id);

    List<SysDepart> queryDepartsByIdList( Integer orgCategory, String departName, List<String> idList);
    
    /**
     * 查询SysDepart集合
     * @param userId
     * @return
     */
	 List<SysDepart> queryUserDeparts(String userId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    List<SysDepart> queryDepartsByUsername(String username);

	 /**
     * 根据部门id批量删除并删除其可能存在的子级部门
     * @param ids
     * @return
     */
	void deleteBatchWithChildren(List<String> ids);

	List<SysDepart> queryChildDepartById(String id);

    List<SysDepart> queryChildParkById(String id);

    List<SysDepart> queryChildAllPark();

    List<SysDepart> queryAllDeparts();

	List<SysDepart> queryParentDepartById(String id);

	SysDepart queryOneByParentIdAndName(String parentId, String name);

	List<SysDepart> queryChildDepartByIds(String ids);
}
