package com.wwp.sevice.impl;

import com.wwp.sevice.ISysLogService;
import com.wwp.common.util.SpringContextUtils;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.LoginUser;
import com.wwp.entity.SysLog;
import com.wwp.entity.SysUser;
import com.wwp.mapper.SysLogMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class SysLogService implements ISysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public void addLog(SysLog sysLog) {
        if(oConvertUtils.isEmpty(sysLog.getId())){
            //sysLog.setId(String.valueOf(IdWorker.getId()));
        }
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            sysLogMapper.saveLog(sysLog);
        } catch (Exception e) {
            System.out.println(" LogContent length : "+sysLog.getLogContent().length());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operatetype, LoginUser user) {
        SysLog sysLog = new SysLog();
        //sysLog.setId(String.valueOf(IdWorker.getId()));
        //注解上的描述,操作日志内容
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operatetype);
        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp("1.1.1.1");
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        //获取登录用户信息
        if(user==null){
            try {
                SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
                sysLog.setUsername(sysUser.getAccount());
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        if(user!=null){
            sysLog.setUsername(user.getAccount());
        }
        sysLog.setCreateTime(new Date());
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            sysLogMapper.saveLog(sysLog);
        } catch (Exception e) {
            System.out.println(" LogContent length : "+sysLog.getLogContent().length());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operateType) {
        addLog(logContent, logType, operateType, null);
    }


}
