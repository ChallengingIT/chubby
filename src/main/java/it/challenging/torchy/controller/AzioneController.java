
/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.entity.*;
import it.challenging.torchy.repository.AzioneRepository;
import it.challenging.torchy.repository.KeyPeopleRepository;
import it.challenging.torchy.repository.TipologiaAzRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/azioni")
public class AzioneController {

    @Autowired
    private KeyPeopleRepository keyPeopleRepository;
    @Autowired
    private AzioneRepository azioneRepository;
    @Autowired
    private TipologiaAzRepository tipologiaAzRepository;

    private static final Logger logger = LoggerFactory.getLogger(AzioneController.class);

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<AzioneKeyPeople> getAll(@PathVariable("id") Integer id) {
        logger.info("Azioni del keyPeople");

        return azioneRepository.findByKeyPeople(id);
    }

    @GetMapping("/react/tipologie")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<TipologiaAz> getTipologie() {
        logger.info("Tipologie azioni");

        return tipologiaAzRepository.findAllByOrderByIdAsc();
    }


    @PostMapping("/react/salva/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public ResponseEntity<String> saveAzione(
        @PathVariable("id") Integer id,
        @RequestBody Map<String, String> azioneMap
    ){
        logger.info("Salva azione");

        try {
            AzioneKeyPeople azioneEntity = new AzioneKeyPeople();

            trasformaMappaInAzione(azioneEntity, azioneMap);

            azioneRepository.save(azioneEntity);

            KeyPeople keyPeople = keyPeopleRepository.findById(id).get();

            keyPeople.getAzioni().add(azioneEntity);

            keyPeopleRepository.save(keyPeople);

            logger.debug("Azione salvata correttamente");

            return ResponseEntity.ok("OK");

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return ResponseEntity.ok("ERRORE");
        }
    }

    public void trasformaMappaInAzione(AzioneKeyPeople azione, Map<String,String> azioneMap) {

        logger.debug("Trasforma mappa in azione");

        azione.setDataModifica(azioneMap.get("data") != null ? Date.valueOf(azioneMap.get("data")) : null);

        if (azioneMap.get("idTipologia") != null) {
            TipologiaAz tipologia = new TipologiaAz();
            tipologia.setId(Integer.parseInt(azioneMap.get("idTipologia")));

            azione.setTipologia(tipologia);
        }

        azione.setNote(azioneMap.get("note") != null ? azioneMap.get("note") : null);

    }
}
