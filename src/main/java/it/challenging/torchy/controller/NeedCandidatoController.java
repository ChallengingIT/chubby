/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.entity.*;
import it.challenging.torchy.repository.*;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/candidato/need")
public class NeedCandidatoController {

    @Autowired
    private CandidatoRepository    candidatoRepository;
    @Autowired
    private ClienteRepository      clienteRepository;
    @Autowired
    private KeyPeopleRepository    keyPeopleRepository;
    @Autowired
    private TipologiaNRepository   tipologiaNRepository;
    @Autowired
    private StatoNRepository       statoNRepository;
    @Autowired
    private OwnerRepository        ownerRepository;
    @Autowired
    private LivelloRepository      livelloRepository;
    @Autowired
    private SkillRepository        skillRepository;
    @Autowired
    private NeedRepository         needRepository;
    @Autowired
    private TipologiaRepository    tipologiaRepository;
    @Autowired
    private StatoCRepository       statoCRepository;
    @Autowired
    private TipoRepository         tipoRepository;
    @Autowired
    private AssociazioniRepository associazioniRepository;
    @Autowired
    private ModalitaImpiegoRepository modalitaImpiegoRepository;
    @Autowired
    private ModalitaLavoroRepository modalitaLavoroRepository;

    private static final Logger logger = LoggerFactory.getLogger(NeedCandidatoController.class);

    @GetMapping("/impiego")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<ModalitaImpiego> getModalitaImpiego() {
        logger.info("JobDescription id cliente");

        return modalitaImpiegoRepository.findAll();
    }

    @GetMapping("/lavoro")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<ModalitaLavoro> getModalitaLavoro() {
        logger.info("JobDescription id cliente");

        return modalitaLavoroRepository.findAll();
    }

    @GetMapping("/react/modificato")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<NeedModificato> getMod(
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Need modificati");

        try {
            Pageable             p               = PageRequest.of(pagina, quantita);
            Page<Need>           pageableNeed    = needRepository.findAllByOrderByProgressivoDesc(p);
            List<Need>           needs           = pageableNeed.getContent();
            List<NeedModificato> needsModificati = new ArrayList<>();

            for (Need need : needs) {
                NeedModificato needSolo = new NeedModificato();

                needSolo.setId(need.getId());
                needSolo.setProgressivo(need.getProgressivo());
                needSolo.setDescrizione(need.getDescrizione());
                needSolo.setPriorita(need.getPriorita());
                needSolo.setAnniEsperienza(need.getAnniEsperienza());

                Cliente cliente = new Cliente();

                cliente.setId(need.getCliente().getId());
                cliente.setDenominazione(need.getCliente().getDenominazione());
                cliente.setLogo(need.getCliente().getLogo());

                needSolo.setCliente(cliente);

                KeyPeople keyPeople = new KeyPeople();

                keyPeople.setId(need.getKeyPeople().getId());
                keyPeople.setNome(need.getKeyPeople().getNome());

                needSolo.setKeyPeople(keyPeople);

                needSolo.setLocation(need.getLocation());
                needSolo.setNote(need.getNote());
                needSolo.setNumeroRisorse(need.getNumeroRisorse());
                needSolo.setOwner(need.getOwner());
                needSolo.setSkills(need.getSkills());
                needSolo.setStato(need.getStato());
                needSolo.setTipo(need.getTipo());
                needSolo.setTipologia(need.getTipologia());
                needSolo.setWeek(need.getWeek());
                needSolo.setPubblicazione(need.getPubblicazione());
                needSolo.setScreening(need.getScreening());

                needsModificati.add(needSolo);
            }

            return needsModificati;

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/ricerca/modificato")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public NeedGroup getModRicerca(
        @RequestParam("azienda") @Nullable Integer azienda,
        @RequestParam("stato") @Nullable Integer stato,
        @RequestParam("keypeople") @Nullable Integer idKeyPeople,
        @RequestParam("priorita") @Nullable Integer priorita,
        @RequestParam("owner") @Nullable Integer owner,
        @RequestParam("week") @Nullable String week,
        @RequestParam("tipologia") @Nullable Integer tipologia,
        @RequestParam("descrizione") @Nullable String descrizione,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Need modificati");

        try {
            Pageable p = PageRequest.of(pagina, quantita);
            NeedGroup needGroup = new NeedGroup();
            Page<Need> pageableNeeds = needRepository.ricerca(azienda, stato, idKeyPeople, priorita, tipologia, week, owner, descrizione, p);
            List<Need> needs = pageableNeeds.getContent();

            List<NeedModificato> needsModificati = new ArrayList<>();

            for (Need need : needs) {
                NeedModificato needSolo = new NeedModificato();

                needSolo.setId(need.getId());
                needSolo.setProgressivo(need.getProgressivo());
                needSolo.setDescrizione(need.getDescrizione());
                needSolo.setPriorita(need.getPriorita());
                needSolo.setAnniEsperienza(need.getAnniEsperienza());

                Cliente cliente = new Cliente();

                cliente.setId(need.getCliente().getId());
                cliente.setDenominazione(need.getCliente().getDenominazione());
                cliente.setLogo(need.getCliente().getLogo());

                needSolo.setCliente(cliente);

                KeyPeople keyPeople = new KeyPeople();

                keyPeople.setId(need.getKeyPeople().getId());
                keyPeople.setNome(need.getKeyPeople().getNome());

                needSolo.setKeyPeople(keyPeople);
                needSolo.setLocation(need.getLocation());
                needSolo.setNote(need.getNote());
                needSolo.setNumeroRisorse(need.getNumeroRisorse());
                needSolo.setOwner(need.getOwner());
                needSolo.setSkills(need.getSkills());
                needSolo.setStato(need.getStato());
                needSolo.setTipo(need.getTipo());
                needSolo.setTipologia(need.getTipologia());
                needSolo.setWeek(need.getWeek());
                needSolo.setPubblicazione(need.getPubblicazione());
                needSolo.setScreening(need.getScreening());

                needsModificati.add(needSolo);
            }

            needGroup.setNeeds(needsModificati);
            needGroup.setRecord(needRepository.countRicerca(azienda, stato, idKeyPeople, priorita, tipologia, week, owner, descrizione));

            return needGroup;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/stato")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<StatoN> getAllStato() {
        logger.info("Stati need");

        return statoNRepository.findAll();
    }

    @GetMapping("/react/tipologia")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<TipologiaN> getAllTipologia() {
        logger.info("Tipologie need");

        return tipologiaNRepository.findAll();
    }

}