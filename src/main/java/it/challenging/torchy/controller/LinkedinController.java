/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static it.challenging.torchy.util.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/finder")
public class LinkedinController {

    private static final Logger logger = LoggerFactory.getLogger(LinkedinController.class);

    @Value("${linkedin.token}")
    private String token;

    @GetMapping("/findAdAccounts")
    public String Find_ad_account() {
        try {
            HttpHeaders  header      = new HttpHeaders();
            RestTemplate lmsTemplate = new RestTemplate();
            //header.set(HttpHeaders.USER_AGENT, USER_AGENT_LMS_VALUE);
            header.set("Authorization", "Bearer " + token);
            header.set("Content-Type", "application/json");
            header.set("LinkedIn-Version", "202312");
            String response = lmsTemplate.exchange(MEMBER_CHANGES_LOGS, HttpMethod.GET,
                new HttpEntity(header), String.class).getBody();
            logger.info("Find Ad Accounts for Authenticated User response is" + response);
            return response;
        } catch (HttpStatusCodeException e) {
            logger.error(e.getMessage(), e);
            return e.getResponseBodyAsString();
        }
    }
}