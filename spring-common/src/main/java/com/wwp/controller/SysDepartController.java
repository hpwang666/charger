package com.wwp.controller;

import com.alibaba.fastjson.JSONObject;
import com.wwp.common.constant.CommonConstant;
import com.wwp.common.util.PinyinUtils;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.DepartIdModel;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysDepartTreeModel;
import com.wwp.entity.SysUser;
import com.wwp.sevice.ISysDepartService;
import com.wwp.vo.Result;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 前端控制器
 * <p>
 * 
 * @Author: Steve @Since： 2019-01-22
 */
@RestController
@RequestMapping("/sys/depart")


public class SysDepartController {

	@Autowired
	private ISysDepartService sysDepartService;

	/**
	 * 查询数据 查出所有部门,并以树结构数据格式响应给前端
	 * 
	 * @return
	 */

	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<JSONObject> queryTreeList()throws Exception {
		Result<JSONObject> result = new Result<>();
		SysUser sysUser = new SysUser();
		PropertyUtils.copyProperties(sysUser,SecurityUtils.getSubject().getPrincipal());

		List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
		//超级管理员是没有部门的 为null
		SysDepart depart =  (departs == null || departs.isEmpty()) ? null:departs.get(0);
		try {
			List<DepartIdModel> list = sysDepartService.queryUserDepartIdList(depart);
			JSONObject obj = new JSONObject();
			obj.put("departTree",list);
			result.setResult(obj);
			result.setCode(200);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(500);
			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 添加新数据 添加用户新建的部门对象数据,并保存到数据库
	 * @param sysDepart  机构
	 * @return
	 */

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<?> add(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
		if(oConvertUtils.isEmpty(sysDepart.getDepartName())) {
			return Result.error("请传参数：departName");
		}

		if(oConvertUtils.isEmpty(sysDepart.getParentId()))  {

			sysDepart.setOrgCategory(1);//城市顶级部门

		}
		else{
			SysDepart parentDepart = sysDepartService.queryDepartById(sysDepart.getParentId());
			if(parentDepart.getOrgCategory()==4) return Result.error(parentDepart.getDepartName() + "不应该有子部门");
			else {
				sysDepart.setOrgCategory(parentDepart.getOrgCategory()+1);
			}
		}
		SysDepart existDepart = sysDepartService.queryOneByParentIdAndName(sysDepart.getParentId(), sysDepart.getDepartName());
		if(existDepart != null) return Result.error(sysDepart.getDepartName() + "已存在");

		Result<SysDepart> result = Result.OK();
		//String username = JwtUtil.getUserNameByToken(request);
		String departNamePy = PinyinUtils.getPinYinHeadChar(sysDepart.getDepartName());
		sysDepart.setDepartNamePy(departNamePy);
		try {
			sysDepart.setDelFlag("0");
			sysDepart.setState(1);
			sysDepartService.saveDepartData(sysDepart);
			result.success200("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 添加新数据 添加用户新建的部门对象数据,并保存到数据库
	 * @param orgCategory  机构类型
	 * @param pageNo 页数
	 * @param pageSize 条数
	 * @return
	 */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "orgCategory", value = "机构类型", dataTypeClass = Integer.class, paramType = "query", required = true),
//			@ApiImplicitParam(name = "departName", value = "机构名称", dataTypeClass = Integer.class, paramType = "query"),
//			@ApiImplicitParam(name = "cityId", value = "城市id", dataTypeClass = Integer.class, paramType = "query"),
//			@ApiImplicitParam(name = "groupId", value = "集团id", dataTypeClass = Integer.class, paramType = "query"),
//			@ApiImplicitParam(name = "companyId", value = "公司id", dataTypeClass = Integer.class, paramType = "query"),
//			@ApiImplicitParam(name = "pageNo", value = "当前页数", dataTypeClass = Integer.class, defaultValue = "1", paramType = "query"),
//			@ApiImplicitParam(name = "pageSize", value = "一页条数", dataTypeClass = Integer.class, paramType = "query")
//	})
	public Result<?> list(@RequestParam Integer orgCategory, @RequestParam(required = false) String departName, @RequestParam(required = false) String cityId,
                                        @RequestParam(required = false) String groupId, @RequestParam(required = false) String companyId, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNo) {
		SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
		if(orgCategory == null) return Result.error("请输入参数:orgCategory");
		if(pageNo == null || pageNo <= 0) pageNo = 1;
		if(pageSize == null || pageSize <= 0) pageSize = Integer.MAX_VALUE;



		List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());


	//	if(CollectionUtil.isEmpty(departs)&&!sysUser.getId().equals("e9ca23d68d884d4ebb19d07889727dae")){
	//		return Result.OK();
	//	}

		if(departs!=null && !departs.isEmpty()) {
			SysDepart sysDepart = departs.get(0);
			int orgType = sysDepart.getOrgCategory();
			if(orgType == CommonConstant.DEPART_TYPE_CITY && oConvertUtils.isEmpty(cityId)) {
				cityId = sysDepart.getId();
			} else if(orgType == CommonConstant.DEPART_TYPE_GROUP && oConvertUtils.isEmpty(groupId)) {
				groupId = sysDepart.getId();
			} else if(orgType == CommonConstant.DEPART_TYPE_COMPANY && oConvertUtils.isEmpty(companyId)) {
				companyId = sysDepart.getId();
			}else if(orgType == CommonConstant.DEPART_TYPE_PROJ ) {
				return Result.OK("查询成功！",departs);
			}else{
				//return Result.ok();
			}
		}



		List<SysDepart> sysDepartList = new ArrayList<>();
		try {
			if(oConvertUtils.isNotEmpty(cityId)) {
				List<SysDepart> list = sysDepartService.queryChildDepartById(cityId);
				if(!(list == null || list.isEmpty())) sysDepartList.addAll(list);
			}else if(oConvertUtils.isNotEmpty(groupId)) {
				sysDepartList.clear();
				List<SysDepart> list = sysDepartService.queryChildDepartById(groupId);
				if(!(list == null || list.isEmpty())) sysDepartList.addAll(list);
			}else if(oConvertUtils.isNotEmpty(companyId)) {
				sysDepartList.clear();
				List<SysDepart> list = sysDepartService.queryChildDepartById(companyId);
				if(!(list == null || list.isEmpty())) sysDepartList.addAll(list);
			}

			List<String> idList = sysDepartList.stream().map(SysDepart::getId).collect(Collectors.toList());
			List<SysDepart> records = sysDepartService.queryDepartsByIdList(orgCategory, departName, idList);
			//List<SysDepart> records = sysDepartPage.getRecords();
			for (SysDepart sysDepart : records) {
				List<SysDepart> parentList = sysDepartService.queryParentDepartById(sysDepart.getId());
				//这里暂时没有启用parentList
				//sysDepart.setParentList(parentList);
			}
			return Result.OK("查询成功！",records);
		} catch (Exception e) {
			return Result.error("查询失败",e.getMessage());
		}
	}



	@RequestMapping(value = "/parklist", method = RequestMethod.GET)
	public Result<List<SysDepart>> parklist() {
		Result<List<SysDepart>> result = new Result();
		SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();

		if(sysUser.getId().equals("e9ca23d68d884d4ebb19d07889727dae")){
			List<SysDepart> list = sysDepartService.queryChildAllPark();
			result.setResult(list);
			result.success200("查询成功！");
		}else{
			List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
			if(departs==null || departs.isEmpty()){
				return Result.OK();
			}
			try {
				SysDepart sysDepart = departs.get(0);
				int orgType = sysDepart.getOrgCategory();

				if(orgType == CommonConstant.DEPART_TYPE_PROJ ){
					result.setResult(departs);
					return result;
				}

				List<SysDepart> list = sysDepartService.queryChildDepartByIds(sysDepart.getId());

				List<String> idList = list.stream().map(SysDepart::getId).collect(Collectors.toList());

				if(orgType == CommonConstant.DEPART_TYPE_CITY ) {
					List<SysDepart> s0 = sysDepartService.queryDepartsByIdList(2, null, idList);
					List<String> r1 = s0.stream().map(SysDepart::getId).collect(Collectors.toList());
					List<SysDepart> s1 = sysDepartService.queryDepartsByIdList(3, null, r1);
					List<String> r2 = s1.stream().map(SysDepart::getId).collect(Collectors.toList());
					List<SysDepart> s2 = sysDepartService.queryDepartsByIdList( 4, null, r2);
					result.setResult(s2);
					return result;
				}

				if(orgType == CommonConstant.DEPART_TYPE_GROUP ) {
					List<SysDepart> records = sysDepartService.queryDepartsByIdList( 3, null, idList);
					List<String> r1 = records.stream().map(SysDepart::getId).collect(Collectors.toList());
					List<SysDepart> s1 = sysDepartService.queryDepartsByIdList(4, null, r1);
					result.setResult(s1);
					return result;
				}

				if(orgType == CommonConstant.DEPART_TYPE_COMPANY) {
					List<SysDepart> records = sysDepartService.queryDepartsByIdList( 4, null, idList);
					result.setResult(records);
					return result;
				}

			} catch (Exception e) {
				result.error500("查询失败"+e.getMessage());
			}
		}
		return result;
	}






	/**
	 * 编辑数据 编辑部门的部分数据,并保存到数据库
	 * 
	 * @param sysDepart
	 * @return
	 */

	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<?> edit(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
		String id = sysDepart.getId();
		if(oConvertUtils.isEmpty(id)) return Result.error("请传参数:id");
		SysDepart depart = sysDepartService.queryDepartById(id);
		if(depart == null) return Result.error("机构不存在");

		String oldDepartName = depart.getDepartName();
		String oldParentId = depart.getParentId();
		String newDepartName = sysDepart.getDepartName();
		String newParentId = sysDepart.getParentId();
		boolean isDepartNameChange = oConvertUtils.isNotEmpty(newDepartName)
				&& !oldDepartName.equals(newDepartName);
		if(isDepartNameChange) depart.setDepartName(newDepartName);

		boolean isParentIdChange = oConvertUtils.isNotEmpty(newParentId)
				&& !oldParentId.equals(newParentId);
		if(isParentIdChange) depart.setParentId(newParentId);

		if(isDepartNameChange || isParentIdChange) {  //不一样则判断
			SysDepart existDepart = sysDepartService.queryOneByParentIdAndName(newParentId, newDepartName);
			if(existDepart != null) return Result.error(newDepartName + "已存在");
		}

		if(oConvertUtils.isNotEmpty(sysDepart.getDepartNameAbbr())) {
			depart.setDepartNameAbbr(sysDepart.getDepartNameAbbr());
		}
		if(oConvertUtils.isNotEmpty(sysDepart.getMemo())) {
			depart.setMemo(sysDepart.getMemo());
		}
		String departNamePy = PinyinUtils.getPinYinHeadChar(depart.getDepartName());
		depart.setDepartNamePy(departNamePy);
		if(sysDepart.getPayChannel() != null) {
			depart.setPayChannel(sysDepart.getPayChannel());
		}
		if(oConvertUtils.isNotEmpty(sysDepart.getPayCode())) {
			depart.setPayCode(sysDepart.getPayCode());
		}
		sysDepartService.updateDepart(depart);
		return Result.OK("修改成功");
	}
	
	 /**
     *   通过id删除
    * @param id id
    * @return
    */

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
   public Result delete(@RequestParam(name="id") String id) {

//       SysDepart sysDepart = sysDepartService.getById(id);
		 try {
			 sysDepartService.delete(id);
			 return Result.OK("删除成功!");
		 } catch (Exception e) {
		 	return Result.error(e.getMessage());
		 }
   }


	/**
	 * 批量删除 根据前端请求的多个ID,对数据库执行删除相关部门数据的操作
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<SysDepart> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

		Result<SysDepart> result = new Result<SysDepart>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			this.sysDepartService.deleteBatchWithChildren(Arrays.asList(ids.split(",")));
			result.success200("删除成功!");
		}
		return result;
	}

	/**
	 * 查询数据 添加或编辑页面对该方法发起请求,以树结构形式加载所有部门的名称,方便用户的操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryIdTree", method = RequestMethod.GET)
	public Result<List<DepartIdModel>> queryIdTree() {
//		Result<List<DepartIdModel>> result = new Result<List<DepartIdModel>>();
//		List<DepartIdModel> idList;
//		try {
//			idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
//			if (idList != null && idList.size() > 0) {
//				result.setResult(idList);
//				result.setSuccess(true);
//			} else {
//				sysDepartService.queryTreeList();
//				idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
//				result.setResult(idList);
//				result.setSuccess(true);
//			}
//			return result;
//		} catch (Exception e) {
//			log.error(e.getMessage(),e);
//			result.setSuccess(false);
//			return result;
//		}
		Result<List<DepartIdModel>> result = new Result<>();
		try {
			List<DepartIdModel> list = sysDepartService.queryAllDepartIdList();
			result.setResult(list);
			result.setSuccess(true);
		} catch (Exception e) {
			//log.error(e.getMessage(),e);
			result.error500(e.getMessage());
		}
		return result;
	}
	 
	/**
	 * <p>
	 * 部门搜索功能方法,根据关键字模糊搜索相关部门
	 * </p>
	 * 
	 * @param keyWord
	 * @return
	 */
	@RequestMapping(value = "/searchBy", method = RequestMethod.GET)
	public Result<List<SysDepartTreeModel>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord) {
		Result<List<SysDepartTreeModel>> result = new Result<List<SysDepartTreeModel>>();
		try {
			List<SysDepartTreeModel> treeList = this.sysDepartService.searhBy(keyWord);
			if (treeList.size() == 0 || treeList == null) {
				throw new Exception();
			}
			result.setSuccess(true);
			result.setResult(treeList);
			return result;
		} catch (Exception e) {
			e.fillInStackTrace();
			result.setSuccess(false);
			result.setMessage("查询失败或没有您想要的任何数据!");
			return result;
		}
	}

}
