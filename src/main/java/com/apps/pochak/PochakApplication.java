package com.apps.pochak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableElasticsearchAuditing
@EnableJpaAuditing
public class PochakApplication {

	public static void main(String[] args) {
		SpringApplication.run(PochakApplication.class, args);
	}

}
