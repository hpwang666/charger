package com.wwp.mapper;

import com.wwp.entity.YlcCharger;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface YlcChargerMapper {
     void add(YlcCharger ylcCharger);
     YlcCharger getChargerBySerialNum(@Param("serialNum") String serialNum);
     void updateTime(@Param("serialNum")String serialNum,@Param("updateTime") Date updateTime);

     //更新充电桩的状态  以及是否插枪
     void updateStatus(@Param("serialNum")String serialNum,@Param("plugStatus") Integer plugStatus,
                       @Param("plugHoming") Integer plugHoming,@Param("slotIn") Integer slotIn);

     void update(YlcCharger ylcCharger);
     List<YlcCharger> queryChargersByDepId(String departId);

     @Select("select * from ylc_charger where id = #{id}")
     YlcCharger getYlcChargerById(@Param("id") String id);


}
