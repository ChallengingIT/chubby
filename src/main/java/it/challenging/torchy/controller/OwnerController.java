/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.entity.Owner;
import it.challenging.torchy.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerRepository ownerRepository;

    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @GetMapping
    ////@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Owner> getAllOwner() {
        logger.info("Lista owner");

        return ownerRepository.findAll();
    }

    @PostMapping("/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveCandidato(
            @RequestBody Map<String,String> ownerMap
    ) {
        logger.info("Salva candidato");

        try {
            Owner ownerEntity = new Owner();

            if(ownerMap.get("id") != null) {
                ownerEntity = ownerRepository.findById(Integer.parseInt(ownerMap.get("id"))).get();

                logger.debug("Candidato trovato si procede in modifica");
            }

            trasformaMappaInOwner(ownerEntity, ownerMap);

            if (controllaDescrizioneDuplicata(ownerEntity.getDescrizione())) {

                if (ownerEntity.getId() == null) {
                    logger.debug("Owner duplicato, denominazione simile già presente");

                    return "DUPLICATO";
                }
            }

            ownerRepository.save(ownerEntity);

            logger.debug("Owner salvato correttamente");

            return ""+ownerEntity.getId();

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public boolean controllaDescrizioneDuplicata(String descrizione) {
        logger.debug("Owner controlla descrizione duplicata");

        List<Owner> owner = ownerRepository.findByDescrizione(descrizione);
        return ((null != owner) && !owner.isEmpty());
    }

    public void trasformaMappaInOwner(Owner owner, Map<String,String> ownerMap) {

        logger.info("Trasforma mappa in owner");

        owner.setNome(ownerMap.get("nome") != null ? ownerMap.get("nome") : null);
        owner.setCognome(ownerMap.get("cognome") != null ? ownerMap.get("cognome") : null);
        owner.setEmail(ownerMap.get("email") != null ? ownerMap.get("email") : null);
        owner.setDescrizione(ownerMap.get("descrizione") != null ? ownerMap.get("descrizione") : null);

    }
}
