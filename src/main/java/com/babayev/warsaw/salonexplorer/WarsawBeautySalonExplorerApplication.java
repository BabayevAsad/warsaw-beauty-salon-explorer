package com.babayev.warsaw.salonexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages ="com.babayev")
@EnableJpaRepositories(basePackages = "com.babayev")
@EntityScan(basePackages = "com.babayev")
@EnableJpaAuditing
public class WarsawBeautySalonExplorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarsawBeautySalonExplorerApplication.class, args);
	}

}