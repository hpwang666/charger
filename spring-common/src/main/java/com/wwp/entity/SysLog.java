package com.wwp.entity;

import java.io.Serializable;
import java.util.Date;

public class SysLog implements Serializable {
    private static final long serialVersionUID = 8482720462943906924L;

    /**内容*/
    private String logContent;

    /**日志类型(0:操作日志;1:登录日志;2: 3:定时任务)  */
    private Integer logType;

    /**操作类型(1:添加;2:修改;3:删除;) */
    private Integer operateType;

    /**登录用户 */
    private LoginUser loginUser;

    private Integer id;
    private String createBy;
    private Date createTime;
    private Long costTime;
    private String ip;

    /**请求参数 */
    private String requestParam;

    /**请求类型*/
    private String requestType;

    /**请求路径*/
    private String requestUrl;

    /**请求方法 */
    private String method;

    /**操作人用户名称*/
    private String username;


    public SysLog(){

    }

    public SysLog(String logContent, Integer logType, Integer operatetype){
        this.logContent = logContent;
        this.logType = logType;
        this.operateType = operatetype;
    }

    public SysLog(String logContent, Integer logType, Integer operatetype, LoginUser loginUser){
        this.logContent = logContent;
        this.logType = logType;
        this.operateType = operatetype;
        this.loginUser = loginUser;
    }

    public void setLogContent(String logContent) { this.logContent=logContent;    }
    public String getLogContent(){return this.logContent;}

    public void setLogType(Integer logType){this.logType = logType;}
    public Integer getLogType(){return this.logType;}

    public void setOperateType(Integer operateType){this.operateType=operateType;}
    public Integer getOperateType(){return this.operateType;}

    public void setLoginUser(LoginUser loginUser){this.loginUser=loginUser;}
    public LoginUser getLoginUser(){return this.loginUser;}

    public void setId(Integer id){this.id = id;}
    public Integer getId(){return this.id;}

    public void setCreateBy(String createBy){this.createBy= createBy;}
    public String getCreateBy(){return this.createBy;}

    public void setCreateTime(Date createTime){this.createTime = createTime;}
    public Date getCreateTime(){return this.createTime;}

    public  void setCostTime(Long costTime){this.costTime=costTime;}
    public Long getCostTime(){return this.costTime;}

    public void setIp(String ip){this.ip=ip;}
    public String getIp(){return this.ip;}

    public void setRequestParam(String requestParam){this.requestParam=requestParam;}
    public String getRequestParam(){return this.requestParam;}

    public void setRequestType(String requestType){this.requestType=requestType;}
    public String getRequestType(){return this.requestType;}

    public void setRequestUrl(String requestUrl){this.requestUrl=requestUrl;}
    public String getRequestUrl(){return this.requestUrl;}

    public void setMethod(String method ){this.method = method;}
    public String getMethod(){return this.method;}

    public void setUsername(String username){this.username=username;}
    public String getUsername(){return this.username;}



}
