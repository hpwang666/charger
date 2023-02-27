package com.wwp.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.wwp.common.constant.YlcCommonConstant;
import com.wwp.entity.YlcCharger;
import com.wwp.mapper.YlcChargerMapper;
import com.wwp.service.IYlcChargerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class YlcChargerServiceImpl implements IYlcChargerService {

    @Resource
    YlcChargerMapper ylcChargerMapper;



    @Override
    public void add(YlcCharger ylcCharger)
    {
        ylcChargerMapper.add(ylcCharger);
    }

    @Override
    public YlcCharger getYlcChargerBySerialNum(String serialNum)
    {
        return ylcChargerMapper.getDevCharger(serialNum);
    }

    @Override
    public void updateTime(String serialNum, Date date)
    {
        ylcChargerMapper.updateTime(serialNum,date);
    }

    @Override
    public void updateStatus(String serialNum, Integer plugStatus,
                             Integer plugHoming, Integer slotIn)
    {
        ylcChargerMapper.updateStatus(serialNum,plugStatus,plugHoming,slotIn);
    }

    @Override
    public  boolean checkChargerAvailable(String serialNum)
    {
        YlcCharger charger =  ylcChargerMapper.getDevCharger(serialNum);
        if(charger!=null && (charger.getUpdateTime()!=null)){
            DateUtil.format(charger.getUpdateTime(), DatePattern.NORM_DATETIME_PATTERN);

            //有正常的心跳
            if(DateUtil.between(charger.getUpdateTime(), new Date(), DateUnit.SECOND)<15){
                if(charger.getPlugStatus() != YlcCommonConstant.STATUS_FREE)
                    return false;
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Override
    public List<YlcCharger> queryChargersByDepId(String departId)
    {
        return ylcChargerMapper.queryChargersByDepId(departId);
    }
}
