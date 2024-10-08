package com.example.demo;

import com.example.demo.Configurations.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.demo.Models"})
@EnableConfigurationProperties(RsaKeyProperties.class)
public class CarebnbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarebnbApplication.class, args);
	}

}
