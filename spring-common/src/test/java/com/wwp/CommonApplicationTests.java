package com.wwp;

import com.wwp.common.aspect.annotation.AutoLog;
import com.wwp.common.util.JwtUtil;
import com.wwp.common.util.RedisUtil;
import com.wwp.entity.DepartIdModel;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysUser;
import com.wwp.mapper.SysPermissionMapper;
import com.wwp.mapper.SysUserMapper;
import com.wwp.sevice.ISysDepartService;
import com.wwp.sevice.ISysRoleService;
import com.wwp.sevice.ISysUserRoleService;
import com.wwp.sevice.impl.SysLogService;
import com.wwp.sevice.impl.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@ComponentScan(basePackages={"com.wwp.common.aspect.annotation"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes= CommonApplication.class)
@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class CommonApplicationTests {

	@Autowired
	private ISysUserRoleService sysUserRoleService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private ISysDepartService sysDepartService;

	@Resource
	private SysLogService sysLogService;

	@Autowired
	private SysPermissionMapper sysPermissionMapper;

	@Resource
	private RedisUtil  redisUtil;

	@Resource
	private ISysRoleService sysRoleService;




	public void testRedis() throws Exception{


		String token = redisUtil.get("prefix_user_token_admin").toString();
		//redisUtil.expire("1234", 100);

		System.out.println(token);
		//System.out.println(JwtUtil.verify(token, "love", "f2efaa3e834261a5ae4bac1123fba47b"));

	}

	@Test
	public void testRoles()
	{
		System.out.println(sysUserService.queryUserIdByDepId("04bc433d888d4cee92098d718e06d972"));
	}





}
