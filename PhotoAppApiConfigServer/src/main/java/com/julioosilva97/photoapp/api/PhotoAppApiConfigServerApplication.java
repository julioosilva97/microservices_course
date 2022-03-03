package com.julioosilva97.photoapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.core.env.Environment;

@EnableConfigServer
@SpringBootApplication
public class PhotoAppApiConfigServerApplication implements CommandLineRunner  {

	@Autowired
	private Environment env;
	
	
	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiConfigServerApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(env.getProperty("spring.cloud.config.server.git.username"));
	}

}
