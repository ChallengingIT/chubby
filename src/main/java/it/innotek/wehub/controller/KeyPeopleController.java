/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.EmailSenderService;
import it.innotek.wehub.entity.Attivita;
import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.KeyPeople;
import it.innotek.wehub.entity.Owner;
import it.innotek.wehub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/keypeople")
public class KeyPeopleController {

    @Autowired
    private KeyPeopleRepository keyPeopleRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TipologiaRepository tipologiaRepository;
    @Autowired
    private LivelloRepository livelloRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private EmailSenderService serviceEmailSender;
    @Autowired
    private AttivitaRepository          attivitaRepository;

    private static final Logger logger = LoggerFactory.getLogger(KeyPeopleController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<KeyPeople> getAll() {
        logger.info("Key people");

        return keyPeopleRepository.findAll();
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public KeyPeople getById(@PathVariable("id") Integer id) {
        logger.info("Key people tramite id");

        return keyPeopleRepository.findById(id).get();
    }

    @PostMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveKeyPeople(
        @RequestBody Map<String,String> keyPeopleMap
    ){
        logger.info("Salva key people");

        try {
            KeyPeople keyPeopleEntity = new KeyPeople();

            if(keyPeopleMap.get("id") != null) {
                keyPeopleEntity = keyPeopleRepository.findById(Integer.parseInt(keyPeopleMap.get("id"))).get();

                logger.debug("Key people trovato si procede in modifica");
            }

            trasformaMappaInKeyPeople(keyPeopleEntity, keyPeopleMap);

            if (( null == keyPeopleEntity.getId() ) && controllaDenominazioneDuplicata(keyPeopleEntity.getNome())) {
                logger.debug("Key people duplicato");

                return "DUPLICATO";
            }

            keyPeopleRepository.save(keyPeopleEntity);

            logger.debug("Key people salvato correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @RequestMapping("/react/attivita/{idKeyPeople}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Attivita> getAttivitaAzienda(
        @PathVariable("idKeyPeople") Integer idKeyPeople
    ){
        logger.info("Attività del key people");

        try {

            return attivitaRepository.findByKeyPeople_Id(idKeyPeople);

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return null;
        }
    }

    @DeleteMapping("/react/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteKeypeople(
        @PathVariable("id") Integer id
    ){
        logger.info("Elimina key people");

        try {

            List<Attivita> attivita = attivitaRepository.findByKeyPeople_Id(id);

            for(Attivita a : attivita) {
                attivitaRepository.deleteById(a.getId());

                logger.debug("Eliminata attivita: " + a.getId() + "del key people");

            }

            keyPeopleRepository.deleteById(id);

            logger.debug("Key people eliminato correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public boolean controllaDenominazioneDuplicata(String nome){
        logger.debug("Controlla denominazione duplicata");

        Optional<KeyPeople> keyPeople =  keyPeopleRepository.findByNome(nome);
        return keyPeople.isPresent();
    }

    public void trasformaMappaInKeyPeople(KeyPeople keyPeople, Map<String,String> keyPeopleMap) {
        logger.debug("Trasforma mappa in key people");

        keyPeople.setCellulare(keyPeopleMap.get("cellulare") != null ? keyPeopleMap.get("cellulare") : null);
        keyPeople.setEmail(keyPeopleMap.get("email") != null ? keyPeopleMap.get("email") : null);
        keyPeople.setNote(keyPeopleMap.get("note") != null ? keyPeopleMap.get("note") : null);;
        keyPeople.setRuolo(keyPeopleMap.get("ruolo") != null ? keyPeopleMap.get("ruolo") : null);
        keyPeople.setDataCreazione(keyPeopleMap.get("dataCreazione") != null ? Date.valueOf(keyPeopleMap.get("dataCreazione")) : null);
        keyPeople.setDataUltimaAttivita(keyPeopleMap.get("dataUltimaAttivita") != null ? Date.valueOf(keyPeopleMap.get("dataUltimaAttivita")) : null);
        keyPeople.setStatus(keyPeopleMap.get("status") != null ? keyPeopleMap.get("status") : null);
        keyPeople.setNome(keyPeopleMap.get("nome") != null ? keyPeopleMap.get("nome") : null);
        keyPeople.setComunicazioniRecenti(keyPeopleMap.get("comunicazioniRecenti") != null ? keyPeopleMap.get("comunicazioniRecenti") : null);

        if (keyPeopleMap.get("idAzienda") != null) {
            Cliente cliente = new Cliente();
            cliente.setId(Integer.parseInt(keyPeopleMap.get("idAzienda")));

            keyPeople.setCliente(cliente);
        }

        if (keyPeopleMap.get("idOwner") != null) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(keyPeopleMap.get("idOwner")));

            keyPeople.setOwner(owner);
        }
    }
}