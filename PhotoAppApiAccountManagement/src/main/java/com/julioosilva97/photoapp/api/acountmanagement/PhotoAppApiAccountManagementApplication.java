package com.julioosilva97.photoapp.api.acountmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PhotoAppApiAccountManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiAccountManagementApplication.class, args);
	}

}
