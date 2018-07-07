package com.highrock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@ServletComponentScan   //使用过滤器
@Configuration
@EnableAspectJAutoProxy //开启aop
@EnableTransactionManagement //开启事务
@ComponentScan(basePackages = "com.highrock")
@EnableAutoConfiguration
@MapperScan("com.highrock.mapper")
@EnableWebMvc  //
public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
