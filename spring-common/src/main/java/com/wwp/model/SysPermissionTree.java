package com.wwp.model;


import com.wwp.entity.SysPermission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SysPermissionTree implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;

	private String key;
	private String title;

	/**
	 * 父id
	 */
	private String parentId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单权限编码
	 */
	private String permission;


	/**
	 * 组件
	 */
	private String component;

	/**
	 * 跳转网页链接
	 */
	private String url;

	/**
	 * 菜单排序
	 */
	private Double sortNo;

	/**
	 * 类型（menu0：一级菜单；menu1：子菜单 ；button：按钮权限）
	 */
	private String resourceType;

	/**
	 * 是否叶子节点: 1:是 0:不是
	 */
	private boolean isLeaf;

	/**
	 * 删除状态 0正常 1已删除
	 */
	private boolean delFlag;


	public SysPermissionTree() {
	}

	public SysPermissionTree(SysPermission permission) {
		this.key = permission.getId();
		this.id = permission.getId();
		this.permission = permission.getPermission();
		this.component = permission.getComponent();
		this.delFlag = permission.getDelFlag();
		this.isLeaf = permission.getIsLeaf();
		this.resourceType = permission.getResourceType();
		this.name = permission.getName();
		this.parentId = permission.getParentId();
		this.sortNo = permission.getSortNo();
		this.url = permission.getUrl();


		/*update_end author:wuxianquan date:20190908 for:赋值 */
		this.title=permission.getName();
		if (!permission.getIsLeaf()) {
			this.children = new ArrayList<SysPermissionTree>();
		}

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private List<SysPermissionTree> children;

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean leaf) {
		isLeaf = leaf;
	}



	public List<SysPermissionTree> getChildren() {
		return children;
	}

	public void setChildren(List<SysPermissionTree> children) {
		this.children = children;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Double getSortNo() {
		return sortNo;
	}

	public void setSortNo(Double sortNo) {
		this.sortNo = sortNo;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}


	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPermission() {
		return this.permission;
	}

	public void setPermission(String perms) {
		this.permission = perms;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}


}
