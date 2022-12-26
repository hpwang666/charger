package com.wwp.sevice.impl;

import com.wwp.sevice.IDevEnergyService;
import com.wwp.common.exception.CustomException;
import com.wwp.entity.DevEnergy;
import com.wwp.mapper.DevEnergyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DevEnergyService implements IDevEnergyService {
    @Resource
    private DevEnergyMapper devEnergyMapper;

    @Override
    public void regionDevEnergy(String serialNum,String parentId)
    {
        DevEnergy devEnergy = new DevEnergy();
        if(devEnergyMapper.getDevEnergy(serialNum)!= null) throw new CustomException("序列号: " + serialNum + " 的设备已存在");
        devEnergy.setSerialNum(serialNum);
        devEnergy.setParentId(parentId);
        devEnergy.setOnlineStatus("1");
        devEnergyMapper.addDevEnergyMapper(devEnergy);
    }

    @Override
    public void updateHeartBeatTime(String serialNum, Date time)
    {
        if(devEnergyMapper.getDevEnergy(serialNum)== null) throw new CustomException("序列号: " + serialNum + " 的设备未注册");
         devEnergyMapper.updateHeartBeatTime(serialNum,time);
    }

    @Override
    public void updateVersion(String serialNum,String version)
    {
        if(devEnergyMapper.getDevEnergy(serialNum)== null) throw new CustomException("序列号: " + serialNum + " 的设备未注册");
        devEnergyMapper.updateVersion(serialNum,version);
    }

}
