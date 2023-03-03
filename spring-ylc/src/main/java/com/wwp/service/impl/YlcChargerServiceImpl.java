package com.wwp.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;
import com.wwp.common.constant.YlcCommonConstant;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.YlcCharger;
import com.wwp.entity.YlcFeeModel;
import com.wwp.mapper.YlcChargerMapper;
import com.wwp.mapper.YlcFeeModelMapper;
import com.wwp.service.IYlcChargerService;
import com.wwp.vo.ChargerVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class YlcChargerServiceImpl implements IYlcChargerService {

    @Resource
    YlcChargerMapper ylcChargerMapper;

    @Resource
    YlcFeeModelMapper ylcFeeModelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(YlcCharger ylcCharger,String feeString,List<String> ratesList)
    {
        YlcFeeModel model =  new YlcFeeModel();
        model.setFeesByModel(feeString);
        model.setFee0(ratesList.get(0));
        model.setFee1(ratesList.get(1));
        model.setFee2(ratesList.get(2));
        model.setFee3(ratesList.get(3));
        model.setLossRate(0);
        model.setModelCode("0001");
        ylcFeeModelMapper.add(model);

        ylcCharger.setModelId(model.getId());
        ylcCharger.setDelFlag(0);
        ylcCharger.setPlugs(1);
        ylcCharger.setType(1);
        ylcChargerMapper.add(ylcCharger);
    }

    @Override
    public void delete(String serialNum)
    {
        YlcCharger charger = ylcChargerMapper.getChargerBySerialNum(serialNum);
        if(oConvertUtils.isEmpty(charger)) throw new CustomException("充电桩不存在");
        charger.setDelFlag(1);
        ylcChargerMapper.update(charger);
    }

    @Override
    public YlcCharger getYlcChargerBySerialNum(String serialNum)
    {
        return ylcChargerMapper.getChargerBySerialNum(serialNum);
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
        YlcCharger charger =  ylcChargerMapper.getChargerBySerialNum(serialNum);
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
    public List<ChargerVO> queryChargersByDepId(String departId)
    {
        List<YlcCharger>  chargers = ylcChargerMapper.queryChargersByDepId(departId);

        List<ChargerVO> chargerVOS = new ArrayList<>();
        for (int i = 0; i < chargers.size(); i++) {
            YlcCharger ylcCharger = chargers.get(i);
            ChargerVO chargerVO = new ChargerVO();
            chargerVO.setSerialNum(ylcCharger.getSerialNum());
            chargerVO.setPlugs(ylcCharger.getPlugs());

            YlcFeeModel ylcFeeModel =  ylcFeeModelMapper.getFeeModelById(ylcCharger.getModelId());

            Date start,end;
            int index =0;

            start = DateUtil.parse("00:00","HH:mm");
            end = new Date();
            while((DateUtil.compare(start,end,"HH:mm")<0) && index<48){
                index++;
                start =DateUtil.offsetMinute(start,30);
            }

            String h = ylcFeeModel.getFeesByModel().substring((index-1)*2+1,(index-1)*2+2);
            try{
                Class<?> YlcFeeModel = Class.forName("com.wwp.entity.YlcFeeModel");
                Method method = YlcFeeModel.getMethod("getFee"+h);
                String rate= (String) method.invoke(ylcFeeModel, new Object[]{});

                //两个费率求和
                String bigRate = (new BigDecimal(HexUtil.hexToInt(rate.substring(0,8))).add(new BigDecimal(HexUtil.hexToInt(rate.substring(8,16))))).
                    divide(new BigDecimal(100000)).setScale(2).toString();
                chargerVO.setRate(bigRate);
            }
            catch (Exception e)
            {
                throw new CustomException(e.getMessage());
            }
            chargerVO.setOnLine("离线");
            if(!oConvertUtils.isEmpty(ylcCharger.getUpdateTime()) )
            {
                //时间超过60秒，判定为离线
                if(DateUtil.between(ylcCharger.getUpdateTime(), new Date(),DateUnit.SECOND)<60)
                    chargerVO.setOnLine("在线");
            }
            chargerVOS.add(chargerVO);
        }
        return chargerVOS;
    }

    @Override
    public YlcCharger getYlcChargerById(String id)
    {
        return ylcChargerMapper.getYlcChargerById(id);
    }


}
