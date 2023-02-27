package com.castro.drones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DronesApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(DronesApplication.class, args);
	}

}
