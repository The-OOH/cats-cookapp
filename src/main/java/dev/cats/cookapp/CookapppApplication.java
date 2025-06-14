package dev.cats.cookapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CookapppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookapppApplication.class, args);
	}

}
