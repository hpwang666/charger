package com.wwp.controller;

import com.alibaba.fastjson.JSONObject;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.oConvertUtils;
import com.wwp.devices.YlcDeviceMap;
import com.wwp.entity.SysDepart;
import com.wwp.entity.YlcCharger;
import com.wwp.entity.YlcFeeModel;
import com.wwp.entity.YlcUserLogical;
import com.wwp.model.YlcCtrlMsg;
import com.wwp.model.YlcResult;
import com.wwp.service.IYlcChargerService;
import com.wwp.service.IYlcCtrlService;
import com.wwp.service.IYlcFeeModelService;
import com.wwp.service.IYlcUserLogicService;
import com.wwp.model.YlcMsgType;
import com.wwp.util.YlcStringUtils;
import com.wwp.util.YlcTimeFeeConvert;
import com.wwp.vo.ChargerVO;
import com.wwp.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/charger")
public class YlcChargerController {
    @Resource
    IYlcCtrlService ylcCtrlService;

    @Resource
    IYlcUserLogicService ylcUserLogicService;

    @Resource
    IYlcChargerService ylcChargerService;

    @Resource
    IYlcFeeModelService ylcFeeModelService;

    @GetMapping("/channelList")
    public List<String> queryChannelList() {

        return YlcDeviceMap.listAllChannel();

    }

    @RequestMapping(value="/list",method= RequestMethod.GET)
    public Result<List<ChargerVO>> queryChargerList(@RequestParam String departId)
    {
        List<ChargerVO> chargers = ylcChargerService.queryChargersByDepId(departId);
        return Result.OK("查询成功",chargers);
    }

    @RequestMapping(value="/feeModel",method= RequestMethod.GET)
    public Result<?> queryfeeModel(@RequestParam String serialNum)
    {

        YlcCharger charger = ylcChargerService.getYlcChargerBySerialNum(serialNum);

        YlcFeeModel model =  ylcFeeModelService.getFeeModel(charger.getModelId());
        if(oConvertUtils.isEmpty(model)) return Result.error("没有计费模型");
        JSONObject obj;

        obj=  YlcTimeFeeConvert.feeModel2Time(model.getFeesByModel(),model.getFee0(),model.getFee1(),model.getFee2(),model.getFee3());



        return Result.OK("查询成功",obj);

    }

    @RequestMapping(value="/feeModel/edit",method= RequestMethod.POST)
    public Result<?> updatefeeModel(@RequestBody JSONObject jsonFeeModel,@RequestParam String serialNum)
    {

        YlcCharger charger = ylcChargerService.getYlcChargerBySerialNum(serialNum);
        if(oConvertUtils.isEmpty(charger)) throw  new CustomException("充电桩错误");

        YlcFeeModel model =  ylcFeeModelService.getFeeModel(charger.getModelId());
        if(oConvertUtils.isEmpty(model)) return Result.error("没有计费模型");

        String feeString =  YlcTimeFeeConvert.time2FeeModel(jsonFeeModel.getJSONArray("timeQuantum"));

        List<String> ratesList =  YlcTimeFeeConvert.ratesJson2String(jsonFeeModel.getJSONArray("rates"));

        model.setFeesByModel(feeString);
        model.setFee0(ratesList.get(0));
        model.setFee1(ratesList.get(1));
        model.setFee2(ratesList.get(2));
        model.setFee3(ratesList.get(3));

        ylcFeeModelService.updateFeeModel(model);

        return Result.OK("更新成功");
    }

    @RequestMapping(value="/genSerialNum",method= RequestMethod.GET)
    public Result<?> genSerialNum()
    {
        JSONObject obj = new JSONObject();

        String serialNum = YlcStringUtils.genSerialNum();
        YlcCharger ylcCharger = ylcChargerService.getYlcChargerBySerialNum(serialNum);
        if(oConvertUtils.isEmpty(ylcCharger))
            obj.put("serialNum", YlcStringUtils.genSerialNum());
        else return  Result.error("序列号获取失败");
    return Result.OK("更新成功",obj);

    }

