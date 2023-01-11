package com.wwp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22 
 */

public class SysDepartTreeModel implements Serializable{
	
    private static final long serialVersionUID = 1L;
    
    /** 对应SysDepart中的id字段,前端数据树中的key*/
    private String key;

    /** 对应SysDepart中的id字段,前端数据树中的value*/
    private String value;

    /** 对应depart_name字段,前端数据树中的title*/
    private String lable;

    private boolean isLeaf;
    // 以下所有字段均与SysDepart相同
    
    private String id;

    private String parentId;

    private String departName;

    private String departNameAbbr;

    private Integer orgCategory;

    private String orgCode;

    private String memo;

    private Integer state;

    private List<SysDepartTreeModel> children = new ArrayList<>();


    public void setKey(String key)
    {
        this.key = key;
    }
    public String getKey()
    {
        return this.key;
    }
    public void setValue(String value)
    {
        this.value= value;
    }
    public String getValue()
    {
        return this.value;
    }
    public void setLable(String title)
    {
        this.lable = title;
    }
    public String getLable()
    {
        return this.lable;
    }
    public void setLeaf(boolean isLeaf)
    {
        this.isLeaf = isLeaf;
    }
    public boolean getLeaf()
    {
        return this.isLeaf;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getId()
    {
        return this.id;
    }
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }
    public String getParentId()
    {
        return this.parentId;
    }
    public void setDepartName(String departName)
    {
        this.departName = departName;
    }
    public String getDepartName()
    {
        return this.departName;
    }
    public void setDepartNameAbbr(String departNameAbbr)
    {
        this.departNameAbbr= departNameAbbr;
    }
    public String getDepartNameAbbr()
    {
        return this.departNameAbbr;
    }
    public void setOrgCategory(Integer orgCategory)
    {
        this.orgCategory = orgCategory;
    }
    public Integer getOrgCategory()
    {
        return this.orgCategory;
    }
    public void setOrgCode(String orgCode)
    {
        this.orgCode = orgCode;
    }
    public String getOrgCode()
    {
        return this.orgCode;
    }
    public String getMemo()
    {
        return this.memo;
    }
    public void setMemo(String memo)
    {
        this.memo = memo;
    }
    public void setStatus(Integer status)
    {
        this.state = status;
    }
    public Integer getStatus()
    {
        return this.state;
    }
    public void setChildren(List<SysDepartTreeModel> children)
    {
        this.children=children;
    }
    public List<SysDepartTreeModel> getChildren()
    {
        return this.children;
    }

    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     * @param sysDepart
     */
	public SysDepartTreeModel(SysDepart sysDepart) {
		this.key = sysDepart.getId();
        this.value = sysDepart.getId();
        this.lable = sysDepart.getDepartName();
        this.id = sysDepart.getId();
        this.parentId = sysDepart.getParentId();
        this.departName = sysDepart.getDepartName();
        this.departNameAbbr = sysDepart.getDepartNameAbbr();
        this.orgCategory = sysDepart.getOrgCategory();
        this.orgCode = sysDepart.getOrgCode();
        this.memo = sysDepart.getMemo();
        this.state = sysDepart.getState();

    }

    public SysDepartTreeModel() { }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        SysDepartTreeModel model = (SysDepartTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(parentId, model.parentId) &&
                Objects.equals(departName, model.departName) &&
                Objects.equals(departNameAbbr, model.departNameAbbr) &&
                Objects.equals(orgCategory, model.orgCategory) &&
                Objects.equals(orgCode, model.orgCode) &&
                Objects.equals(memo, model.memo) &&
                Objects.equals(state, model.state) &&
                Objects.equals(children, model.children);
    }
    
    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(id, parentId, departName, departNameAbbr,
        		orgCategory, orgCode, memo, state, children);
    }

}
