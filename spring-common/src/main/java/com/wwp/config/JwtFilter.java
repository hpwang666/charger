package com.wwp.config;

import com.wwp.common.exception.CustomException;
import com.wwp.common.util.oConvertUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 鉴权登录拦截器
 * @Author: Scott
 * @Date: 2018/10/7
 **/

public class JwtFilter extends BasicHttpAuthenticationFilter {

	/**
	 * 执行登录认证
	 *
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 */


	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)  {
		try {
			executeLogin(request, response);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 *
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

		System.out.println("开始登录啦");
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader("X-Access-Token");
		if(oConvertUtils.isEmpty(token)) 	throw new CustomException("token is empty");
		JwtToken jwtToken = new JwtToken(token);
		getSubject(request, response).login(jwtToken);
		// 如果没有抛出异常则代表登入成功，返回true
		return true;
		//return getSubject(request, response).isAuthenticated();
	}
	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		System.out.println("当 isAccessAllowed 返回 false 的时候，才会执行 method onAccessDenied ");

		HttpServletResponse httpServletResponse = WebUtils.toHttp(servletResponse);
		httpServletResponse.setStatus(HttpStatus.OK.value());
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "token");
		//解决低危漏洞点击劫持 X-Frame-Options Header未配置
		httpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json; charset=utf-8");

		httpServletResponse.getWriter().write("{\"code\":\"401\",\"error\":\"用户未登录\"}");
		return false;
	}
	/**
	 * 对跨域提供支持
	 */

}
