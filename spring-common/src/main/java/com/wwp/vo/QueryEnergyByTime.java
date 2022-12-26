package com.wwp.vo;

import java.math.BigDecimal;
import java.util.Date;

public class QueryEnergyByTime {
    private String serialNum;
    private BigDecimal totalKwh;
    private Date day;

    public void setSerialNum(String serialNum)
    {
        this.serialNum = serialNum;
    }
    public String getSerialNum()
    {
        return this.serialNum;
    }
    public void setTotalKwh(BigDecimal totalKwh)
    {
        this.totalKwh = totalKwh;
    }
    public BigDecimal getTotalKwh()
    {
        return this.totalKwh;
    }

    public void setDay(Date day)
    {
        this.day = day;
    }
    public Date getDay()
    {
        return this.day;
    }
}
