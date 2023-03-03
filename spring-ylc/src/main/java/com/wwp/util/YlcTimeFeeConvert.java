package com.wwp.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.oConvertUtils;
import org.apache.http.ParseException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YlcTimeFeeConvert {

    public static JSONObject  feeModel2Time(String feeModel,String fee0,String fee1,String fee2,String fee3) throws CustomException
    {
        //String fee="000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        if(feeModel.length()!=96) throw new CustomException("字段长度错误");
        if(oConvertUtils.isEmpty(fee0)||oConvertUtils.isEmpty(fee1)||oConvertUtils.isEmpty(fee2)||oConvertUtils.isEmpty(fee3)) throw new CustomException("费率为空");


        JSONArray ratesArray = new JSONArray();
        JSONObject obj = new JSONObject();

        obj.put("name","尖");
        obj.put("data0",(new BigDecimal(HexUtil.hexToInt(fee0.substring(0,8))).divide(new BigDecimal(100000))).setScale(2).toString());
        obj.put("data1",(new BigDecimal(HexUtil.hexToInt(fee0.substring(8,16))).divide(new BigDecimal(100000))).setScale(2).toString());
        ratesArray.add(obj);

        obj = new JSONObject();
        obj.put("name","峰");
        obj.put("data0",(new BigDecimal(HexUtil.hexToInt(fee1.substring(0,8))).divide(new BigDecimal(100000))).setScale(2).toString());
        obj.put("data1",(new BigDecimal(HexUtil.hexToInt(fee1.substring(8,16))).divide(new BigDecimal(100000))).setScale(2).toString());
        ratesArray.add(obj);

        obj = new JSONObject();
        obj.put("name","平");
        obj.put("data0",(new BigDecimal(HexUtil.hexToInt(fee2.substring(0,8))).divide(new BigDecimal(100000))).setScale(2).toString());
        obj.put("data1",(new BigDecimal(HexUtil.hexToInt(fee2.substring(8,16))).divide(new BigDecimal(100000))).setScale(2).toString());
        ratesArray.add(obj);

        obj = new JSONObject();
        obj.put("name","谷");
        obj.put("data0",(new BigDecimal(HexUtil.hexToInt(fee3.substring(0,8))).divide(new BigDecimal(100000))).setScale(2).toString());
        obj.put("data1",(new BigDecimal(HexUtil.hexToInt(fee3.substring(8,16))).divide(new BigDecimal(100000))).setScale(2).toString());
        ratesArray.add(obj);

        List<String> timeZone = new ArrayList<>(48);
        for (int i = 0; i < 48; i++) {
            timeZone.add(StrUtil.sub(feeModel,i*2,(i*2)+2));
        }

        String s = timeZone.get(0);
        JSONArray jsonArray = new JSONArray();
        obj = new JSONObject();

        Date endTime = DateUtil.parse("00:00","HH:mm");
        obj.put("startTime",DateUtil.format(endTime, "HH:mm"));

        for(int i =0;i<timeZone.size();i++)
        {
            if(!timeZone.get(i).equals(s)){
                obj.put("endTime",DateUtil.format(endTime, "HH:mm"));

                obj.put("rate",s.substring(1));
                jsonArray.add(obj);
                obj = new JSONObject();
                obj.put("startTime",DateUtil.format(endTime, "HH:mm"));
                s= timeZone.get(i);
            }
            if(i==timeZone.size()-1){
                obj.put("endTime","24:00");//24:00是不能做时间转换的 ,只是为了前端对其 应该是23:59
                obj.put("rate",s.substring(1));

                jsonArray.add(obj);
            }
            endTime =DateUtil.offsetMinute(endTime,30);

        }
        obj = new JSONObject();
        obj.put("timeQuantum",jsonArray);
        obj.put("rates",ratesArray);

        return obj;
    }

    public static String time2FeeModel(JSONArray timeQuantum)
    {
        //String timeZone =  "{\"timeQuantum\":[{\"rate\":\"00\",\"startTime\":\"00:00\",\"endTime\":\"00:30\"},{\"rate\":\"01\",\"startTime\":\"00:30\",\"endTime\":\"01:00\"},{\"rate\":\"05\",\"startTime\":\"01:00\",\"endTime\":\"23:59\"}]}";
        // String timeZone =  "{\"timeQuantum\":[{\"rate\":\"00\",\"startTime\":\"00:00\",\"endTime\":\"23:59\"}]}";



        if(oConvertUtils.isEmpty(timeQuantum)||timeQuantum.size()==0) throw new CustomException("时段1分区为空");

        timeQuantum.getJSONObject(timeQuantum.size()-1).put("endTime","23:59");//将24:00修正为23:59
        Date start,end;
        Integer index =0;
        StringBuilder feeBuilder = new StringBuilder(100);
        for(int i=0;i<timeQuantum.size();i++)
        {
            start = DateUtil.parse((String)timeQuantum.getJSONObject(i).get("startTime"),"HH:mm");
            end = DateUtil.parse((String)timeQuantum.getJSONObject(i).get("endTime"),"HH:mm");
            while(!DateUtil.isSameTime(start,end) && index<48){
                feeBuilder.append("0"+(String)timeQuantum.getJSONObject(i).get("rate"));
                index++;
                start =DateUtil.offsetMinute(start,30);
            }

        }
        if(feeBuilder.length()!=96)
            throw new CustomException("时间分段转换错误");

        return feeBuilder.toString();

    }

    public static List<String> ratesJson2String(JSONArray ratesJosn)
    {
        if(oConvertUtils.isEmpty(ratesJosn)|| ratesJosn.size()!=4) throw new CustomException("费率数组错误");

        List<String> feesList = new ArrayList<>(4);
        for(int i=0;i<4;i++)
        {
            BigDecimal data0 = (new BigDecimal((String)ratesJosn.getJSONObject(i).get("data0"))).setScale(2).multiply(new BigDecimal(100000)) ;
            BigDecimal data1 = (new BigDecimal((String)ratesJosn.getJSONObject(i).get("data1"))).setScale(2).multiply(new BigDecimal(100000)) ;

            StringBuilder s = new StringBuilder( HexUtil.encodeHexStr( ArrayUtil.reverse(ByteUtil.intToBytes(data0.intValue())) ).toUpperCase());
            s.append( HexUtil.encodeHexStr( ArrayUtil.reverse(ByteUtil.intToBytes(data1.intValue())) ).toUpperCase());
            feesList.add(s.toString());
        }
        return feesList;
    }
}
