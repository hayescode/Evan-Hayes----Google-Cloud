package com.evanhayes.evanhayes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

///*TODO: uncomment this to deploy to remote server*/
//@SpringBootApplication
//public class EvanhayesApplication extends SpringBootServletInitializer {
//
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(EvanhayesApplication.class);
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(EvanhayesApplication.class, args);
//	}
//}

@SpringBootApplication
public class EvanhayesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvanhayesApplication.class, args);
	}
}
