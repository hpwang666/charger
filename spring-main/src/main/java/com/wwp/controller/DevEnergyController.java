package com.wwp.controller;

import com.alibaba.fastjson.JSONObject;
import com.wwp.common.aspect.annotation.AutoLog;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.DevEnergy;
import com.wwp.sevice.IDevEnergyService;
import com.wwp.sevice.IEnergyService;
import com.wwp.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/dev")
public class DevEnergyController {
    @Resource
    IDevEnergyService devEnergyService;

    @Resource
    IEnergyService energyService;

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public Result<DevEnergy> ping(@RequestBody JSONObject jsonObject)
    {
        String serialNum = jsonObject.getString("serialNum");
        if(oConvertUtils.isEmpty(serialNum)) throw new CustomException("No serialNum selected");
        devEnergyService.updateHeartBeatTime(serialNum,new Date());
        return Result.OK();
    }

    @RequestMapping(value="/setVer" ,method = RequestMethod.POST)
    public Result<?> setVer(@RequestBody JSONObject jsonObject)
    {
        String version = jsonObject.getString("version");
        String serialNum = jsonObject.getString("serialNum");
        if(oConvertUtils.isEmpty(serialNum)) throw new CustomException("No serialNum selected");
        if(oConvertUtils.isEmpty(version)) throw new CustomException("No version selected");
        devEnergyService.updateVersion(serialNum,version);
        return Result.OK();
    }

    @RequestMapping(value="/regionDev",method = RequestMethod.POST)
    @RequiresPermissions("dev:add")
    public Result<?> regionDev(@RequestBody JSONObject jsonObject)
    {
        String serialNum = jsonObject.getString("serialNum");
        String parentId = jsonObject.getString("parentId");

        if(oConvertUtils.isEmpty(serialNum)) throw new CustomException("No serialNum selected");
        if(oConvertUtils.isEmpty(parentId)) throw new CustomException("No parentId selected");

        devEnergyService.regionDevEnergy(serialNum,parentId);
        return Result.OK();
    }

    @AutoLog(value="查询能耗",logType = 2)
    @RequestMapping(value="/queryEnergy",method = RequestMethod.GET)
    public Result<?> queryEnergy(@RequestParam(value="serialNum",required=true) String serialNum,
                                 @RequestParam(value="pageNo",required=true,defaultValue="1") String pageNo,
                                 @RequestParam(value="pageSize",required=false,defaultValue="10") String pageSize)
    {
        return Result.OK(energyService.queryEnergyByPage(serialNum,pageNo,pageSize));
    }


    @AutoLog(value="按天查询能耗",logType = 2)
    @RequestMapping(value="/queryByDay",method = RequestMethod.GET)
    public Result<?> queryEnergyByDay(@RequestParam(value="day",required=true) String day)
    {
        try{
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.setLenient(false);//不让超出的数据进位
            return Result.OK(energyService.queryEnergyByDay(formatter.parse(day)));
        }
        catch (Exception e){
            return Result.error("时间格式错误: "+e.getMessage());
        }
    }
}
