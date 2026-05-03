package com.github.lilianjaf.restaurante_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.github.lilianjaf.restaurante_service",
		"com.github.lilianjaf.usuario_service"
})
public class RestauranteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestauranteServiceApplication.class, args);
	}

}
