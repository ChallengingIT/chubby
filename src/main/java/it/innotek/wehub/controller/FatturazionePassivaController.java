/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.FatturazionePassiva;
import it.innotek.wehub.entity.Fornitore;
import it.innotek.wehub.entity.StatoFP;
import it.innotek.wehub.repository.ClienteRepository;
import it.innotek.wehub.repository.FatturazionePassivaRepository;
import it.innotek.wehub.repository.StatoFPRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fatturazione/passiva")
public class FatturazionePassivaController {
    @Autowired
    private FatturazionePassivaRepository fatturazionePassivaRepository;
    @Autowired
    private ClienteRepository             clienteRepository;
    @Autowired
    private StatoFPRepository             statoFPRepository;

    private static final Logger logger = LoggerFactory.getLogger(FatturazionePassivaController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<FatturazionePassiva> getAll() {

        logger.info("Fatturazioni passive");

        return fatturazionePassivaRepository.findAll();
    }

    @GetMapping("/react/stato")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<StatoFP> getAllStato()
    {
        logger.info("Stati fatturazioni passive");

        return statoFPRepository.findAll();
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public FatturazionePassiva getById(@PathVariable("id") Integer id)
    {
        logger.info("Fatturazione passiva tramite id");

        return fatturazionePassivaRepository.findById(id).get();
    }

    @PostMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveFattPassiva(
        @RequestBody Map<String,String> fatturazione
    ){
        logger.info("Salva fatturazione passiva");

        try {

            FatturazionePassiva fatturazioneEntity = new FatturazionePassiva();

            if(fatturazione.get("id") != null) {
                fatturazioneEntity = fatturazionePassivaRepository.findById(Integer.parseInt(fatturazione.get("id"))).get();

                logger.debug("Fatturazione passiva trovata si procede in modifica");
            }

            trasformaMappaInFatturazione(fatturazioneEntity, fatturazione);

            fatturazionePassivaRepository.save(fatturazioneEntity);

            logger.debug("Fatturazione passiva salvata correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public FatturazionePassiva trasformaMappaInFatturazione(FatturazionePassiva fatturazione, Map<String,String> fatturazioneMap) {
        logger.debug("Trasforma mappa in fatturazione passiva");

        if (fatturazioneMap.get("stato") != null) {
            StatoFP stato = new StatoFP();
            stato.setId(Integer.parseInt(fatturazioneMap.get("stato")));
            fatturazione.setStato(stato);
        }

        if (fatturazioneMap.get("idFornitore") != null) {
            Fornitore fornitore = new Fornitore();
            fornitore.setId(Integer.parseInt(fatturazioneMap.get("idFornitore")));
            fatturazione.setFornitore(fornitore);
        }

        fatturazione.setDataFattura(fatturazioneMap.get("dataFattura") != null ? Date.valueOf(fatturazioneMap.get("dataFattura")) : null);
        fatturazione.setScadenza(fatturazioneMap.get("scadenza") != null ? Date.valueOf(fatturazioneMap.get("scadenza")) : null);
        fatturazione.setDescrizione(fatturazioneMap.get("descrizione") != null ? fatturazioneMap.get("descrizione") : null);
        fatturazione.setImponibile(fatturazioneMap.get("imponibile") != null ? fatturazioneMap.get("imponibile") : null);
        fatturazione.setImporto(fatturazioneMap.get("importo") != null ? fatturazioneMap.get("importo") : null);
        fatturazione.setIva(fatturazioneMap.get("iva") != null ? fatturazioneMap.get("iva") : null);
        fatturazione.setNote(fatturazioneMap.get("note") != null ? fatturazioneMap.get("note") : null);
        fatturazione.setTipologia(fatturazioneMap.get("tipologia") != null ? fatturazioneMap.get("tipologia") : null);
        fatturazione.setRiferimenti(fatturazioneMap.get("riferimenti") != null ? fatturazioneMap.get("riferimenti") : null);
        return fatturazione;
    }
}