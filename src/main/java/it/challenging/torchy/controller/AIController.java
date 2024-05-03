/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.repository.*;
import it.challenging.torchy.util.Constants;
import it.challenging.torchy.util.UtilLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    private static final Logger                         logger       = LoggerFactory.getLogger(AIController.class);
    private static final ArrayList<String>              keyOrder     = new ArrayList<>();
    private static final ArrayList<String>              keyLimit     = new ArrayList<>();
    private static       ArrayList<String>              keyCandidati = new ArrayList<>();
    private static final LinkedHashMap<ArrayList<String>, ArrayList<String>> dictionary = new LinkedHashMap<>();

    @GetMapping
    public ResponseEntity<?> findById(
            @RequestParam("message") String message
    ){
        logger.info("AI Message");
        boolean ricerca = false;
        boolean ordina  = false;
        boolean candidati = false;
        boolean aziende = false;
        boolean need = false;
        boolean keyPeople = false;

        UtilLib.caricaDictionary(dictionary);
        UtilLib.caricaKeyOrder(keyOrder);
        UtilLib.caricaKeyLimit(keyLimit);
        keyCandidati = UtilLib.getElementByIndex(dictionary, 1); //candidati

        for  (ArrayList<String> s : dictionary.keySet()) {
            if (UtilLib.findInArray(message, s)) {
                switch(s.get(0)){
                    case "candidati":
                        candidati = true;
                        break;
                    case "aziende":
                        aziende = true;
                        break;
                    case "keyPeople":
                        keyPeople = true;
                        break;
                    case "need":
                        need = true;
                        break;
                    default:
                        break;
                }
            }
        }

        if (UtilLib.findInArray(message, keyOrder)){
            ordina = true;
        }

        if (candidati && need) {

        } else if (aziende && need) {

        } else if (keyPeople && need) {

        } else if (keyPeople && aziende) {

            UtilLib.getElementByIndex(dictionary, 0); //aziende
            UtilLib.getElementByIndex(dictionary, 1); //candidati
            UtilLib.getElementByIndex(dictionary, 2); //keyPeople
            UtilLib.getElementByIndex(dictionary, 3); //need

        } else if (candidati) {

            for (String s : keyCandidati) {
                if (message.toLowerCase().contains(s)) {

                    switch(s) {
                        case Constants.CANDIDATI_NOME:
                            String nome = UtilLib.cercaNome(message, candidatoRepository.findAllNames());

                            if (null == nome) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_NOME);
                            } else {
                                return ResponseEntity.ok(candidatoRepository.findByNome(nome));
                            }

                        case Constants.CANDIDATI_CHIAMANO:
                            String nome_chiamano = message.split(Constants.CANDIDATI_CHIAMANO)[1];

                            return ResponseEntity.ok(candidatoRepository.findByNome(nome_chiamano.trim()));

                        case Constants.CANDIDATI_COGNOME:
                            String cognome = message.split(Constants.CANDIDATI_COGNOME)[1];

                            return ResponseEntity.ok(candidatoRepository.findByCognome(cognome.trim()));

                    }

                    if (ordina) {

                    }
                }
            }

        } else if (need) {

        } else if (aziende) {

        } else if (keyPeople) {

        }

        return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT);
    }

}
