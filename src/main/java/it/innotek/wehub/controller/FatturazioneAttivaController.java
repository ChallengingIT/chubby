/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.FatturazioneAttiva;
import it.innotek.wehub.entity.StatoFA;
import it.innotek.wehub.repository.ClienteRepository;
import it.innotek.wehub.repository.FatturazioneAttivaRepository;
import it.innotek.wehub.repository.StatoFARepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fatturazione/attiva")
public class FatturazioneAttivaController {

    @Autowired
    private FatturazioneAttivaRepository fatturazioneAttivaRepository;
    @Autowired
    private ClienteRepository            clienteRepository;
    @Autowired
    private StatoFARepository            statoFARepository;

    private static final Logger logger = LoggerFactory.getLogger(FatturazioneAttivaController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<FatturazioneAttiva> getAll()
    {
        logger.info("Fatturazioni attive");

        return fatturazioneAttivaRepository.findAll();
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public FatturazioneAttiva getById(@PathVariable("id") Integer id)
    {
        logger.info("Fatturazione attiva tramite id");

        return fatturazioneAttivaRepository.findById(id).get();
    }

    @GetMapping("/react/stato")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<StatoFA> getAllStato()
    {
        logger.info("Stati fatturazioni attive");

        return statoFARepository.findAll();
    }

    @PostMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveFattAttiva(
        @RequestBody Map<String,String> fatturazione
    ){
        logger.info("Salva fatturazione attiva");

        try {

            FatturazioneAttiva fatturazioneEntity = new FatturazioneAttiva();

            if(fatturazione.get("id") != null) {
                fatturazioneEntity = fatturazioneAttivaRepository.findById(Integer.parseInt(fatturazione.get("id"))).get();

                logger.debug("Fatturazione attiva trovata si procede in modifica");
            }

            trasformaMappaInFatturazione(fatturazioneEntity, fatturazione);

            fatturazioneAttivaRepository.save(fatturazioneEntity);

            logger.debug("Fatturazione attiva salvata correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public void trasformaMappaInFatturazione(FatturazioneAttiva fatturazione, Map<String,String> fatturazioneMap) {
        logger.debug("Trasforma mappa in fatturazione attiva");

        if (fatturazioneMap.get("stato") != null) {
            StatoFA stato = new StatoFA();
            stato.setId(Integer.parseInt(fatturazioneMap.get("stato")));
            fatturazione.setStato(stato);
        }

        fatturazione.setConsulente(fatturazioneMap.get("consulente") != null ? fatturazioneMap.get("consulente") : null);

        if (fatturazioneMap.get("idCliente") != null) {
            Cliente cliente = new Cliente();
            cliente.setId(Integer.parseInt(fatturazioneMap.get("idCliente")));

            fatturazione.setCliente(cliente);
        }

        fatturazione.setDataEmissione(fatturazioneMap.get("dataEmissione") != null ? Date.valueOf(fatturazioneMap.get("dataEmissione")) : null);
        fatturazione.setDataScadenza(fatturazioneMap.get("dataScadenza") != null ? Date.valueOf(fatturazioneMap.get("dataScadenza")) : null);
        fatturazione.setDescrizione(fatturazioneMap.get("descrizione") != null ? fatturazioneMap.get("descrizione") : null);
        fatturazione.setGiorniLavorati(fatturazioneMap.get("giorniLavorati") != null ? fatturazioneMap.get("giorniLavorati") : null);
        fatturazione.setImponibile(fatturazioneMap.get("imponibile") != null ? fatturazioneMap.get("imponibile") : null);
        fatturazione.setNFattura(fatturazioneMap.get("nFattura") != null ? fatturazioneMap.get("nFattura") : null);
        fatturazione.setOda(fatturazioneMap.get("oda") != null ? fatturazioneMap.get("oda") : null);
        fatturazione.setNote(fatturazioneMap.get("note") != null ? fatturazioneMap.get("note") : null);
        fatturazione.setOggetto(fatturazioneMap.get("oggetto") != null ? fatturazioneMap.get("oggetto") : null);
        fatturazione.setTariffa(fatturazioneMap.get("tariffa") != null ? fatturazioneMap.get("tariffa") : null);
        fatturazione.setTermine(fatturazioneMap.get("termine") != null ? fatturazioneMap.get("termine") : null);
        fatturazione.setTotaleConIva(fatturazioneMap.get("totaleConIva") != null ? fatturazioneMap.get("totaleConIva") : null);
    }
}