    @RequestMapping(value="/add",method= RequestMethod.POST)
    public Result<?> add(@RequestBody JSONObject jsonFeeModel,@RequestParam String departId,@RequestParam String serialNum)
    {

        if(oConvertUtils.isEmpty(departId)) throw new CustomException("departId 为空");


        YlcCharger charger = new YlcCharger();
        charger.setSerialNum(serialNum);
        charger.setDepartId(departId);


        String feeString =  YlcTimeFeeConvert.time2FeeModel(jsonFeeModel.getJSONArray("timeQuantum"));
        List<String> ratesList =  YlcTimeFeeConvert.ratesJson2String(jsonFeeModel.getJSONArray("rates"));
        System.out.println(feeString);
        System.out.println(ratesList);

        ylcChargerService.add(charger,feeString,ratesList);

        return Result.OK("添加成功");
    }

    @RequestMapping(value="/delete",method= RequestMethod.GET)
    public Result<?> delete(@RequestParam String serialNum)
    {

        ylcChargerService.delete(serialNum);

        return Result.OK("删除成功");
    }

    @RequestMapping(value="/remoteOn",method= RequestMethod.GET)
    @ApiOperation("APP充电申请")
    public Result remoteOn(@RequestParam String chargerId) {


        if(ylcChargerService.checkChargerAvailable(chargerId)) {
            YlcCtrlMsg ctrlMsg = new YlcCtrlMsg();
            ctrlMsg.setMsgType(YlcMsgType.REMOTE_ON);
            ctrlMsg.setSerialNum(chargerId);
            ctrlMsg.setPlugNo(1);

            YlcUserLogical ylcUserLogical = ylcUserLogicService.queryByUserId("1111");

            ctrlMsg.setUserId("1111");
            ctrlMsg.setLogicNum(ylcUserLogical.getLogicalNum());//0000001000000573
            ctrlMsg.setPhysicalNum(ylcUserLogical.getLogicalNum());//00000000D14B0A54

            //金额太大了 就下发1000.00元
            if (ylcUserLogical.getAmount().toString().length() > 6)
                ctrlMsg.setAccount("100000");//A0860100   1000.00元
            else {
                ctrlMsg.setAccount(ylcUserLogical.getAmount().toString());
            }


            YlcResult result = ylcCtrlService.remoteDevOn(ctrlMsg);
            if (result.getSuccess() == true)
                return Result.OK();
            else return Result.error(result.getMessage()) ;
        }
        else return Result.error("充电桩不可用");
    }

    //这个卡的物理卡号  00000000A5FBCA5B
    @GetMapping("/remoteAddPhysCard")
    public String remoteAddPhys() {
        YlcCtrlMsg ctrlMsg = new YlcCtrlMsg();
        ctrlMsg.setMsgType(YlcMsgType.CARD_UPDATE);
        ctrlMsg.setSerialNum("32010600213533");
        ctrlMsg.setPlugNo(1);
        ctrlMsg.setLogicNum("0000002022009092");
        ctrlMsg.setPhysicalNum("AAAAANFLClQ=");//00000000D1B2C3D4  AAAAANGyw9Q=
        ctrlMsg.setAccount("oIYBAA==");//A0860100   1000.00元

        YlcResult result =  ylcCtrlService.remoteAddPhysCard(ctrlMsg);
        System.out.println(result.getMessage());
        if(result.getSuccess()==true)
            return YlcDeviceMap.getDEVICES().size()+" ";
        else
            return result.getMessage();
    }

    @GetMapping("/remoteOff")
    public String remoteOff() {


        YlcCtrlMsg ctrlMsg = new YlcCtrlMsg();
        ctrlMsg.setMsgType(YlcMsgType.REMOTE_OFF);
        ctrlMsg.setSerialNum("32010600213533");
        ctrlMsg.setPlugNo(1);

        YlcResult result =  ylcCtrlService.remoteDevOff(ctrlMsg);
        if(result.getSuccess()==true)
            return YlcDeviceMap.getDEVICES().size()+" ";
        else return result.getMessage();
    }

}
