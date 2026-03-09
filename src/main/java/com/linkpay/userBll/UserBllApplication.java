package com.linkpay.userBll;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.linkpay")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, DynamicDataSourceAutoConfiguration.class})
public class UserBllApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.linkpay.userBll.UserBllApplication.class, args);
	}

}
