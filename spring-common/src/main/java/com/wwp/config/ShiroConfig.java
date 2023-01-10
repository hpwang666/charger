package com.wwp.config;

import com.wwp.common.util.oConvertUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfig {
	@Value("${spring.redis.port}")
	private String port;

	@Value("${spring.redis.host}")
	private String host;

	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		System.out.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//拦截器.
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/logout", "anon");
		filterChainDefinitionMap.put("/dev/regionDev", "authc");
		filterChainDefinitionMap.put("/dev/**", "anon");

		Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
		filterMap.put("jwt", new JwtFilter());
		shiroFilterFactoryBean.setFilters(filterMap);

		filterChainDefinitionMap.put("/**", "jwt");

		//配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		//<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;

		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/403");


		// 登录成功后要跳转的链接
		//shiroFilterFactoryBean.setSuccessUrl("/index");

		//未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 凭证匹配器
	 * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * ）
	 * @return
	 */


	@Bean
	public MyShiroRealm myShiroRealm(){
		MyShiroRealm myShiroRealm = new MyShiroRealm();

		//myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		myShiroRealm.setCachingEnabled(true);
		// 开启身份验证缓存，即缓存AuthenticationInfo信息
		myShiroRealm.setAuthenticationCachingEnabled(true);
		// 设置身份缓存名称前缀
		myShiroRealm.setAuthenticationCacheName("authenticationCache");
		// 开启授权缓存
		myShiroRealm.setAuthorizationCachingEnabled(true);
		// 这是权限缓存名称前缀
		myShiroRealm.setAuthorizationCacheName("authorizationCache");



		return myShiroRealm;
	}


	@Bean
	public SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());


		//关闭shiro自带的session
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);

		//禁用session
		securityManager.setSubjectFactory(new StatelessDefaultSubjectFactory());

		//自定义缓存实现,使用redis
		securityManager.setCacheManager(redisCacheManager());
		securityManager.setSessionManager(sessionManager());

		return securityManager;
	}


	/**
	 *  开启shiro aop注解支持.
	 *  使用代理方式;所以需要开启代码支持;
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}



	@Bean(name = "lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}

	public RedisCacheManager redisCacheManager() {
		System.out.println("===============(1)创建缓存管理器RedisCacheManager");
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		//redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
		redisCacheManager.setPrincipalIdFieldName("username");
		//用户权限信息缓存时间
		redisCacheManager.setExpire(43200);//只有token有效期的一半 12小时
		return redisCacheManager;
	}

	/**
	 * 配置shiro redisManager
	 * 使用的是shiro-redis开源插件
	 *
	 * @return
	 */
	@Bean
	public RedisManager redisManager() {
		System.out.println("===============(2)创建RedisManager,连接Redis..URL= " + host + ":" + port);
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(host);
		redisManager.setPort(oConvertUtils.getInt(port));
		redisManager.setTimeout(0);
//		if (!StringUtils.isEmpty(redisPassword)) {
//			redisManager.setPassword(redisPassword)/**/;
//		}
		return redisManager;
	}

	@Bean
	public SessionManager sessionManager() {
		//把session存入到redis  前提需要使能session的各种
		//ShiroSessionManager sessionManager = new ShiroSessionManager();
		//sessionManager.setSessionDAO(redisSessionDAO());

		DefaultSessionManager shiroSessionManager = new DefaultSessionManager();
		// 关闭session校验轮询
		shiroSessionManager.setSessionValidationSchedulerEnabled(false);
		return shiroSessionManager;
	}

	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		// 设置缓存名前缀
		redisSessionDAO.setKeyPrefix("shiro:session:");
		return redisSessionDAO;
	}




}