package it.challenging.torchy.controller;

import it.challenging.torchy.EmailSenderService;
import it.challenging.torchy.entity.Email;
import it.challenging.torchy.request.EmailRequest;
import it.challenging.torchy.util.UtilLib;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailSenderService serviceEmail;

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @PostMapping("/send")
    public String sendEmail(@Valid @RequestBody EmailRequest emailRequest){
        try {
            logger.debug("Invio email");

            String[] destinatari = emailRequest.getDestinatari().split(";");

            for (String destinatario : destinatari) {

                Email email = UtilLib.getEmail(destinatario, emailRequest.getNote(), emailRequest.getOggetto());

                serviceEmail.sendHtmlMessage(email);

            }

        } catch (Exception e){
            logger.error(e.toString());
            return "ERRORE";
        }
        return "OK";
    }

}