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

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/need")
public class NeedController {

    @Autowired
    private CandidatoRepository    candidatoRepository;
    @Autowired
    private IntervistaRepository    intervistaRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(NeedController.class);

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

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Need> getAll() {
        logger.info("Need");

        return needRepository.findAll();
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

    @GetMapping("/react/modificato/personal")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<NeedModificato> getModPersonal(
            @RequestParam("username") String username,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Need modificati personal");

        try {
            Pageable             p               = PageRequest.of(pagina, quantita);
            List<Need>           needs    = needRepository.ricercaByUsername(username);
            //List<Need>           needs           = pageableNeed.getContent();
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

    @GetMapping("/react/cliente/priorita/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<NeedSoloPriorita> getAllSoloPriorita(@PathVariable("id") Integer id) {
        logger.info("Need solo priorita");

        try {
            List<Need> needs = needRepository.findByCliente_Id(id);
            List<NeedSoloPriorita> needsPriorita = new ArrayList<>();

            for (Need need : needs) {
                NeedSoloPriorita needSolo = new NeedSoloPriorita();

                needSolo.setId(need.getId());
                needSolo.setDescrizione(need.getDescrizione());
                needSolo.setPriorita(need.getPriorita());

                needsPriorita.add(needSolo);
            }

            return needsPriorita;

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
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

    @GetMapping("/react/keypeople/modificato/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<NeedModificato> getByIdKeypeopleeMod(
            @PathVariable("id") Integer id,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Need tramite id cliente modificati");

        try {
            Pageable p = PageRequest.of(pagina, quantita);

            Page<Need> pageableNeeds = needRepository.findByKeyPeople_IdOrderByProgressivoDesc(id, p);

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

            return needsModificati;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/cliente/modificato/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<NeedModificato> getByIdClienteMod(
        @PathVariable("id") Integer id,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Need tramite id cliente modificati");

        try {
            Pageable p = PageRequest.of(pagina, quantita);

            Page<Need> pageableNeeds = needRepository.findByCliente_IdOrderByProgressivoDesc(id, p);

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

    @PostMapping("/add/shortlist")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String addShortlist(
            @RequestParam("id") Integer id,
            @RequestParam("idCandidato") Integer idCandidato,
            @RequestParam("username") String username
    ) {
        logger.info("Salva need");
        StatoA                    statoa       = new StatoA();
        AssociazioneCandidatoNeed associazione = new AssociazioneCandidatoNeed();
        long                      millis       = System.currentTimeMillis();

        try {

            statoa.setId(1);
            statoa.setDescrizione("Pool");
            associazione.setStato(statoa);
            associazione.setDataModifica(new Date(millis));

            Need need = needRepository.findById(id).get();
            Candidato candidato = candidatoRepository.findById(idCandidato).get();

            need.getCandidati().add(candidato);

            needRepository.save(need);

            associazione.setNeed(need);
            associazione.setCandidato(candidato);

            Owner owner = ownerRepository.findByUsername(username).isPresent() ? ownerRepository.findByUsername(username).get() : null;

            associazione.setOwner(owner);

            associazioniRepository.save(associazione);

            logger.info("Candidato inserito correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @GetMapping("/react/ricerca/modificato/personal")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public NeedGroup getModRicercaPersonal(
            @RequestParam("username") @Nullable String username,
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
            Page<Need> pageableNeeds = needRepository.ricercaUsername(azienda, stato, idKeyPeople, priorita, tipologia, week, owner, descrizione, username, p);
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
            needGroup.setRecord(needRepository.countRicercaUsername(azienda, stato, idKeyPeople, priorita, tipologia, week, owner, descrizione, username));

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

    @PostMapping("/react/cliente/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveJobTitle(
            @RequestBody Map<String,String> needMap,
            @RequestParam("username") String username,
            @RequestParam("idCliente") Integer idCliente,
            @RequestParam("skill") List<Integer> skillList
    ) {
        logger.info("Salva need");

        try {

            Need need = new Need();

            if(needMap.get("id") != null) {
                need = needRepository.findById(Integer.parseInt(needMap.get("id"))).get();

            } else {
                trasformaMappaInNeed(need, needMap, skillList);

                String ultimoProgressivo = needRepository.findUltimoProgressivo();

                String anno      = ultimoProgressivo.split("-")[0];
                String contatore = ultimoProgressivo.split("-")[1];

                OffsetDateTime data = OffsetDateTime.now();
                String annoCorrente = ""+data.getYear();
                String finaleAnnoCorrente = annoCorrente.substring(2);
                if (!anno.equalsIgnoreCase(finaleAnnoCorrente)) {
                    if (finaleAnnoCorrente.equalsIgnoreCase("99")) {
                        anno = "00";
                        contatore = "01";
                    } else {
                        anno = String.valueOf(Integer.parseInt(finaleAnnoCorrente) + 1);
                        contatore = "01";
                    }
                } else {
                    contatore = String.valueOf(Integer.parseInt(contatore) + 1);
                }

                need.setProgressivo(anno + "-" + contatore);
                need.setDataRichiesta(new Date(System.currentTimeMillis()));
                need.setCliente(clienteRepository.findById(idCliente).get());
                need.setKeyPeople(keyPeopleRepository.findByCliente_Id(idCliente).get(0));
                need.setOwner(ownerRepository.findByUsername(username).isPresent() ? ownerRepository.findByUsername(username).get() : null);
                need.setNumeroRisorse(1);
                need.setPriorita(1);
                need.setScreening(1);
                need.setPubblicazione(1);
                need.setNumeroRisorse(1);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new java.util.Date());

                need.setWeek(String.valueOf(calendar.getWeekYear()));

                TipologiaN tipologia = new TipologiaN();
                StatoN stato = new StatoN();
                stato.setId(1);  //Attivo
                tipologia.setId(2); //Head Hunting

                need.setStato(stato);
                need.setTipologia(tipologia);

            }

            needRepository.save(need);

            logger.info("Job Description salvata correttamente");

            return need.getId().toString();
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @PostMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveNeed(
        @RequestBody Map<String,String> needMap,
        @RequestParam ("skill1") @Nullable List<Integer> skill1List
    ) {
        logger.info("Salva need");

        try {

            Need need = new Need();

            if(needMap.get("id") != null) {
                need = needRepository.findById(Integer.parseInt(needMap.get("id"))).get();
            } else {
                String ultimoProgressivo = needRepository.findUltimoProgressivo();

                String anno      = ultimoProgressivo.split("-")[0];
                String contatore = ultimoProgressivo.split("-")[1];

                OffsetDateTime data = OffsetDateTime.now();
                String annoCorrente = ""+data.getYear();
                String finaleAnnoCorrente = annoCorrente.substring(2);
                if (!anno.equalsIgnoreCase(finaleAnnoCorrente)) {
                    if (finaleAnnoCorrente.equalsIgnoreCase("99")) {
                        anno = "00";
                        contatore = "01";
                    } else {
                        anno = String.valueOf(Integer.parseInt(finaleAnnoCorrente) + 1);
                        contatore = "01";
                    }
                } else {
                    contatore = String.valueOf(Integer.parseInt(contatore) + 1);
                }

                need.setProgressivo(anno + "-" + contatore);
            }

            trasformaMappaInNeed(need, needMap, skill1List);

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
        @RequestParam ("stato") Integer idStato,
        @RequestParam ("priorita") Integer priorita

    ) {
        logger.info("Salva cambio stato need");

        try {

            Need need = needRepository.findById(id).get();

            StatoN stato = new StatoN();
            stato.setId(idStato);

            need.setStato(stato);
            need.setPriorita(priorita);

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
    public AssociazioneGroup findByIdNeed(
        @PathVariable("idNeed") Integer idNeed,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ){
        logger.info("Storico associazioni need");

        try {
            Pageable p = PageRequest.of(pagina, quantita);
            AssociazioneGroup associazioneGroup = new AssociazioneGroup();

            List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByNeed_IdOrderByDataModificaDesc(idNeed, p).getContent();

            List<AssociazioneModificata> associazioniModificate = new ArrayList<>();

            for (AssociazioneCandidatoNeed associazione : associazioni) {
                AssociazioneModificata associazioneMod = new AssociazioneModificata();

                associazioneMod.setId(associazione.getId());
                associazioneMod.setDataModifica(associazione.getDataModifica());
                associazioneMod.setStato(associazione.getStato());

                Candidato candidato = new Candidato();

                candidato.setId(associazione.getCandidato().getId());
                candidato.setNome(associazione.getCandidato().getNome());
                candidato.setCognome(associazione.getCandidato().getCognome());
                candidato.setTipo(associazione.getCandidato().getTipo());
                candidato.setTipologia(associazione.getCandidato().getTipologia());

                associazioneMod.setCandidato(candidato);

                Owner owner = new Owner();

                owner.setId(associazione.getOwner().getId());
                owner.setDescrizione(associazione.getOwner().getDescrizione());

                associazioneMod.setOwner(owner);

                associazioniModificate.add(associazioneMod);
            }

            associazioneGroup.setAssociazioni(associazioniModificate);
            associazioneGroup.setRecord(associazioniRepository.countByNeed_Id(idNeed));

            return associazioneGroup;

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/match/associabili/{idNeed}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Candidato> showMatchForm(
        @PathVariable("idNeed") Integer idNeed,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Candidati non associati al need");

        try {
            Pageable p = PageRequest.of(pagina, quantita);

            return candidatoRepository.findCandidatiNonAssociati(idNeed, p).getContent();
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }

    }

    @GetMapping("/react/match/associabili/mod/{idNeed}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public CandidatoGroup showMatchFormMod(
        @PathVariable("idNeed") Integer idNeed,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    )
        throws Exception {
        logger.info("Candidati non associati al need modificati");

        try {
            Pageable                  p                   = PageRequest.of(pagina, quantita);
            CandidatoGroup            candidatoGroup      = new CandidatoGroup();
            Page<Candidato>           candidatoPage       = candidatoRepository.findCandidatiNonAssociati(idNeed, p);
            List<Candidato>           candidati           = candidatoPage.getContent();
            List<CandidatoModificato> candidatiModificati = new ArrayList<>();

            for (Candidato candidato : candidati) {
                CandidatoModificato candidatoMod = new CandidatoModificato();

                candidatoMod.setId(candidato.getId());
                candidatoMod.setNote(candidato.getNote());
                candidatoMod.setOwner(candidato.getOwner());
                candidatoMod.setStato(candidato.getStato());
                candidatoMod.setTipologia(candidato.getTipologia());
                candidatoMod.setCognome(candidato.getCognome());

                if (null != candidato.getFiles()) {
                    File file = null;

                    for (File fileC : candidato.getFiles()) {
                        if (fileC.getTipologia() != null && fileC.getTipologia().getId() == 1) {
                            file = new File();
                            file.setId(fileC.getId());
                            file.setDescrizione(fileC.getDescrizione());
                        }
                    }

                    candidatoMod.setFile(file);
                }


                candidatoMod.setNome(candidato.getNome());
                candidatoMod.setDataUltimoContatto(candidato.getDataUltimoContatto());
                candidatoMod.setCitta(candidato.getCitta());
                candidatoMod.setRal(candidato.getRal());
                candidatoMod.setRating(candidato.getRating());
                candidatoMod.setHasInterviste(!intervistaRepository.findByCandidato_Id(candidato.getId()).isEmpty());

                candidatiModificati.add(candidatoMod);
            }

            candidatoGroup.setCandidati(candidatiModificati);
            candidatoGroup.setRecord(candidatoRepository.countCandidatiNonAssociati(idNeed));

            return candidatoGroup;
        } catch( Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/react/match/associabili/ricerca/mod/{idNeed}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public CandidatoGroup showRicercaMatchFormMod(
        @PathVariable("idNeed") Integer idNeed,
        @RequestParam("nome") @Nullable String nome,
        @RequestParam("cognome") @Nullable String cognome,
        @RequestParam("tipologia") @Nullable Integer idTipologia,
        @RequestParam("tipo") @Nullable Integer idTipo,
        @RequestParam("minimo") @Nullable Integer anniMinimi,
        @RequestParam("massimo") @Nullable Integer anniMassimi,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Candidati non associati al need modificati");

        try {
            Pageable                  p                   = PageRequest.of(pagina, quantita);
            CandidatoGroup            candidatoGroup      = new CandidatoGroup();
            List<Candidato>           candidati           = candidatoRepository.ricercaCandidatiNonAssociati(idNeed, nome, cognome, idTipologia, idTipo, anniMinimi, anniMassimi, p).getContent();
            List<CandidatoModificato> candidatiModificati = new ArrayList<>();

            for (Candidato candidato : candidati) {
                CandidatoModificato candidatoMod = new CandidatoModificato();

                candidatoMod.setId(candidato.getId());
                candidatoMod.setNote(candidato.getNote());
                candidatoMod.setOwner(candidato.getOwner());
                candidatoMod.setStato(candidato.getStato());
                candidatoMod.setTipologia(candidato.getTipologia());
                candidatoMod.setCognome(candidato.getCognome());

                if (null != candidato.getFiles()) {
                    File file = null;

                    for (File fileC : candidato.getFiles()) {
                        if (fileC.getTipologia() != null && fileC.getTipologia().getId() == 1) {
                            file = new File();
                            file.setId(fileC.getId());
                            file.setDescrizione(fileC.getDescrizione());
                        }
                    }

                    candidatoMod.setFile(file);
                }

                candidatoMod.setNome(candidato.getNome());
                candidatoMod.setDataUltimoContatto(candidato.getDataUltimoContatto());
                candidatoMod.setCitta(candidato.getCitta());
                candidatoMod.setRal(candidato.getRal());
                candidatoMod.setRating(candidato.getRating());
                candidatoMod.setHasInterviste(!intervistaRepository.findByCandidato_Id(candidato.getId()).isEmpty());

                candidatiModificati.add(candidatoMod);
            }

            candidatoGroup.setCandidati(candidatiModificati);
            candidatoGroup.setRecord(candidatoRepository.countRicercaCandidatiNonAssociati(idNeed, nome, cognome, idTipologia, idTipo, anniMinimi, anniMassimi));

            return candidatoGroup;

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/match/associati/{idNeed}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Candidato> showMatchAssociatiForm(
        @PathVariable("idNeed") Integer idNeed,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Candidati associati al need");

        try {
            Pageable p = PageRequest.of(pagina, quantita);

            return candidatoRepository.findCandidatiAssociati(idNeed, p).getContent();

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }

    }

    @GetMapping("/react/match/associati/mod/{idNeed}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public CandidatoGroup showMatchAssociatiMod(
        @PathVariable("idNeed") Integer idNeed,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Candidati non associati al need modificati");

        try {
            Pageable                  p                   = PageRequest.of(pagina, quantita);
            CandidatoGroup            candidatoGroup      = new CandidatoGroup();
            List<Candidato>           candidati           = candidatoRepository.findCandidatiAssociati(idNeed, p).getContent();
            List<CandidatoModificato> candidatiModificati = new ArrayList<>();

            for (Candidato candidato : candidati) {
                CandidatoModificato candidatoMod = new CandidatoModificato();

                candidatoMod.setId(candidato.getId());
                candidatoMod.setNote(candidato.getNote());
                candidatoMod.setOwner(candidato.getOwner());
                candidatoMod.setStato(candidato.getStato());
                candidatoMod.setTipologia(candidato.getTipologia());
                candidatoMod.setCognome(candidato.getCognome());

                if (null != candidato.getFiles()) {
                    File file = null;

                    for (File fileC : candidato.getFiles()) {
                        if (fileC.getTipologia() != null && fileC.getTipologia().getId() == 1) {
                            file = new File();
                            file.setId(fileC.getId());
                            file.setDescrizione(fileC.getDescrizione());
                        }
                    }

                    candidatoMod.setFile(file);
                }

                candidatoMod.setNome(candidato.getNome());
                candidatoMod.setDataUltimoContatto(candidato.getDataUltimoContatto());
                candidatoMod.setCitta(candidato.getCitta());
                candidatoMod.setRal(candidato.getRal());
                candidatoMod.setRating(candidato.getRating());

                if (!isPresent(candidatiModificati,candidatoMod)) {
                    candidatiModificati.add(candidatoMod);
                }
            }

            candidatoGroup.setCandidati(candidatiModificati);
            candidatoGroup.setRecord(candidatoRepository.countCandidatiAssociati(idNeed));

            return candidatoGroup;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }

    }

    public void trasformaMappaInNeed(Need need, Map<String,String> needMap, List<Integer> skill1List) {
        logger.debug("Trasforma mappa in need");

        need.setAnniEsperienza(needMap.get("anniEsperienza") != null ? Double.parseDouble(needMap.get("anniEsperienza")) : null);
        need.setNumeroRisorse(needMap.get("numeroRisorse") != null ? Integer.parseInt(needMap.get("numeroRisorse")) : null);
        need.setNote(needMap.get("note") != null ? needMap.get("note") : null);

        need.setDescrizione(needMap.get("descrizione") != null ? needMap.get("descrizione") : null);
        need.setLocation(needMap.get("location") != null ? needMap.get("location") : null);
        need.setTipo(needMap.get("tipo") != null ? Integer.parseInt(needMap.get("tipo")) : null);
        need.setPubblicazione(needMap.get("pubblicazione") != null ? Integer.parseInt(needMap.get("pubblicazione")) : null);
        need.setScreening(needMap.get("screening") != null ? Integer.parseInt(needMap.get("screening")) : null);

        if (needMap.get("idAzienda") != null) {
            Cliente cliente = new Cliente();
            cliente.setId(Integer.parseInt(needMap.get("idAzienda")));

            need.setCliente(cliente);
        }

        if (needMap.get("idKeyPeople") != null) {
            KeyPeople keyPeople = new KeyPeople();
            keyPeople.setId(Integer.parseInt(needMap.get("idKeyPeople")));

            need.setKeyPeople(keyPeople);
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

        if (needMap.get("jobTitle") != null) {
            Tipologia tipologia = new Tipologia();
            tipologia.setId(Integer.parseInt(needMap.get("tipologia")));

            need.setJobTitle(tipologia);
        }

        if (needMap.get("modalitaImpiego") != null) {
            ModalitaImpiego modalitaImpiego = new ModalitaImpiego();
            modalitaImpiego.setId(Integer.parseInt(needMap.get("modalitaImpiego")));

            need.setModalitaImpiego(modalitaImpiego);
        }

        if (needMap.get("modalitaLavoro") != null) {
            ModalitaLavoro modalitaLavoro = new ModalitaLavoro();
            modalitaLavoro.setId(Integer.parseInt(needMap.get("modalitaLavoro")));

            need.setModalitaLavoro(modalitaLavoro);
        }

        Set<Skill> skill1ListNew = new HashSet<>();

        if (null != skill1List) {
            for (Integer skillId : skill1List) {
                Skill skill = new Skill();
                skill.setId(skillId);

                skill1ListNew.add(skill);
            }
            need.setSkills(skill1ListNew);
        }

    }

    public boolean isPresent(List<CandidatoModificato> candidati, CandidatoModificato candidato) {
        return candidati.contains(candidato);
    }

}