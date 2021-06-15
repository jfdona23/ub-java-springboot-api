package ub.prog3.exposer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExposerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExposerApplication.class, args);

		Logger logger = LoggerFactory.getLogger(ExposerApplication.class);
		logger.info("Iniciando Backend Exposer");
	}

}
