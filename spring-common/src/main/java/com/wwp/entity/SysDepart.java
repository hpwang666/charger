package com.wwp.entity;

import com.wwp.common.aspect.annotation.Id;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 部门表
 * <p>
 * 
 * @Author Steve
 * @Since  2022-04-29
*/
public class SysDepart implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**ID*/
	@Id
	private String id;

	/**父机构ID*/
	private String parentId;

	private String parentPrjId; //关联的父项目ID  为了直接关联大车场

	private String departName;//机构/部门名称

	private String departNamePy;//机构名称拼音

	private String departNameAbbr;//机构简称

	private String payCode;//支付机构对于的编码

	private Integer payChannel;//支付机构类型默认0-泊链支付，1-PP支付,2-移领支付

	private Integer orgCategory;//机构类别，1城市，2集团，3公司，4车场

	private String orgCode;//机构编码

	private String memo;//备注

	private Integer state;//状态（1启用，0不启用）

	private String delFlag;//删除状态（0，正常，1已删除）


	//private List<SysDepart> parentList;

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

	public void setParentPrjId(String parentPrjId)
	{
		this.parentPrjId = parentPrjId;
	}
	public String getParentPrjId()
	{
		return this.parentPrjId;
	}
	public void setPayCode(String payCode)
	{
		this.payCode = payCode;
	}
	public String getPayCode()
	{
		return this.payCode;
	}
	public void setPayChannel(Integer payChannel)
	{
		this.payChannel = payChannel;
	}
	public Integer getPayChannel()
	{
		return this.payChannel;
	}
	public void setDepartName(String departName)
	{
		this.departName = departName;
	}
	public String getDepartName()
	{
		return this.departName;
	}
	public void setDepartNamePy(String departNamePy)
	{
		this.departNamePy = departNamePy;
	}
	public String getDepartNamePy()
	{
		return this.departNamePy;
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
	public void setState(Integer state)
	{
		this.state = state;
	}
	public Integer getState()
	{
		return this.state;
	}
	public void setDelFlag(String delFlag)
	{
		this.delFlag = delFlag;
	}
	public String getDelFlag()
	{
		return this.delFlag;
	}


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
        if (!super.equals(o)) {
			return false;
		}
        SysDepart depart = (SysDepart) o;
        return Objects.equals(id, depart.id) &&
                Objects.equals(parentId, depart.parentId) &&
                Objects.equals(departName, depart.departName) &&
                Objects.equals(departNameAbbr, depart.departNameAbbr) &&
                Objects.equals(orgCategory, depart.orgCategory) &&
                Objects.equals(orgCode, depart.orgCode) &&
                Objects.equals(memo, depart.memo) &&
                Objects.equals(state, depart.state) ;
    }

    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, parentId, departName, 
        		departNameAbbr, orgCategory, orgCode, memo, state);
    }
}
