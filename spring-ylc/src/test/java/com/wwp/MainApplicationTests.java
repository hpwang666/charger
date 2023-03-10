package com.wwp;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.checksum.crc16.CRC16XModem;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.wwp.entity.*;
import com.wwp.mapper.YlcChargerMapper;
import com.wwp.service.impl.YlcChargerServiceImpl;
import com.wwp.service.impl.YlcUserLogicServiceImpl;
import com.wwp.util.YlcStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.swagger.models.auth.In;
import org.apache.http.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import static com.wwp.util.YlcStringUtils.parseByte2HexStr;



@ComponentScan(basePackages = {"com.wwp.common.annotation"})
@SpringBootTest(classes= MainApplication.class)
public class MainApplicationTests {
    @Resource
    YlcUserLogicServiceImpl ylcUserLogicServiceImpl;
//
    @Resource
    YlcChargerServiceImpl ylcChargerService;

    @Resource
    YlcChargerMapper ylcChargerMapper;


    public void testBean()
    {
        YlcCharger ylcCharger = ylcChargerService.getYlcChargerBySerialNum("32010600213533");
        Date d = DateUtil.parse("2023-02-12 16:45:23");
        if(ylcCharger!=null){
            System.out.println(DateUtil.format(ylcCharger.getUpdateTime(),DatePattern.NORM_DATETIME_PATTERN) +"   "+DateUtil.between(ylcCharger.getUpdateTime(), new Date(),DateUnit.SECOND));
        }

    }

    @Test
    public void test()
    {
        YlcCharger charger = ylcChargerMapper.getChargerBySerialNum("580074859933074");

        charger.setDelFlag(3);
        ylcChargerMapper.update(charger);
    }

    public void testFeeModel() throws Exception
    {
        String fee="000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        if(fee.length()!=96) throw new ParseException("??????????????????");

        List<String> timeZone = new ArrayList<>(48);
        for (int i = 0; i < 48; i++) {
            timeZone.add(StrUtil.sub(fee,i*2,(i*2)+2));
        }

        String s = timeZone.get(0);
        JSONArray jsonArray = new JSONArray();
        JSONObject obj = new JSONObject();

        Date endTime = DateUtil.parse("00:00","HH:mm");
        obj.put("startTime",DateUtil.format(endTime, "HH:mm"));

        for(int i =0;i<timeZone.size();i++)
        {
            if(!timeZone.get(i).equals(s)){
                obj.put("endTime",DateUtil.format(endTime, "HH:mm"));
                obj.put("rate",s);
                jsonArray.add(obj);
                obj = new JSONObject();
                obj.put("startTime",DateUtil.format(endTime, "HH:mm"));
                s= timeZone.get(i);
            }
            if(i==timeZone.size()-1){
                obj.put("endTime","23:59");
                obj.put("rate",timeZone.get(i));
                jsonArray.add(obj);
            }
            endTime =DateUtil.offsetMinute(endTime,30);

        }
        obj = new JSONObject();
        obj.put("timeZone",jsonArray);
        System.out.println(obj);

    }


