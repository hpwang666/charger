package com.wwp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
@MapperScan("com.wwp.mapper")
public class MainApplication {


	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}
