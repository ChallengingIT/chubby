/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub;

import it.innotek.wehub.entity.TimedEmail;
import it.innotek.wehub.entity.timesheet.Email;
import it.innotek.wehub.repository.TimedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages={"it.innotek.wehub"})
@Controller
public class WeHubApplication {

	@Autowired private EmailSenderService   serviceEmail;
	@Autowired private TimedEmailRepository timedEmailRepository;

	@RequestMapping("/homepage")
	
	public String showHomePage(){

		List<TimedEmail> timedEmails = timedEmailRepository.findAll();

		for (TimedEmail email : timedEmails) {

			if (email.getInviata() != 1) {

				Email               emailDaInviare = new Email();
				Map<String, Object> mappa          = new HashMap<>();

				emailDaInviare.setFrom("sviluppo@inno-tek.it");
				emailDaInviare.setTo(email.getEmailOwner());
				mappa.put("nome", email.getNomeOwner());
				mappa.put("cognome", email.getCognomeOwner());
				mappa.put("nomeCandidato", email.getNomeCandidato());
				mappa.put("cognomeCandidato", email.getCognomeCandidato());
				mappa.put("mailCandidato", email.getEmailCandidato());
				mappa.put("cellCandidato", email.getCellCandidato());
				emailDaInviare.setProperties(mappa);
				emailDaInviare.setSubject(
					"Reminder per intervista successiva a " +
						email.getNomeCandidato() + " " +
						email.getCognomeCandidato()
				);
				emailDaInviare.setTemplate("reminder-email.html");

				serviceEmail.sendHtmlMessagePost(emailDaInviare, email.getDataAggiornamento(), email.getId());
			}
		}
		return "index";
	}

	
	public static void main(String[] args) {
		SpringApplication.run(WeHubApplication.class, args);
	}
}