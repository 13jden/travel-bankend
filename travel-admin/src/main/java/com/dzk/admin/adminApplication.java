package com.dzk.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.dzk.admin")
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {"com.dzk.common", "com.dzk.admin", "com.dzk.web"})
@MapperScan({"com.dzk.admin.api", "com.dzk.web"})
public class adminApplication {

	public static void main(String[] args) {
		SpringApplication.run(adminApplication.class, args);
	}

}
