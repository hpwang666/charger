package com.wwp.entity;

import com.wwp.common.aspect.annotation.Id;

import java.io.Serializable;
import java.util.Date;

public class YlcCharger implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    //所属部门/车场
    private String departId;

    //序列号 7个字节
    private String serialNum;

    //充电桩类型
    private Integer type;

    //充电枪数量
    private Integer plugs;

    //计费模型id
    private String modelId;


    //0x00：离线   0x01：故障  0x02：空闲  0x03：充电
    private Integer plugStatus;

    // 0x00归位  0x01：未归位  0x02 未知
    private Integer plugHoming;

    //是否插枪 0x00 no   0x01 yes
    private Integer  slotIn;

    private Integer errorCode;//故障代码

    private java.util.Date updateTime;

    private Integer delFlag; //0--正常  1--删除

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }
    public String getDepartId() {
        return departId;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
    public String getSerialNum() {
        return this.serialNum;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getType() {
        return type;
    }

    public void setPlugs(Integer plugs) {
        this.plugs = plugs;
    }
    public Integer getPlugs() {
        return plugs;
    }

    public Integer getSlotIn() {
        return slotIn;
    }

    public void setSlotIn(Integer slotIn) {
        this.slotIn = slotIn;
    }

    public Integer getPlugHoming() {
        return plugHoming;
    }

    public void setPlugHoming(Integer plugHoming) {
        this.plugHoming = plugHoming;
    }

    public void setPlugStatus(Integer plugStatus) {
        this.plugStatus = plugStatus;
    }
    public Integer getPlugStatus() {
        return plugStatus;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
    public String getModelId() {
        return modelId;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }


    public Integer getDelFlag() {
        return delFlag;
    }
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
