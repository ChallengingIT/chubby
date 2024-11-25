/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"it.challenging.torchy"})
public class TorchyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TorchyApplication.class, args);
	}
}