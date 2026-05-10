package com.example.prueba_tecnica_backend_2;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.banco.api", "com.example.prueba_tecnica_backend_2"})
@EntityScan(basePackages = "com.banco.api.entity")
@EnableJpaRepositories(basePackages = "com.banco.api.repository")
public class PruebaTecnicaBackend2Application {

	public static void main(String[] args) {
		SpringApplication.run(PruebaTecnicaBackend2Application.class, args);
	}
}
