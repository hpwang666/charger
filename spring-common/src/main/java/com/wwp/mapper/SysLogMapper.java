package com.wwp.mapper;

import com.wwp.entity.SysLog;
import org.apache.ibatis.annotations.Param;

public interface SysLogMapper {
    void saveLog(@Param("sysLog") SysLog sysLog);
}
