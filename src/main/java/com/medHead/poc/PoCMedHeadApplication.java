package com.medHead.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

@SpringBootApplication(scanBasePackages = "com.medHead.poc")
public class PoCMedHeadApplication {

	private static final Logger logger = LoggerFactory.getLogger(PoCMedHeadApplication.class);

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		var context = SpringApplication.run(PoCMedHeadApplication.class, args);
		logger.info("Application démarrée avec succès.");
	}
}
