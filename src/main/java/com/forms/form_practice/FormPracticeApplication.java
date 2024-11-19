package com.forms.form_practice;

import java.io.File;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FormPracticeApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FormPracticeApplication.class);

		ApplicationArguments argsOptions = new DefaultApplicationArguments(args);
		String dirFile = "";

		if (argsOptions.containsOption("dataDir")) {
			dirFile = argsOptions.getOptionValues("dataDir").get(0);

			// Create directory if it does not exist
			File directory = new File(dirFile);
			if (!directory.exists()) {
				boolean isCreated = directory.mkdirs(); // Create directories
				if (isCreated) {
					System.out.println("Directory created: " + dirFile);
				} else {
					System.err.println("Failed to create directory: " + dirFile);
				}
			} else {
				System.out.println("Directory already exists: " + dirFile);
			}
		} else {
			System.err.println("No directory specified using --dataDir=<directory>");
		}

		System.setProperty("dataDir", dirFile);

		app.run(args);
	}
}