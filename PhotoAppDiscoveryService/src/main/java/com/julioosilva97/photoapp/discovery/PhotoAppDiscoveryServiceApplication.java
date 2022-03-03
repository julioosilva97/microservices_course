package com.julioosilva97.photoapp.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;

@EnableEurekaServer
@SpringBootApplication
public class PhotoAppDiscoveryServiceApplication implements CommandLineRunner{

	@Autowired
	private Environment env;
	
	
	public static void main(String[] args) {
		SpringApplication.run(PhotoAppDiscoveryServiceApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(env.getProperty("spring.security.user.name"));
	}
}
