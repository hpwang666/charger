package com.wwp.entity;

import java.io.Serializable;
import java.util.Date;

public class DevEnergy implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String serialNum; //序列号
    private String parentId; //所属大车/上级设备
    private String version; //设备版本号
    private Date heartBeatTime; //心跳时间

    //@Dict(dicCode = "sss")
    private String onlineStatus;//在线状态  0--online  1--offline

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
    public void setParentId(String parentId)
    {
        this.parentId=parentId;
    }
    public String getParentId()
    {
        return this.parentId;
    }
    public void setVersion(String version)
    {
        this.version=version;
    }
    public String getVersion()
    {
        return this.version;
    }
    public void setHeartBeatTime(Date heartBeatTime)
    {
        this.heartBeatTime= heartBeatTime;
    }
    public Date getHeartBeatTime()
    {
        return this.heartBeatTime;
    }
    public void setOnlineStatus(String onlineStatus)
    {
        this.onlineStatus=onlineStatus;
    }
    public String getOnlineStatus()
{
    return this.onlineStatus;
}
}
