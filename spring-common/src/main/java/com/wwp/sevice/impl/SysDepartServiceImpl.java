package com.wwp.sevice.impl;


import com.wwp.sevice.ISysDepartService;
import com.wwp.sevice.ISysUserService;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.FindsDepartsChildrenUtil;
import com.wwp.common.util.PinyinUtils;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.DepartIdModel;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysDepartTreeModel;
import com.wwp.mapper.SysDepartMapper;
import com.wwp.mapper.SysUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22
 */
@Service
public class SysDepartServiceImpl implements ISysDepartService {


	@Resource
	SysUserMapper sysUserMapper;
	@Resource
	SysDepartMapper sysDepartMapper;
	@Resource
    ISysUserService sysUserService;

	/**
	 * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
	 */
	@Override
	public List<DepartIdModel> queryTreeList(SysDepart depart) {
		Integer parentCategory = 4;
		List<SysDepart> list;
		List<SysDepart>  parentList;
		if(depart != null) {
			parentCategory = depart.getOrgCategory() + 1;
			list = queryChildDepartById(depart.getId());
			list.add(depart);
			parentList = queryParentDepartById(depart.getId());//直线上顶层到集团
			if(parentList.size()>0){
				list.addAll(parentList);
			}
		} else {
			list = queryAllDeparts();
		}

		List<DepartIdModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToDepartIdTreeList(list, parentCategory);
		return listResult;
	}

	@Override
	public List<DepartIdModel> queryDepartIdTreeList() {
		List<SysDepart> list = queryAllDeparts();
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<DepartIdModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToDepartIdTreeList(list, 1);
		return listResult;
	}

	@Override
	public SysDepart queryDepartById(String id){
		return sysDepartMapper.queryDepartById(id);
	}

	@Override
	public boolean updateDepart(SysDepart depart)
	{
		return  sysDepartMapper.update(depart);
	}
	/**
	 * saveDepartData 对应 add 保存用户在页面添加的新的部门对象数据
	 */
	@Override
	@Transactional
	public void saveDepartData(SysDepart sysDepart, String username) {
		if (sysDepart != null && username != null) {
			if (sysDepart.getParentId() == null) {
				sysDepart.setParentId("");
			}


			if(sysDepart.getPayChannel() == null) {
				sysDepart.setPayChannel(0);
			}
			//生成支付编码
//			sysDepart.setPayCode(createPayCode(sysDepart.getPayChannel()));
			sysDepartMapper.save(sysDepart);
		}

	}

	private String createPayCode(Integer payType) {
		//LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
		//query.eq(SysDepart::getOrgCategory, CommonConstant.DEPART_TYPE_PROJ);
		int count = 123;//count(query);
		return payType.toString() + (10000 + count);
	}


	/**
	 * updateDepartDataById 对应 edit 根据部门主键来更新对应的部门数据
	 */

	
	@Override
	@Transactional
	public void deleteBatchWithChildren(List<String> ids) {
		List<String> idList = new ArrayList<String>();
		for(String id: ids) {
			idList.add(id);
			this.checkChildrenExists(id, idList);
		}
		sysDepartMapper.removeByIds(idList);

	}

    @Override
    public List<SysDepart> queryChildDepartById(String id) {

		List<SysDepart> list = new ArrayList<SysDepart>();
		getChildDepartById(id,list);
        return list;
    }

