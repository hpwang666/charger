package com.wwp.vo;

import java.io.Serializable;

public class ChargerVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //序列号 14个字节
    private String serialNum;

    //充电枪数量
    private Integer plugs;

    //当前费率
    private String rate;

    private String addrs;

    private String phone;

    //在线状态
    private String onLine;

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setPlugs(Integer plugs) {
        this.plugs = plugs;
    }

    public Integer getPlugs() {
        return plugs;
    }

    public void setAddrs(String addrs) {
        this.addrs = addrs;
    }

    public String getAddrs() {
        return addrs;
    }

    public void setOnLine(String onLine) {
        this.onLine = onLine;
    }

    public String getOnLine() {
        return onLine;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }
}
