/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.entity.*;
import it.challenging.torchy.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.threeten.extra.YearWeek;

import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/job/description")
public class JobDescriptionController {

    @Autowired
    private ClienteRepository      clienteRepository;
    @Autowired
    private OwnerRepository        ownerRepository;
    @Autowired
    private NeedRepository         needRepository;
    @Autowired
    private ModalitaImpiegoRepository modalitaImpiegoRepository;
    @Autowired
    private ModalitaLavoroRepository modalitaLavoroRepository;
    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;
    @Autowired
    private CandidatoRepository candidatoRepository;
    @Autowired
    private KeyPeopleRepository keyPeopleRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobDescriptionController.class);

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public JobDescription getById(@PathVariable("id") Long id) {
        logger.info("JobDescription by id");

        return jobDescriptionRepository.findById(id).get();
    }

    @GetMapping("/need/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public JobDescription getByIdNeed(@PathVariable("id") Integer id) {
        logger.info("JobDescription by id need");

        return jobDescriptionRepository.findByNeed_Id(id).get();
    }

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

    @PostMapping("/add/shortlist")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String addShortlist(
            @RequestParam("id") Long id,
            @RequestParam("idCandidato") Integer idCandidato
    ) {
        logger.info("Salva need");

        try {

            JobDescription jobDescription = jobDescriptionRepository.findById(id).get();
            Candidato candidato = candidatoRepository.findById(idCandidato).get();

            jobDescription.getNeed().getCandidati().add(candidato);

            jobDescriptionRepository.save(jobDescription);

            logger.info("Candidato inserito correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @PostMapping("/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveJobTitle(
        @RequestBody Map<String,String> jobMap,
        @RequestParam("username") String username,
        @RequestParam("idCliente") Integer idCliente,
        @RequestParam("skill") List<Integer> skillList
    ) {
        logger.info("Salva need");

        try {

            JobDescription jobDescription = new JobDescription();

            if(jobMap.get("id") != null) {
                jobDescription = jobDescriptionRepository.findById(Long.parseLong(jobMap.get("id"))).get();

                trasformaMappaInJobDescription(jobDescription, jobMap, skillList);

                jobDescription.getNeed().setAnniEsperienza(jobDescription.getAnniEsperienza());
                jobDescription.getNeed().setSkills(jobDescription.getSkills());
                jobDescription.getNeed().setLocation(jobDescription.getLocation());
                jobDescription.getNeed().setNote(jobDescription.getNote());

            } else {
                trasformaMappaInJobDescription(jobDescription, jobMap, skillList);

                Need nuovoNeed = new Need();

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

                nuovoNeed.setProgressivo(anno + "-" + contatore);
                nuovoNeed.setAnniEsperienza(jobDescription.getAnniEsperienza());
                nuovoNeed.setDescrizione(jobDescription.getTipologia().getDescrizione());
                nuovoNeed.setDataRichiesta(new Date(System.currentTimeMillis()));
                nuovoNeed.setNote(jobDescription.getNote());
                nuovoNeed.setCliente(clienteRepository.findById(idCliente).get());
                nuovoNeed.setKeyPeople(keyPeopleRepository.findByCliente_Id(idCliente).get(0));
                nuovoNeed.setOwner(ownerRepository.findByUsername(username).isPresent() ? ownerRepository.findByUsername(username).get() : null);
                nuovoNeed.setNumeroRisorse(1);
                nuovoNeed.setPriorita(1);
                nuovoNeed.setSkills(jobDescription.getSkills());
                nuovoNeed.setScreening(1);
                nuovoNeed.setPubblicazione(1);
                nuovoNeed.setNumeroRisorse(1);
                nuovoNeed.setWeek(YearWeek.from(LocalDate.now()).toString());

                TipologiaN tipologia = new TipologiaN();
                StatoN stato = new StatoN();
                stato.setId(1);  //Attivo
                tipologia.setId(2); //Head Hunting

                nuovoNeed.setStato(stato);
                nuovoNeed.setTipologia(tipologia);

                jobDescription.setNeed(nuovoNeed);
            }

            jobDescriptionRepository.save(jobDescription);

            logger.info("Job Description salvata correttamente");

            return jobDescription.getId().toString();
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @DeleteMapping("/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteJobDescription(
        @PathVariable("id") Long id
    ){
        logger.info("Elimina jobDescription");

        try {

            jobDescriptionRepository.deleteById(id);

            logger.debug("JobDescription eliminata correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public void trasformaMappaInJobDescription(JobDescription jobDescription, Map<String,String> jobMap, List<Integer> skill1List) {
        logger.debug("Trasforma mappa in need");

        jobDescription.setAnniEsperienza(jobMap.get("anniEsperienza") != null ? Double.parseDouble(jobMap.get("anniEsperienza")) : null);
        jobDescription.setNote(jobMap.get("note") != null ? jobMap.get("note") : null);
        jobDescription.setLocation(jobMap.get("location") != null ? jobMap.get("location") : null);


        if (jobMap.get("tipologia") != null) {
            Tipologia tipologia = new Tipologia();
            tipologia.setId(Integer.parseInt(jobMap.get("tipologia")));

            jobDescription.setTipologia(tipologia);
        }

        if (jobMap.get("modalitaImpiego") != null) {
            ModalitaImpiego modalitaImpiego = new ModalitaImpiego();
            modalitaImpiego.setId(Integer.parseInt(jobMap.get("modalitaImpiego")));

            jobDescription.setModalitaImpiego(modalitaImpiego);
        }

        if (jobMap.get("modalitaLavoro") != null) {
            ModalitaLavoro modalitaLavoro = new ModalitaLavoro();
            modalitaLavoro.setId(Integer.parseInt(jobMap.get("modalitaLavoro")));

            jobDescription.setModalitaLavoro(modalitaLavoro);
        }

        Set<Skill> skill1ListNew = new HashSet<>();

        if (null != skill1List) {
            for (Integer skillId : skill1List) {
                Skill skill = new Skill();
                skill.setId(skillId);

                skill1ListNew.add(skill);
            }
            jobDescription.setSkills(skill1ListNew);
        }
    }
}