	@Override
	public List<SysDepart> queryChildParkById(String id) {
		List<SysDepart> list = new ArrayList<SysDepart>();
		getChildDepartById(id,list);
		Iterator<SysDepart> iterator = list.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next().getOrgCategory()!=4)
				iterator.remove();
		}
		return list;
	}


	@Override
	public List<SysDepart> queryChildDepartByIds(String ids) {
		return sysDepartMapper.queryChildParkByIds(ids);
	}


	@Override
	public List<SysDepart> queryChildAllPark() {
		return sysDepartMapper.queryChildAllPark();
	}


	@Override
	public List<SysDepart> queryAllDeparts() {
		return sysDepartMapper.queryAllDeparts();
	}

    @Override
    public List<SysDepart> queryParentDepartById(String id) {
		List<SysDepart> list = new ArrayList<SysDepart>();
		SysDepart depart = sysDepartMapper.queryDepartById(id);
        getParentDepartById(depart,list);
        return list;
    }

	@Override
	public SysDepart queryOneByParentIdAndName(String parentId, String name) {
		Map<String, Object> map=new HashMap<>();
		map.put("parentId",parentId);
		map.put("departName",name);
		return sysDepartMapper.queryOneByParentIdAndName(map);
	}

	/**
	 * <p>
	 * 根据关键字搜索相关的部门数据
	 * </p>
	 */
	@Override
	public List<SysDepartTreeModel> searhBy(String keyWord) {
		SysDepartTreeModel model = new SysDepartTreeModel();
		List<SysDepart> departList = sysDepartMapper.queryDepartsByNamePy(keyWord);
		List<SysDepartTreeModel> newList = new ArrayList<>();
		if(departList.size() > 0) {
			for(SysDepart depart : departList) {
				model = new SysDepartTreeModel(depart);
				model.setChildren(null);
	    //update-end--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索功回显优化----------------------
				newList.add(model);
			}
			return newList;
		}
		return null;
	}

	/**
	 * 根据部门id删除并且删除其可能存在的子级任何部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delete(String id) {
		List<String> idList = new ArrayList<>();
//		idList.add(id);
		this.checkChildrenExists(id, idList);
		if(idList.size()>0) {
			throw new CustomException("请先删除此机构的子机构");
		}
		SysDepart sysDepart = sysDepartMapper.queryDepartById(id);
		if(sysDepart.getOrgCategory() == 4) { //项目

			//ZybParkInfo,ZybGroup里面有depId，需要判定
			//throw new IllegalArgumentException("请先删除此项目下的的车场");
		}
		//删除此机构下的账号
		//查找这个机构下的用户
		List<String> userIdList = sysUserService.queryUserIdByDepId(id);
		if(userIdList.size()>0) {
			//sysUserMapper.deleteBatchIds(userIdList);
			throw new CustomException("要不要把用户也删除了??");
		}
		return sysDepartMapper.removeById(id);
	}

	/**
	 * 根据用户查询机构列表
	 * @param page  分页
	 * @param orgCategory 机构类型
	 * @return
	 */
	@Override
	public List<SysDepart> queryDepartsByIdList(Integer orgCategory, String departName, List<String> idList) {
		if(idList.size()>0) idList = null;
		String departNamePy = PinyinUtils.getPinYinHeadChar(departName);

		Map<String, Object> map=new HashMap<>();
		map.put("departName",departName);
		map.put("departNamePy",departNamePy);
		map.put("orgCategory",orgCategory);
		map.put("idList",idList);
		return sysDepartMapper.queryDepartsByIdList(map);
	}


	/**
	 * delete 方法调用
	 * @param id
	 * @param idList
	 */
	private void checkChildrenExists(String id, List<String> idList) {	
		//LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
		//query.eq(SysDepart::getParentId,id);
		List<SysDepart> departList = sysDepartMapper.queryDepartsByParentId(id);
		if(departList != null && departList.size() > 0) {
			for(SysDepart depart : departList) {
				idList.add(depart.getId());
				this.checkChildrenExists(depart.getId(), idList);
			}
		}
	}


	private void getChildDepartById(String id, List<SysDepart> list) {
		//LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
		//query.eq(SysDepart::getParentId,id);
		List<SysDepart> departList = sysDepartMapper.queryDepartsByParentId(id);
		if(departList != null && departList.size() > 0) {
			for(SysDepart depart : departList) {
				list.add(depart);
				this.getChildDepartById(depart.getId(), list);
			}
		}
	}

	private void getParentDepartById(SysDepart depart, List<SysDepart> list) {
		//LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
		//query.eq(SysDepart::getParentId,id);
		SysDepart parentDepart;

		if(depart!=null&&!oConvertUtils.isEmpty(depart.getParentId())) {
			parentDepart = sysDepartMapper.queryDepartById(depart.getParentId());
			if (parentDepart != null)
				list.add(parentDepart);
			this.getParentDepartById(parentDepart, list);
		}
	}



	@Override
	public List<SysDepart> queryUserDeparts(String userId) {
		return sysDepartMapper.queryUserDeparts(userId);
	}

	@Override
	public List<SysDepart> queryDepartsByUsername(String username) {
		return sysDepartMapper.queryDepartsByUsername(username);
	}

}
