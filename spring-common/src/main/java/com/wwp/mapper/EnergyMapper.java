package com.wwp.mapper;

import com.wwp.entity.Energy;
import com.wwp.vo.QueryEnergyByTime;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface EnergyMapper {
    public void addEnergy(Energy energy);
    public List<Energy> queryEnergyByPage(@Param("serialNum") String serialNum,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);
    public List<Energy> queryEnergyByTimeByPage(@Param("serialNum") String serialNum, @Param("startTime") Date startTime, @Param("startTime") Date endTime,
                                                @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);
    public List<QueryEnergyByTime> queryEnergyByDay(@Param("day")Date day);
}