    public void time2FeeModel() throws Exception
    {
        String timeZone =  "{\"timeZone\":[{\"rate\":\"00\",\"startTime\":\"00:00\",\"endTime\":\"00:30\"},{\"rate\":\"01\",\"startTime\":\"00:30\",\"endTime\":\"01:00\"},{\"rate\":\"05\",\"startTime\":\"01:00\",\"endTime\":\"23:59\"}]}";
       // String timeZone =  "{\"timeZone\":[{\"rate\":\"00\",\"startTime\":\"00:00\",\"endTime\":\"23:59\"}]}";

        JSONArray f= JSON.parseObject(timeZone).getJSONArray("timeZone");
        Date start,end;
        Integer index =0;
        StringBuilder feeBuilder = new StringBuilder(100);
        for(int i=0;i<f.size();i++)
        {
            start = DateUtil.parse((String)f.getJSONObject(i).get("startTime"),"HH:mm");
            end = DateUtil.parse((String)f.getJSONObject(i).get("endTime"),"HH:mm");
            while(!DateUtil.isSameTime(start,end) && index<48){
                feeBuilder.append((String)f.getJSONObject(i).get("rate"));
                index++;
                start =DateUtil.offsetMinute(start,30);
            }

        }
        if(feeBuilder.length()!=96)
            throw new ParseException("????????????????????????");

        System.out.println(feeBuilder.length()+feeBuilder.toString());


    }
    public  void testServer()
    {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {
                // ?????? ByteToMessageDecoder ??????????????????????????????
                // channel.pipeline().addLast(new MyIntegerDecoder()).addLast(new IntegerProcessorHandler());
                // ?????? ReplayingDecoder ????????????????????????
               // channel.pipeline().addLast(new MyIntegerDecoder2()).addLast(new IntegerProcessorHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        for (int j = 0;j < 20;j++){
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeInt(j);
            channel.writeInbound(byteBuf);
        }
        try{
            Thread.sleep(2000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

    }



    public void testBUffer()
    {
        ByteBuffer buffer = ByteBuffer.allocate(6);
        // position: 0, limit: 6, capacity: 6

        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        // position: 3, limit: 6, capacity: 6

        buffer.mark();  // ???????????????????????????????????????
        // position: 3, limit: 6, capacity: 6

        buffer.put((byte) 4); // ??????????????????????????????
        // position: 4, limit: 6, capacity: 6

        buffer.reset(); // ???buffer?????????????????????????????????Mark????????????
        // position: 3, limit: 6, capacity: 6

        buffer.flip();  // ?????????????????????????????????????????????????????????
        // position: 0, limit: 3, capacity: 6

        System.out.println(buffer.get()+" "+buffer.get());  // ??????????????????????????????????????????
        buffer.mark();
        // position: 1, limit: 3, capacity: 6

        buffer.get(); // ??????????????????????????????
        // position: 2, limit: 3, capacity: 6

        buffer.reset(); // ????????????????????????????????????mark?????????
    }


    public void testString()
    {
        short[] src={0x22,0,0x5};
        String s = YlcStringUtils.bcd2string(src);
        System.out.println(s.length()+" " +s.toString());

        short crcL,crcH;

        short[] src0={0x92,0x01,0x00,0x13,0x32,0x01,0x06,0x00,0x21,0x35,0x33,0x01,0x20,0x23,0x01,0x15,0x21,0x14,0x00,0x01,0x32,0x01,0x06,0x00,0x21,0x35,0x33,0x01,0x03,0x02,0x01,0x43,0x09,0x28,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x33,0x00,0x00,0x00,0xb8,0x24,0x00,0x00,0xb8,0x24,0x00,0x00,0x28,0x55,0x00,0x00,0x00,0x00};//0x68,0x90};
        //crcL: 117  crcH: 132 166 87

        byte[] src1={ 0x29,0x00,0x00,0x03,0x32,0x01,0x06,0x00,0x21,0x35,0x33,0x01,0x00};
        // crcL: 57  crcH: 180 180 65
        //       39  B4  B4 41
        //AUTH:  6822250000013201060021353301010F6C796B2D56323900001920168043024800000000C554
        //HEART: 680d050000033201060021353301005718

        short[] src_101={0x25,0x00,0x00,0x01,0x32,0x01,0x06,0x00,0x21,0x35,0x33,0x01,0x01,0x0F,0x6C,0x79,0x6B,0x2D,0x56,0x32,0x39,0x00,0x00,0x19,0x20,0x16,0x80,0x43,0x02,0x48,0x00,0x00,0x00,0x00};//,0xC5,0x54
      //68 22 40 00 00 01 32 01 06 00 21 35 33 01 01 0F 6C 79 6B 2D 56 32 39 00 00 19 20 16 80 43 02 48 00 00 00 00 D2 6D

        CRC16XModem crc16 = new CRC16XModem();
        crc16.update(src1);
        String hexValue = crc16.getHexValue(true);
        System.out.println(hexValue);

        //CRC16Checksum.getHexValue(true);
        System.out.println(" "+YlcStringUtils.crc(src0,src0.length));
       crcL = (short)(YlcStringUtils.crc(src1,src1.length)&0xff);
        crcH = (short)(YlcStringUtils.crc(src1,src1.length)>>8&0xff);
        System.out.printf(" crcH: %02x  crcL: %02x ",crcH,crcL);




        byte[] ss= YlcStringUtils.string2bcd("9345");
        System.out.println("ss: "+(ss[0]&0xff)+" "+(ss[1]&0xff));

        int[] src2={0xA0,0x86,0x01,0x00};
        int[] src4={0X00,0X00,0X00,0X00,0XD1,0XB2,0XC3,0XD4};
        String s1= "gBoGAJxAAAA=";
        ByteBuffer buf = ByteBuffer.allocate(src4.length);
        IntStream.of(src4).forEach(i -> buf.put((byte)i));
        String base64encodedString = Base64.getEncoder().encodeToString(buf.array());
        System.out.println("base64 encode: "+base64encodedString);
        byte[] base64decodedBytes = Base64.getDecoder().decode(s1);
        System.out.println("base64 decode: "+parseByte2HexStr(base64decodedBytes));

    }


    public void testHutool()
    {
        byte[] src2={0x50,0x36,0x01,0x00};
        Integer I1=100000;
        String str3 = "A0860100";

        //??????????????????????????????
        String str2 = HexUtil.encodeHexStr(src2);
        System.out.println(str2);

        //??????????????????????????????
        byte[] b2 = HexUtil.decodeHex(str3);

        //????????????????????????int ????????????????????????4??????????????????????????????4?????????
        //?????????????????? ??????????????????
        System.out.println(ByteUtil.bytesToInt(b2));

        System.out.println(HexUtil.encodeHexStr(ByteUtil.intToBytes(I1)));//?????????????????????4?????????,????????????
        System.out.println(HexUtil.toHex(I1));//???????????????????????????

        //???????????????????????????  ?????????????????????????????????
        String str4 = HexUtil.encodeHexStr(ArrayUtil.reverse(b2));
        System.out.println("b2 reverse: "+str4+" "+ HexUtil.hexToLong(str4));


    }

    public void testTime()
    {
        short[] t ={0x98,0xB7,0x0E,0x11,0x10,0x03,0x14};


           Date d1 = YlcStringUtils.cp56Time2Date(t);
           System.out.println(d1);

        YlcChargerStatus ylcChargerStatus=new YlcChargerStatus();
        ylcChargerStatus.setUpdateTime(new Date());
        ylcChargerStatus.setOrderNum("32010600213533012023020819120001");

        //ylcChargerStatusMapper.update(ylcChargerStatus);
//        ylcUserLogicalMapper.updateUserAmount("1111",new BigDecimal("3410045").multiply(new BigDecimal("100")));
//        BigDecimal b1= ylcUserLogicalMapper.queryUserAmount("1111").divide(new BigDecimal("100")).setScale(2);

      //  System.out.println(b1.toString());




    }


    public void testThreads()
    {
        DefaultEventExecutorGroup eventLoop = new DefaultEventExecutorGroup(3);

        DefaultPromise<Integer> promise = new DefaultPromise(eventLoop.next());
        DefaultPromise<Integer> promise1 = new DefaultPromise(eventLoop.next());
        DefaultPromise<Integer> promise2 = new DefaultPromise(eventLoop.next());
        DefaultPromise<Integer> promise3 = new DefaultPromise(eventLoop.next());
        DefaultPromise<Integer> promise4 = new DefaultPromise(eventLoop.next());
        DefaultPromise<Integer> promise5 = new DefaultPromise(eventLoop.next());

      // System.out.println(promise.s()) ;

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            promise.setSuccess(8);
            System.out.println(Thread.currentThread());
            //promise.setFailure(new IllegalStateException()); ?????????????????????
        }, "t1").start();

        new Thread(() -> {
//            promise.addListener(future -> {//???????????????????????????????????????????????????
//                Object futureNow = future.getNow();
//                System.out.println(Thread.currentThread());
//                System.out.println("future result: "+futureNow);
//            });
            try{
                Integer i =  (Integer) promise.get(2000, TimeUnit.MILLISECONDS);

                System.out.println(Thread.currentThread()+"i: "+i);
            }
           catch(TimeoutException e)
           {
               e.printStackTrace();
           }
            catch(InterruptedException | ExecutionException e2)
            {
                e2.printStackTrace();
            }
        }, "t2").start();



        System.out.println("start");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void  testFuture()
    {
        DefaultEventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(5);
        Future<StringBuilder> future = eventExecutorGroup.next().submit(new Callable<StringBuilder>() {

            @Override
            public StringBuilder call() {
                System.out.println("thread: " + Thread.currentThread().getName());
                try{
                    Thread.sleep(4000);
                    return new StringBuilder("work");
                }
                catch(InterruptedException e)
                {
                    return new StringBuilder(e.toString());
                }

            }
        });

        future.addListener(new FutureListener<StringBuilder>() {

            @Override
            public void operationComplete(Future<StringBuilder> future) throws Exception {
                if (future.isSuccess()) {
                    StringBuilder respone = ((StringBuilder) future.get());
                    System.out.println("respone: " + respone+" "+Thread.currentThread().getName());
                }
            }
        });


        System.out.println("start: "+Thread.currentThread().getName());
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}

