package com.wwp.sevice;

import java.util.Date;

public interface IDevEnergyService {
    public void regionDevEnergy(String serialNum,String parentId);
    public void updateHeartBeatTime(String serialNum, Date time);
    public void updateVersion(String serialNum, String version);
}
