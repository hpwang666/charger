package com.wwp.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.wwp.common.aspect.annotation.Id;

import java.io.Serializable;
import java.util.Date;


public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String account;//帐号
    private String name;//名称（昵称或者真实姓名，不同系统不同定义）

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password; //密码;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;//加密密码的盐

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    private String phone;

    private String openid;

    private Integer type;//用户类型 商户--0 车主--1

    private Date loginTime;//最后一次登录时间


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }


    public void setType(Integer type)
    {
        this.type = type;
    }
    public Integer getType()
    {
        return this.type;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return this.phone;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
    public Date getLoginTime() {
        return loginTime;
    }
}