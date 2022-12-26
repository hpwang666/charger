package com.wwp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Energy implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String serialNum; //采集设备序列号
    private Date updateTime;
    private BigDecimal updateKwh;//每次更新的电表度数增量
    private BigDecimal totalKwh;//电表总计数

    public void setId(Integer id)
    {
        this.id = id;
    }
    public Integer getId()
    {
        return this.id;
    }
    public void setSerialNum(String serialNum)
    {
        this.serialNum = serialNum;
    }
    public String getSerialNum()
    {
        return this.serialNum;
    }
    public void setUpdateTime(Date time)
    {
        this.updateTime=time;
    }
    public Date getUpdateTime()
    {
        return this.updateTime;
    }
    public void setUpdateKwh(BigDecimal updateKwh)
    {
        this.updateKwh= updateKwh;
    }
    public BigDecimal getUpdateKwh()
    {
        return this.updateKwh;
    }
    public void setTotalKwh(BigDecimal totalKwh)
    {
        this.totalKwh=totalKwh;
    }
    public BigDecimal getTotalKwh()
    {
        return this.totalKwh;
    }

}
