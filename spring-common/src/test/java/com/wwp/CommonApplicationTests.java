package com.wwp;

import com.wwp.common.aspect.annotation.AutoLog;
import com.wwp.entity.SysUser;
import com.wwp.mapper.SysPermissionMapper;
import com.wwp.mapper.SysUserMapper;
import com.wwp.mapper.SysUserRoleMapper;
import com.wwp.model.TreeModel;
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
	private ISysPermissionService sysPermissionService;

	@Resource
	private SysLogService sysLogService;

	@Autowired
	private SysPermissionMapper sysPermissionMapper;

	@Test
	@AutoLog(value = "jojo")
	public void contextLoads() throws Exception {

		SysUser s = sysUserMapper.getUserByUsername("admin");
		List<SysUser> list = sysUserMapper.list();
	}

}
