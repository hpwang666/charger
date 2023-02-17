package com.wwp;

import com.wwp.netty.NettyServerListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@SpringBootApplication
@MapperScan({"com.wwp.mapper"})
@ComponentScan(value = {"com.wwp.common.interceptor","com.wwp.netty","com.wwp"})
public class MainApplication extends SpringBootServletInitializer implements CommandLineRunner {
    @Resource
    private NettyServerListener nettyServerListener;



    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... strings) {
        nettyServerListener.start();

    }
}
