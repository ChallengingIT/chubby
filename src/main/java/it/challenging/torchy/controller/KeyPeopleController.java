/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.EmailSenderService;
import it.challenging.torchy.entity.*;
import it.challenging.torchy.repository.*;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
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
    private ClienteRepository   clienteRepository;
    @Autowired
    private OwnerRepository     ownerRepository;
    @Autowired
    private TipologiaRepository tipologiaRepository;
    @Autowired
    private LivelloRepository   livelloRepository;
    @Autowired
    private ProvinceRepository  provinceRepository;
    @Autowired
    private StatoKRepository  statoKRepository;
    @Autowired
    private EmailSenderService  serviceEmailSender;

    private static final Logger logger = LoggerFactory.getLogger(KeyPeopleController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<KeyPeople> getAll() {
        logger.info("Key people");

        return keyPeopleRepository.findAll();
    }

    @GetMapping("/react/stati")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<StatoK> getAllStati() {
        logger.info("Key people stati");

        return statoKRepository.findAll();
    }

    @GetMapping("/react/mod")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<KeyPeopleModificato> getAllMod(
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Key people Mod");

        try {
            Pageable p = PageRequest.of(pagina, quantita);

            List<KeyPeople> keyPeoples = keyPeopleRepository.findAllByOrderByNomeAsc(p).getContent();
            List<KeyPeopleModificato> keyPeoplesMod = new ArrayList<>();

            for (KeyPeople keyPeople : keyPeoples) {
                KeyPeopleModificato keyPeopleMod = new KeyPeopleModificato();

                keyPeopleMod.setId(keyPeople.getId());
                keyPeopleMod.setCellulare(keyPeople.getCellulare());
                keyPeopleMod.setComunicazioniRecenti(keyPeople.getComunicazioniRecenti());
                keyPeopleMod.setDataCreazione(keyPeople.getDataCreazione());
                keyPeopleMod.setDataUltimaAttivita(keyPeople.getDataUltimaAttivita());
                keyPeopleMod.setEmail(keyPeople.getEmail());
                keyPeopleMod.setNome(keyPeople.getNome());
                keyPeopleMod.setRuolo(keyPeople.getRuolo());
                keyPeopleMod.setStato(keyPeople.getStato());
                keyPeopleMod.setTipo(keyPeople.getTipo());

                Cliente cliente = new Cliente();

                cliente.setId(keyPeople.getCliente().getId());
                cliente.setDenominazione(keyPeople.getCliente().getDenominazione());
                cliente.setLogo(keyPeople.getCliente().getLogo());

                keyPeopleMod.setCliente(cliente);
                keyPeopleMod.setNote(keyPeople.getNote());
                keyPeopleMod.setOwner(keyPeople.getOwner());

                keyPeoplesMod.add(keyPeopleMod);
            }

            return keyPeoplesMod;

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/ricerca/mod")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<KeyPeopleModificato> getAllModRicerca(
        @RequestParam("azienda") @Nullable Integer azienda,
        @RequestParam("stato") @Nullable Integer stato,
        @RequestParam("owner") @Nullable Integer owner,
        @RequestParam("nome") @Nullable String nome,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Key people Mod");

        try {
            Pageable p = PageRequest.of(pagina, quantita);

            List<KeyPeople> keyPeoples = keyPeopleRepository.ricercaByIdStatoAndIdOwnerAndIdAzienda(stato, azienda, owner, nome, p).getContent();
            List<KeyPeopleModificato> keyPeoplesMod = new ArrayList<>();

            for (KeyPeople keyPeople : keyPeoples) {
                KeyPeopleModificato keyPeopleMod = new KeyPeopleModificato();

                keyPeopleMod.setId(keyPeople.getId());
                keyPeopleMod.setCellulare(keyPeople.getCellulare());
                keyPeopleMod.setComunicazioniRecenti(keyPeople.getComunicazioniRecenti());
                keyPeopleMod.setDataCreazione(keyPeople.getDataCreazione());
                keyPeopleMod.setDataUltimaAttivita(keyPeople.getDataUltimaAttivita());
                keyPeopleMod.setEmail(keyPeople.getEmail());
                keyPeopleMod.setNome(keyPeople.getNome());
                keyPeopleMod.setRuolo(keyPeople.getRuolo());
                keyPeopleMod.setStato(keyPeople.getStato());
                keyPeopleMod.setTipo(keyPeople.getTipo());

                Cliente cliente = new Cliente();

                cliente.setId(keyPeople.getCliente().getId());
                cliente.setDenominazione(keyPeople.getCliente().getDenominazione());
                cliente.setLogo(keyPeople.getCliente().getLogo());

                keyPeopleMod.setCliente(cliente);
                keyPeopleMod.setNote(keyPeople.getNote());
                keyPeopleMod.setOwner(keyPeople.getOwner());

                keyPeoplesMod.add(keyPeopleMod);
            }

            return keyPeoplesMod;

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public KeyPeople getById(@PathVariable("id") Integer id) {
        logger.info("Key people tramite id");

        return keyPeopleRepository.findById(id).get();
    }

    @GetMapping("/react/azienda/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<KeyPeople> getByIdAzienda(@PathVariable("id") Integer id) {
        logger.info("Key people tramite id azienda");

        return keyPeopleRepository.findByCliente_Id(id);
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

            if (( null == keyPeopleEntity.getId() ) && controllaEmailDuplicata(keyPeopleEntity.getEmail())) {
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

    @DeleteMapping("/react/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteKeypeople(
        @PathVariable("id") Integer id
    ){
        logger.info("Elimina key people");

        try {

            keyPeopleRepository.deleteById(id);

            logger.debug("Key people eliminato correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public boolean controllaEmailDuplicata(String email){
        logger.debug("Controlla email duplicata");

        Optional<KeyPeople> keyPeople =  keyPeopleRepository.findByEmail(email);
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
        keyPeople.setNome(keyPeopleMap.get("nome") != null ? keyPeopleMap.get("nome") : null);
        keyPeople.setComunicazioniRecenti(keyPeopleMap.get("comunicazioniRecenti") != null ? keyPeopleMap.get("comunicazioniRecenti") : null);
        keyPeople.setTipo(keyPeopleMap.get("tipo") != null ? Integer.parseInt(keyPeopleMap.get("tipo")) : null);

        if (keyPeopleMap.get("idAzienda") != null) {
            Cliente cliente = new Cliente();
            cliente.setId(Integer.parseInt(keyPeopleMap.get("idAzienda")));

            keyPeople.setCliente(cliente);
        }

        if (keyPeopleMap.get("idStato") != null) {
            StatoK stato = new StatoK();
            stato.setId(Integer.parseInt(keyPeopleMap.get("idStato")));

            keyPeople.setStato(stato);
        }

        if (keyPeopleMap.get("idOwner") != null) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(keyPeopleMap.get("idOwner")));

            keyPeople.setOwner(owner);
        }
    }
}