package com.ajith.KnowTheRound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class KnowTheRoundApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowTheRoundApplication.class, args);
	}

}
