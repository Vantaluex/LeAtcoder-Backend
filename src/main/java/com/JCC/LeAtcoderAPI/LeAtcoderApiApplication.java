package com.JCC.LeAtcoderAPI;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeAtcoderApiApplication {

	public static void main(String[] args) {
		Dotenv.load();
		SpringApplication.run(LeAtcoderApiApplication.class, args);
	}

}
