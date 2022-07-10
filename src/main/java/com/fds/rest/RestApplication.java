package com.fds.rest;

import com.fds.rest.config.AppProperties;
import com.fds.rest.config.FdsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.fds.rest.repository")
@EnableConfigurationProperties({AppProperties.class, FdsProperties.class})
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

}
