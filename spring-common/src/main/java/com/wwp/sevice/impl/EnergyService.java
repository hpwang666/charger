package com.wwp.sevice.impl;

import com.wwp.sevice.IEnergyService;
import com.wwp.common.exception.CustomException;
import com.wwp.entity.Energy;
import com.wwp.mapper.DevEnergyMapper;
import com.wwp.mapper.EnergyMapper;
import com.wwp.vo.QueryEnergyByTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class EnergyService implements IEnergyService {

    @Resource
    EnergyMapper energyMapper;

    @Resource
    private DevEnergyMapper devEnergyMapper;

    @Override
    public void saveEnergy(Energy energy)
    {
        if(devEnergyMapper.getDevEnergy(energy.getSerialNum())== null) throw new CustomException("序列号: " + energy.getSerialNum() + " 的设备未注册");
        energyMapper.addEnergy(energy);
    }
    @Override
    public List<Energy> queryEnergyByPage(String serialNum, String pageNo, String pageSize)
    {
        if(devEnergyMapper.getDevEnergy(serialNum)== null) throw new CustomException("序列号: " + serialNum + " 的设备未注册");
        return energyMapper.queryEnergyByPage(serialNum,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
    }

    @Override
    public List<QueryEnergyByTime> queryEnergyByDay(Date day)
    {
        return energyMapper.queryEnergyByDay(day);
    }
}
