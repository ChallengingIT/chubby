/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.*;
import it.innotek.wehub.repository.*;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/need")
public class NeedController {

    @Autowired
    private CandidatoRepository    candidatoRepository;
    @Autowired
    private ClienteRepository      clienteRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(NeedController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Need> getAll() {
        logger.info("Need");

        return needRepository.findAll();
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Need getById(@PathVariable("id") Integer id) {
        logger.info("Need tramite id");

        return needRepository.findById(id).get();
    }

    @GetMapping("/react/cliente/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Need> getByIdCliente(@PathVariable("id") Integer id) {
        logger.info("Need tramite id cliente");

        return needRepository.findByCliente_Id(id);
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

    @PostMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveNeed(
        @RequestBody Map<String,String> needMap,
        @RequestParam ("skill1") @Nullable List<Integer> skill1List,
        @RequestParam ("skill2") @Nullable List<Integer> skill2List
    ) {
        logger.info("Salva need");

        try {

            Need need = new Need();

            if(needMap.get("id") != null) {
                need = needRepository.findById(Integer.parseInt(needMap.get("id"))).get();
            }

            trasformaMappaInNeed(need, needMap, skill1List, skill2List);

            needRepository.save(need);

            logger.info("Need salvato correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @PostMapping("/react/salva/stato/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveNeedStato(
        @PathVariable("id") Integer id,
        @RequestParam ("stato") Integer idStato
    ) {
        logger.info("Salva cambio stato need");

        try {

            Need need = needRepository.findById(id).get();

            StatoN stato = new StatoN();
            stato.setId(idStato);

            need.setStato(stato);

            needRepository.save(need);

            logger.info("Cambio stato salvato correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @DeleteMapping("/react/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteNeed(
        @PathVariable("id") Integer id
    ){
        logger.info("Elimina need");

        try {

            List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByNeed_Id(id);

            for(AssociazioneCandidatoNeed associazione : associazioni) {
                associazioniRepository.deleteById(associazione.getId());

                logger.debug("Associazione " + associazione.getId() + " eliminata");
            }

            needRepository.deleteById(id);

            logger.debug("Need eliminato correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @GetMapping("/react/storico/{idNeed}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<AssociazioneCandidatoNeed> findByIdNeed(
        @PathVariable("idNeed") Integer idNeed
    ){
        logger.info("Storico associazioni need");

        return associazioniRepository.findByNeed_Id(idNeed);
    }

    @GetMapping("/react/match/associabili/{idNeed}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Candidato> showMatchForm(
        @PathVariable("idNeed") Integer idNeed
    ) {
        logger.info("Candidati non associati al need");

        return candidatoRepository.findCandidatiNonAssociati(idNeed);

    }

    @GetMapping("/react/match/associati/{idNeed}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Candidato> showMatchAssociatiForm(
        @PathVariable("idNeed") Integer idNeed
    ) {
        logger.info("Candidati associati al need");

        return candidatoRepository.findCandidatiAssociati(idNeed);

    }

    public void trasformaMappaInNeed(Need need, Map<String,String> needMap, List<Integer> skill1List, List<Integer> skill2List) {
        logger.debug("Trasforma mappa in need");

        need.setAnniEsperienza(needMap.get("anniEsperienza") != null ? Integer.parseInt(needMap.get("anniEsperienza")) : null);
        need.setNumeroRisorse(needMap.get("numeroRisorse") != null ? Integer.parseInt(needMap.get("numeroRisorse")) : null);
        need.setNote(needMap.get("note") != null ? needMap.get("note") : null);;
        need.setDescrizione(needMap.get("descrizione") != null ? needMap.get("descrizione") : null);
        need.setLocation(needMap.get("location") != null ? needMap.get("location") : null);
        need.setTipo(needMap.get("tipo") != null ? Integer.parseInt(needMap.get("tipo")) : null);

        if (needMap.get("idAzienda") != null) {
            Cliente cliente = new Cliente();
            cliente.setId(Integer.parseInt(needMap.get("idAzienda")));

            need.setCliente(cliente);
        }

        need.setWeek(needMap.get("week") != null ? needMap.get("week") : null);
        need.setDataRichiesta(needMap.get("dataRichiesta") != null ? Date.valueOf(needMap.get("dataRichiesta")) : null);
        need.setPriorita(needMap.get("priorita") != null ? Integer.parseInt(needMap.get("priorita")) : null);

        if (needMap.get("idOwner") != null) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(needMap.get("idOwner")));

            need.setOwner(owner);
        }

        if (needMap.get("stato") != null) {
            StatoN stato = new StatoN();
            stato.setId(Integer.parseInt(needMap.get("stato")));

            need.setStato(stato);
        }

        if (needMap.get("tipologia") != null) {
            TipologiaN tipologia = new TipologiaN();
            tipologia.setId(Integer.parseInt(needMap.get("tipologia")));

            need.setTipologia(tipologia);
        }

        Set<Skill> skill1ListNew = new HashSet<>();

        for (Integer skillId: skill1List) {
            Skill skill = new Skill();
            skill.setId(skillId);

            skill1ListNew.add(skill);
        }

        need.setSkills(skill1ListNew);

        Set<Skill> skill2ListNew = new HashSet<>();

        for (Integer skillId: skill2List) {
            Skill skill = new Skill();
            skill.setId(skillId);

            skill2ListNew.add(skill);
        }

        need.setSkills2(skill2ListNew);
    }

}