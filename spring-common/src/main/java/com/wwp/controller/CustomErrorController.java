package com.wwp.controller;


import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;


//所有的post 无路径都会处理到这里
@Component
public class CustomErrorController extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        //调用父类的方法，会自动获取内置的那些属性，如果你不想要，可以不调用这个
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        errorAttributes.put("success",false);
        errorAttributes.put("code",404);
        errorAttributes.remove("timestamp");
        errorAttributes.remove("status");
        errorAttributes.remove("error");
        // errorAttributes.remove("message");

        //添加自定义的属性

        // 你可以看一下这个方法的参数webRequest这个对象，我相信你肯定能发现好东西

        return errorAttributes;
    }
}