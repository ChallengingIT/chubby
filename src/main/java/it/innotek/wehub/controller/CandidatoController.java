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
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/staffing")
public class CandidatoController {

    @Autowired
    private CandidatoRepository candidatoRepository;
    @Autowired
    private TipologiaRepository tipologiaRepository;
    @Autowired
    private FacoltaRepository facoltaRepository;
    @Autowired
    private StatoCRepository statoCRepository;
    @Autowired
    private LivelloRepository livelloRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private IntervistaRepository intervistaRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TipoRepository tipoRepository;
    @Autowired
    private FornitoreRepository fornitoreRepository;
    @Autowired
    private AssociazioniRepository associazioniRepository;

    private static final Logger logger = LoggerFactory.getLogger(CandidatoController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Candidato> getAll() {
        logger.info("Candidati");

        return candidatoRepository.findAll();
    }

    @GetMapping("/react/mod")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<CandidatoModificato> getAllMod() {
        logger.info("Candidati");
        List<Candidato> candidati = candidatoRepository.findAll();
        List<CandidatoModificato> candidatiModificati = new ArrayList<>();

        for (Candidato candidato : candidati) {
            CandidatoModificato candidatoMod = new CandidatoModificato();

            candidatoMod.setId(candidato.getId());
            candidatoMod.setNote(candidato.getNote());
            candidatoMod.setOwner(candidato.getOwner());
            candidatoMod.setStato(candidato.getStato());
            candidatoMod.setTipologia(candidato.getTipologia());
            candidatoMod.setCognome(candidato.getCognome());
            candidatoMod.setNome(candidato.getNome());
            candidatoMod.setDataUltimoContatto(candidato.getDataUltimoContatto());
            candidatoMod.setEmail(candidato.getEmail());

            if (null != candidato.getFiles()) {
                File file = null;

                for (File fileC : candidato.getFiles()) {
                    if (fileC.getTipologia() != null && fileC.getTipologia().getId() == 1) {
                        file = new File();
                        file.setId(fileC.getId());
                    }
                }

                candidatoMod.setFile(file);
            }
            candidatoMod.setRal(candidato.getRal());
            candidatoMod.setRating(candidato.getRating());

            candidatiModificati.add(candidatoMod);
        }

        return candidatiModificati;
    }

    @GetMapping("/react/mod/ricerca")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<CandidatoModificato> getAllModRicerca(
        @RequestParam("nome") @Nullable String nome,
        @RequestParam("cognome") @Nullable String cognome,
        @RequestParam("email") @Nullable String email,
        @RequestParam("stato") @Nullable Integer stato,
        @RequestParam("tipo") @Nullable Integer tipo,
        @RequestParam("tipologia") @Nullable Integer tipologia
    ) {
        logger.info("Candidati");
        List<Candidato> candidati = candidatoRepository.ricercaByNomeAndCognomeAndEmailAndTipologia_IdAndStato_IdAndTipo_Id(
            nome, cognome,email,tipologia,stato, tipo
        );
        List<CandidatoModificato> candidatiModificati = new ArrayList<>();

        for (Candidato candidato : candidati) {
            CandidatoModificato candidatoMod = new CandidatoModificato();

            candidatoMod.setId(candidato.getId());
            candidatoMod.setNote(candidato.getNote());
            candidatoMod.setOwner(candidato.getOwner());
            candidatoMod.setStato(candidato.getStato());
            candidatoMod.setTipologia(candidato.getTipologia());
            candidatoMod.setCognome(candidato.getCognome());
            candidatoMod.setNome(candidato.getNome());
            candidatoMod.setDataUltimoContatto(candidato.getDataUltimoContatto());
            candidatoMod.setEmail(candidato.getEmail());

            candidatoMod.setRal(candidato.getRal());
            candidatoMod.setRating(candidato.getRating());

            candidatiModificati.add(candidatoMod);
        }

        return candidatiModificati;
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Candidato getAll(@PathVariable("id") Integer id) {
        logger.info("Candidato tramite id");

        return candidatoRepository.findById(id).get();
    }

    @GetMapping("/react/tipo")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Tipo> getAllTipo() {
        logger.info("Tipi Candidato");

        return tipoRepository.findAll();
    }

    @GetMapping("/react/skill")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Skill> getAllSkills() {
        logger.info("Skills");

        return skillRepository.findAll();
    }

    @GetMapping("/react/stato/candidato")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<StatoC> getAllStatoC() {
        logger.info("Stati Candidato");

        return statoCRepository.findAllByOrderByIdAsc();
    }

    @GetMapping("/react/facolta")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Facolta> getAllFacolta() {
        logger.info("Facoltà");

        return facoltaRepository.findAll();
    }

    @GetMapping("/react/livello")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<LivelloScolastico> getAllLivello() {
        logger.info("Livelli scolastici");

        return livelloRepository.findAll();
    }

    @PostMapping("/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveCandidato(
        @RequestBody Map<String,String>  candidatoMap,
        @RequestParam("skill") @Nullable List<Integer> skillList
    ) {
        logger.info("Salva candidato");

        try {
            Candidato candidatoEntity = new Candidato();

            if(candidatoMap.get("id") != null) {
                candidatoEntity = candidatoRepository.findById(Integer.parseInt(candidatoMap.get("id"))).get();

                logger.debug("Candidato trovato si procede in modifica");
            }

             trasformaMappaInCandidato(candidatoEntity, candidatoMap, skillList);

            if (controllaMailDuplicata(candidatoEntity.getEmail())) {

                if (candidatoEntity.getId() == null) {
                    logger.debug("Candidato duplicato, denominazione già presente");

                    return "DUPLICATO";
                }
            }

            if (null == candidatoEntity.getRating()) {
                candidatoEntity.setRating(0.0);
            }

            if (null != candidatoEntity.getFacolta()) {
                if (null == candidatoEntity.getFacolta().getId()) {
                    candidatoEntity.setFacolta(null);
                }
            }

            /*if (( null != file.getOriginalFilename() ) && !file.getOriginalFilename().isEmpty()) {
                candidatoEntity.getFiles().removeIf(f -> f.getTipologia().getId() == 1);
                candidatoEntity.getFiles().add(fileVoid(file, 1));

                logger.debug("CV del candidato aggiornato");
            }

            if (( null != cf.getOriginalFilename() ) && !cf.getOriginalFilename().isEmpty()) {
                candidatoEntity.getFiles().removeIf(f -> f.getTipologia().getId() == 2);
                candidatoEntity.getFiles().add(fileVoid(cf, 2));

                logger.debug("CF del candidato aggiornato");
            }*/

            candidatoRepository.save(candidatoEntity);
            logger.debug("Candidato salvato correttamente");

            return ""+candidatoEntity.getId();

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @PostMapping("/react/staff/salva/file/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveCandidato(
        @PathVariable("id") Integer id,
        @RequestParam("tipo") Integer tipo,
        @RequestParam("file") MultipartFile file
    ) {
        logger.info("Staff salva file");

        try {

            Candidato candidato =  candidatoRepository.findById(id).get();

            if (null != candidato.getId()) {

                if (( null != file.getOriginalFilename() ) && !file.getOriginalFilename().isEmpty() && tipo == 1) {
                    candidato.getFiles().removeIf(f -> f.getTipologia().getId() == 1);
                    candidato.getFiles().add(fileVoid(file, 1));

                    logger.debug("CV del candidato aggiornato");
                }

                if (( null != file.getOriginalFilename() ) && !file.getOriginalFilename().isEmpty() && tipo == 2) {
                    candidato.getFiles().removeIf(f -> f.getTipologia().getId() == 2);
                    candidato.getFiles().add(fileVoid(file, 2));

                    logger.debug("CF del candidato aggiornato");
                }

                candidatoRepository.save(candidato);
            }

            logger.debug("Salvataggio effettuato correttamente");

            return "OK";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }
    /*@CrossOrigin(origins = "*")
    @PostMapping("/salva/file/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveCandidatoFile(
        @PathVariable("id") Integer idCandidato,
        @RequestParam("cv") MultipartFile file
    ) {
        logger.info("salva candidato");

       return "SCIAO BELO";
    }*/
    
    @DeleteMapping("/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteCandidato(
        @PathVariable("id") Integer id
    ){
        logger.info("Elimina candidato");

        try {

            List<Intervista> interviste = intervistaRepository.findByCandidato_Id(id);

            for (Intervista intervista : interviste) {
                intervistaRepository.deleteById(intervista.getId());

                logger.debug("Eliminata intervista: " + intervista.getId());

            }

            candidatoRepository.deleteById(id);

            logger.info("Candidato eliminato correttamente");

            return "OK";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public File fileVoid(
        MultipartFile file,
        Integer tipoFile
    )
        throws Exception {
        logger.debug("Creazione file");

        try {
            String         descrizione          = Objects.requireNonNull(file.getOriginalFilename()).length() <= 90 ? file.getOriginalFilename() : file.getOriginalFilename().substring(0,89);
            byte[]         arrayByte            = file.getBytes();
            String         tipo                 = file.getContentType();
            File           fileOggettoCandidato = new File();
            java.util.Date date                 = new java.util.Date();
            java.sql.Date  sqlDate              = new Date(date.getTime());
            TipologiaF     tipologia            = new TipologiaF();

            fileOggettoCandidato.setData(arrayByte);
            fileOggettoCandidato.setDataInserimento(new java.sql.Date(sqlDate.getTime()));
            fileOggettoCandidato.setDescrizione(descrizione);
            fileOggettoCandidato.setTipo(tipo);

            tipologia.setId(tipoFile);
            fileOggettoCandidato.setTipologia(tipologia);

            return fileOggettoCandidato;

        } catch (Exception e) {
            logger.error(e.toString());

            throw new Exception(e);
        }
    }

    public boolean controllaMailDuplicata(String email) {
        logger.debug("Candidati controlla mail duplicata");

        List<Candidato> candidati = candidatoRepository.findByEmail(email);
        return ((null != candidati) && !candidati.isEmpty());
    }

    public void trasformaMappaInCandidato(Candidato candidato, Map<String,String> candidatoMap, List<Integer> skillList) {
        logger.debug("Trasforma mappa in candidato");

        if (candidatoMap.get("stato") != null) {
            StatoC stato = new StatoC();
            stato.setId(Integer.parseInt(candidatoMap.get("stato")));
            candidato.setStato(stato);
        }
        candidato.setAnniEsperienza(candidatoMap.get("anniEsperienza") != null ? Double.parseDouble(candidatoMap.get("anniEsperienza")) : null);
        candidato.setAnniEsperienzaRuolo(candidatoMap.get("anniEsperienzaRuolo") != null ? Double.parseDouble(candidatoMap.get("anniEsperienzaRuolo")) : null);
        candidato.setCellulare(candidatoMap.get("cellulare") != null ? candidatoMap.get("cellulare") : null);
        candidato.setCitta(candidatoMap.get("citta") != null ? candidatoMap.get("citta") : null);
        candidato.setEmail(candidatoMap.get("email") != null ? candidatoMap.get("email") : null);
        candidato.setNote(candidatoMap.get("note") != null ? candidatoMap.get("note") : null);;
        candidato.setCognome(candidatoMap.get("cognome") != null ? candidatoMap.get("cognome") : null);
        candidato.setDataNascita(candidatoMap.get("dataNascita") != null ? Date.valueOf(candidatoMap.get("dataNascita")) : null);
        candidato.setDataUltimoContatto(candidatoMap.get("dataUltimoContatto") != null ? Date.valueOf(candidatoMap.get("dataUltimoContatto")) : null);
        candidato.setDisponibilita(candidatoMap.get("disponibilita") != null ? candidatoMap.get("disponibilita") : null);

        if (candidatoMap.get("facolta") != null) {
            Facolta facolta = new Facolta();
            facolta.setId(Integer.parseInt(candidatoMap.get("facolta")));

            candidato.setFacolta(facolta);
        }
        if (candidatoMap.get("fornitore") != null) {
            Fornitore fornitore = new Fornitore();
            fornitore.setId(Integer.parseInt(candidatoMap.get("fornitore")));

            candidato.setFornitore(fornitore);
        }
        if (candidatoMap.get("livelloScolastico") != null) {
            LivelloScolastico livello = new LivelloScolastico();
            livello.setId(Integer.parseInt(candidatoMap.get("livelloScolastico")));

            candidato.setLivelloScolastico(livello);
        }
        candidato.setModalita(candidatoMap.get("modalita") != null ? Integer.parseInt(candidatoMap.get("modalita")) : null);
        candidato.setNome(candidatoMap.get("nome") != null ? candidatoMap.get("nome") : null);

        if (candidatoMap.get("owner") != null) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(candidatoMap.get("owner")));

            candidato.setOwner(owner);
        }
        candidato.setRal(candidatoMap.get("ral") != null ? candidatoMap.get("ral") : null);
        candidato.setRicerca(candidatoMap.get("ricerca") != null ? candidatoMap.get("ricerca") : null);

        if (candidatoMap.get("tipo") != null) {
            Tipo tipo = new Tipo();
            tipo.setId(Integer.parseInt(candidatoMap.get("tipo")));

            candidato.setTipo(tipo);
        }

        if (candidatoMap.get("tipologia") != null) {
            Tipologia tipologia = new Tipologia();
            tipologia.setId(Integer.parseInt(candidatoMap.get("tipologia")));

            candidato.setTipologia(tipologia);
        }

        Set<Skill> skillListNew = new HashSet<>();

        for (Integer skillId: skillList) {
            Skill skill = new Skill();
            skill.setId(skillId);

            skillListNew.add(skill);
        }

        candidato.setSkills(skillListNew);
    }
}
