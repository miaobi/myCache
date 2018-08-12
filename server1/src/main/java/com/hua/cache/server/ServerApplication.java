package com.hua.cache.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan("com.hua.cache.server")
@MapperScan("com.hua.cache.server.data.mapper")
public class ServerApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ServerApplication.class, args);
	}
}
