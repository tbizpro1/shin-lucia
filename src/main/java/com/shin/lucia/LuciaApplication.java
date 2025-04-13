package com.shin.lucia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LuciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuciaApplication.class, args);
	}

}
