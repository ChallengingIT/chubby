/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub;

import it.innotek.wehub.repository.TimedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication(scanBasePackages={"it.innotek.wehub"})
@Controller
public class WeHubApplication {

	@Autowired private EmailSenderService   serviceEmail;
	@Autowired private TimedEmailRepository timedEmailRepository;

	@RequestMapping("/homepage")
	public String showHomePage(){

		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(WeHubApplication.class, args);
	}
}