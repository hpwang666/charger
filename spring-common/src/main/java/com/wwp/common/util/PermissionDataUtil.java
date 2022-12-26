package com.wwp.common.util;


import com.wwp.entity.SysPermission;

/**
 * @Author: scott
 * @Date: 2019-04-03
 */
public class PermissionDataUtil {

	/**
	 * 智能处理错误数据，简化用户失误操作
	 * 
	 * @param permission
	 */
	public static SysPermission intelligentProcessData(SysPermission permission) {
		if (permission == null) {
			return null;
		}

		// 组件
		if (oConvertUtils.isNotEmpty(permission.getComponent())) {
			String component = permission.getComponent();
			if (component.startsWith("/")) {
				component = component.substring(1);
			}
			if (component.startsWith("views/")) {
				component = component.replaceFirst("views/", "");
			}
			if (component.startsWith("src/views/")) {
				component = component.replaceFirst("src/views/", "");
			}
			if (component.endsWith(".vue")) {
				component = component.replace(".vue", "");
			}
			permission.setComponent(component);
		}
		
		// 请求URL
		if (oConvertUtils.isNotEmpty(permission.getUrl())) {
			String url = permission.getUrl();
			if (url.endsWith(".vue")) {
				url = url.replace(".vue", "");
			}
			if (!url.startsWith("http") && !url.startsWith("/")&&!url.trim().startsWith("{{")) {
				url = "/" + url;
			}
			permission.setUrl(url);
		}
		
		// 一级菜单默认组件
		if (permission.getResourceType().equals("menu0")&& oConvertUtils.isEmpty(permission.getComponent())) {
			// 一级菜单默认组件
			permission.setComponent("layouts/RouteView");
		}
		return permission;
	}
	
}
