package com.wwp.mapper;

import com.wwp.entity.DevEnergy;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

public interface DevEnergyMapper {
    public void addDevEnergyMapper(DevEnergy devEnergy);
    public DevEnergy getDevEnergy(@Param("serialNum") String serialNum);
    public void updateHeartBeatTime(@Param("serialNum")String serialNum,@Param("heartBeatTime")Date heartBeatTime);
    public void updateVersion(String serialNum,String version);
}
