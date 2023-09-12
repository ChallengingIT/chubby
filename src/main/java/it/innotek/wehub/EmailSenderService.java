/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub;

import it.innotek.wehub.entity.timesheet.Email;
import it.innotek.wehub.service.TimedEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class EmailSenderService {

    private final JavaMailSender       emailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    private TimedEmailService serviceTimedEmail;

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

    public void sendHtmlMessagePost(
        Email email,
        LocalDateTime data,
        Long idEmail
    ){
        if (null != data) {
            Date javaDate = Date.from(data.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        MimeMessage message = emailSender.createMimeMessage();

                        message.setSentDate(javaDate);

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
                        helper.setSentDate(javaDate);
                        helper.setSubject(email.getSubject());

                        String html = templateEngine.process(email.getTemplate(), context);

                        helper.setText(html, true);

                        serviceTimedEmail.updateEmailTemporizzata(idEmail, 1);

                        emailSender.send(message);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }, javaDate);
        }
    }
}