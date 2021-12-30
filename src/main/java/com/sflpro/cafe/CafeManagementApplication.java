package com.sflpro.cafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing
@SpringBootApplication
@EnableTransactionManagement
public class CafeManagementApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(
				CafeManagementApplication.class, args);
	}

}
