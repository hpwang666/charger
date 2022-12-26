package com.wwp.sevice;

import com.wwp.entity.Energy;
import com.wwp.vo.QueryEnergyByTime;

import java.util.Date;
import java.util.List;

public interface IEnergyService {
    void saveEnergy(Energy energy);
    List<Energy> queryEnergyByPage(String serialNum, String pageNo, String pageSize);
    List<QueryEnergyByTime> queryEnergyByDay(Date day);//查询某天每台设备耗电总数
}
