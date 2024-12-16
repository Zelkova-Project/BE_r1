package com.zelkova.zelkova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing // @CreatedDate,@LastModifiedDate
public class ZelkovaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZelkovaApplication.class, args);
	}

}
