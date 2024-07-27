package com.molla.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EntityScan({"com.molla.common.entity"})
public class MollaBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(MollaBackEndApplication.class, args);
	}

}
