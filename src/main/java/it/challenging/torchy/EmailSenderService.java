/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy;

import it.challenging.torchy.entity.Email;
import it.challenging.torchy.repository.TimedEmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class EmailSenderService {

    private final JavaMailSender       emailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    private TimedEmailRepository timedEmailRepository;

    public EmailSenderService(
        JavaMailSender emailSender,
        SpringTemplateEngine templateEngine
    ){
        this.emailSender    = emailSender;
        this.templateEngine = templateEngine;
    }

    public void sendHtmlMessage(Email email) throws MessagingException {
        MimeMessage       message = emailSender.createMimeMessage();
        Context           context = new Context();
        MimeMessageHelper helper  =
            new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
            );

        context.setVariables(email.getProperties());
        helper.setFrom(email.getFrom());
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());

        String html = templateEngine.process(email.getTemplate(), context);

        helper.setText(html, true);

        emailSender.send(message);
    }
}