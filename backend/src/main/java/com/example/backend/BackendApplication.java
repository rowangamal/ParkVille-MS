package com.example.backend;

import com.example.backend.model.Driver;
import com.example.backend.repository.DriverRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BackendApplication.class, args);
//		Driver driver1 = context.getBean(Driver.class);
//		driver1.setId(1);
//		driver1.setUsername("amin");
//		driver1.setEmail("amencsed@gmail.com");
//		driver1.setPassword("123456789");
//		driver1.setLicensePlateNumber("1731");
//		driver1.setPaymentMethod("visa");
//		System.out.println(driver1.toString());
//
//
//		DriverRepo authRepo = context.getBean(DriverRepo.class);
//		authRepo.save(driver1);
//		System.out.println(authRepo.getAll());
	}
}
