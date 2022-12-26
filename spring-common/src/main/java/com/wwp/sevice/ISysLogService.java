package com.wwp.sevice;

import com.wwp.entity.LoginUser;
import com.wwp.entity.SysLog;

public interface ISysLogService {


    void addLog(SysLog sysLog);

    /**
     * 保存日志
     * @param LogContent
     * @param logType
     * @param operateType
     * @param user
     */
    void addLog(String LogContent, Integer logType, Integer operateType, LoginUser user);

    /**
     * 保存日志
     * @param LogContent
     * @param logType
     * @param operateType
     */
    void addLog(String LogContent, Integer logType, Integer operateType);
}
