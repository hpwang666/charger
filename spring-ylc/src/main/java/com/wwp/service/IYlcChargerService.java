package com.wwp.service;

import com.wwp.entity.YlcCharger;
import com.wwp.vo.ChargerVO;

import java.util.Date;
import java.util.List;

public interface IYlcChargerService {
     void add(YlcCharger ylcCharger,String feeString,List<String> ratesList);
     YlcCharger getYlcChargerBySerialNum(String serialNum);

     YlcCharger getYlcChargerById(String serialNum);

     void delete(String serialNum);

     //由心跳进行更新
     void updateTime(String serialNum, Date date);

     //通过上传status进行更新
     void updateStatus(String serialNum, Integer plugStatus,
                              Integer plugHoming, Integer slotIn);

     //这里会检查充电桩是否在线，是否可用
     boolean checkChargerAvailable(String serialNum);

     List<ChargerVO> queryChargersByDepId(String departId);
}
