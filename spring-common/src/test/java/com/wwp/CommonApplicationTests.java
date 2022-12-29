package com.wwp;

import com.wwp.common.aspect.annotation.AutoLog;
import com.wwp.common.util.JwtUtil;
import com.wwp.common.util.RedisUtil;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysDepartTreeModel;
import com.wwp.entity.SysUser;
import com.wwp.mapper.SysPermissionMapper;
import com.wwp.mapper.SysUserMapper;
import com.wwp.mapper.SysUserRoleMapper;
import com.wwp.model.TreeModel;
import com.wwp.sevice.ISysDepartService;
import com.wwp.sevice.ISysPermissionService;
import com.wwp.sevice.ISysUserRoleService;
import com.wwp.sevice.impl.SysLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages={"com.wwp.common.aspect.annotation"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes= CommonApplication.class)
public class CommonApplicationTests {

	@Autowired
	private ISysUserRoleService sysUserRoleService;

	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private ISysDepartService sysDepartService;

	@Resource
	private SysLogService sysLogService;

	@Autowired
	private SysPermissionMapper sysPermissionMapper;

	@Autowired
	private RedisUtil redisUtil;

	@AutoLog(value = "jojo")
	public void contextLoads() throws Exception {

		SysUser s = sysUserMapper.getUserByUsername("admin");
		List<SysUser> list = sysUserMapper.list();
	}


	public void getDeparts() throws Exception{
		List<SysDepart> departs = sysDepartService.queryUserDeparts("17");

		SysDepart depart =  (departs == null || departs.isEmpty()) ? null:departs.get(1);
		List<SysDepartTreeModel> list = sysDepartService.queryTreeList(depart);

		for(SysDepartTreeModel departModel:list){
			System.out.println(departModel.getDepartName());
		}
	}

	@Test
	public void testRedis() throws Exception{
		//redisUtil.set("1234","hello");
		redisUtil.expire("1234", 100);

		//System.out.println(redisUtil.get("123"));
	}

}
