/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.repository.*;
import it.challenging.torchy.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private StatoARepository       statoARepository;
    @Autowired
    private OwnerRepository        ownerRepository;
    @Autowired
    private CandidatoRepository    candidatoRepository;
    @Autowired
    private ClienteRepository      clienteRepository;
    @Autowired
    private AssociazioniRepository associazioniRepository;
    @Autowired
    private NeedRepository         needRepository;

    private static final Logger                         logger     = LoggerFactory.getLogger(AIController.class);
    private static final Map<String, ArrayList<String>> dictionary = new HashMap<>();

    @GetMapping
    public ResponseEntity<?> findById(
            @RequestParam("message") String message
    ){
        logger.info("AI Message");

        //if (message.contains(dictionary.keySet()))

        return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT);
    }

}
