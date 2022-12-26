package com.wwp.entity;


import com.wwp.common.aspect.annotation.Id;

import java.io.Serializable;


public class SysPermission implements Serializable {

    @Id
    private String id;//主键.
    private String name;//名称.
    private String resourceType;//资源类型，[menu0|menu1|menu2|menu3|button|auth]
    private String url;//资源路径.
    private Double sortNo; //菜单排序
    private String component;//组件
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private String parentId; //父编号
    private boolean isLeaf; //是否为叶子节点
    private boolean delFlag;
    //private List<SysRole> roles;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public void setSortNo(Double sortNo)
    {
        this.sortNo = sortNo;
    }
    public Double getSortNo()
    {
        return this.sortNo;
    }

    public void setComponent(String component){
        this.component = component;
    }
    public String getComponent(){
        return this.component;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getParentId() {
          return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


    public void setIsLeaf(boolean isLeaf){
        this.isLeaf = isLeaf;
    }
    public boolean getIsLeaf()
    {
        return this.isLeaf;
    }

    public void setDelFlag(boolean delFlag){
        this.delFlag = delFlag;
    }
    public boolean getDelFlag(){
        return this.delFlag;
    }

   /* public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }*/
}