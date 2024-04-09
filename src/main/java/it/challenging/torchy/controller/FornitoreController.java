
/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.repository.CandidatoRepository;
import it.challenging.torchy.repository.FornitoreRepository;
import it.challenging.torchy.entity.Fornitore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fornitori")
public class FornitoreController {

    @Autowired
    private FornitoreRepository           fornitoreRepository;
    @Autowired
    private CandidatoRepository           candidatoRepository;

    private static final Logger logger = LoggerFactory.getLogger(FornitoreController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Fornitore> getAll() {
        logger.info("Fornitori");

        return fornitoreRepository.findAll();
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Fornitore getById(@PathVariable("id") Integer id) {
        logger.info("Fornitore tramite id");

        return fornitoreRepository.findById(id).get();
    }

    @DeleteMapping("/react/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteFornitore(@PathVariable Integer id) {
        logger.info("Elimina fornitore tramite id");

        try {

            fornitoreRepository.deleteById(id);

            logger.info("Fornitore eliminato correttamente");

            return "OK";
        } catch (Exception e) {
            return "ERRORE";
        }
    }

    @PostMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public ResponseEntity<String> saveFornitore(
        @RequestBody Map<String, String> fornitoreMap
    ){
        logger.info("Salva fornitore");

        try {
            Fornitore fornitoreEntity = new Fornitore();

            if(fornitoreMap.get("id") != null) {
                fornitoreEntity = fornitoreRepository.findById(Integer.parseInt(fornitoreMap.get("id"))).get();

                logger.debug("Fornitore trovato si procede in modifica");
            }

            trasformaMappaInFornitore(fornitoreEntity, fornitoreMap);

            fornitoreRepository.save(fornitoreEntity);

            logger.debug("Fornitore salvato correttamente");

            return ResponseEntity.ok("OK");

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return ResponseEntity.ok("ERRORE");
        }
    }

    public void trasformaMappaInFornitore(Fornitore fornitore, Map<String,String> clienteMap) {

        logger.debug("Trasforma mappa in fornitore");

        fornitore.setEmail(clienteMap.get("email") != null ? clienteMap.get("email") : null);
        fornitore.setReferente(clienteMap.get("referente") != null ? clienteMap.get("referente") : null);
        fornitore.setDenominazione(clienteMap.get("denominazione") != null ? clienteMap.get("denominazione") : null);
        fornitore.setCellulare(clienteMap.get("cellulare") != null ? clienteMap.get("cellulare") : null);
        fornitore.setCitta(clienteMap.get("citta") != null ? clienteMap.get("citta") : null);
        fornitore.setCodice(clienteMap.get("codice") != null ? clienteMap.get("codice") : null);
        fornitore.setPi(clienteMap.get("pi") != null ? clienteMap.get("pi") : null);
    }
}
