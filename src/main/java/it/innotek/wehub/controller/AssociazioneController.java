/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.*;
import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.entity.timesheet.Calendario;
import it.innotek.wehub.entity.timesheet.Progetto;
import it.innotek.wehub.repository.*;
import it.innotek.wehub.util.UtilLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/associazioni")
public class AssociazioneController {

    @Autowired
    private StatoARepository       statoARepository;
    @Autowired
    private OwnerRepository        ownerRepository;
    @Autowired
    private CandidatoRepository    candidatoRepository;
    @Autowired
    private ClienteRepository      clienteRepository;
    @Autowired
    private AssociazioniRepository associazioniRepository;
    @Autowired
    private NeedRepository         needRepository;
    @Autowired
    private StaffRepository        staffRepository;
    @Autowired
    private ProgettoRepository     progettoRepository;

    private static final Logger logger = LoggerFactory.getLogger(AssociazioneController.class);

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public AssociazioneCandidatoNeed findById(
        @PathVariable("id") Integer id
    ){
        logger.info("Associazione by id");

        return associazioniRepository.findById(id).get();
    }

    @GetMapping("/react/stati")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<StatoA> findStati(){
        logger.info("Stati associazione");

        return statoARepository.findAll();
    }

    @PostMapping("/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveAssociazione(
        @RequestBody Map<String,String> associazione
    ) {
        logger.info("Salva associazione");

        try {

            AssociazioneCandidatoNeed associazioneEntity = new AssociazioneCandidatoNeed();

            if(associazione.get("id") != null) {
                associazioneEntity = associazioniRepository.findById(Integer.parseInt(associazione.get("id"))).get();

                logger.debug("Associazione trovata si procede in modifica");
            }

            trasformaMappaInAssociazione(associazioneEntity, associazione);

            logger.debug("Associazione trasformata");

            List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByNeed_IdAndCandidato_IdAndStato_IdAndDataModifica(associazioneEntity.getNeed().getId(), associazioneEntity.getCandidato().getId(), associazioneEntity.getStato().getId(), associazioneEntity.getDataModifica());

            if (( null == associazioni ) || associazioni.isEmpty()) {
                if (associazioneEntity.getStato().getId() == 11) {  //Staffato
                    Need    need    = needRepository.findById(associazioneEntity.getNeed().getId()).get();
                    Integer risorse = need.getNumeroRisorse();

                    need.setNumeroRisorse(risorse - 1);

                    if (risorse == 1) {
                        StatoN statoNeed = new StatoN();

                        statoNeed.setId(3); //Vinto
                        need.setStato(statoNeed);
                    }

                    needRepository.save(need);
                    associazioniRepository.save(associazioneEntity);

                    Candidato candidato = candidatoRepository.findById(associazioneEntity.getCandidato().getId()).get();
                    Staff     staff     = trasformaCandidatoInStaff(candidato);
                    Calendar  calendar  = Calendar.getInstance();

                    calendar.setTime(associazioneEntity.getDataModifica());

                    int               anno       = calendar.get(Calendar.YEAR);
                    int               mese       = calendar.get(Calendar.MONTH) + 1;
                    Progetto          progetto   = new Progetto();
                    TipologiaProgetto tipologia  = new TipologiaProgetto();
                    Calendario        calendario = UtilLib.creazioneCalendarioLib(anno, mese, 1, progetto);

                    tipologia.setId(1);
                    progetto.setDescription("Ferie, Permessi e Malattia");
                    progetto.setTipologia(tipologia);

                    progettoRepository.save(progetto);

                    staff.getProgetti().add(progetto);
                    staff.setTimesheet(calendario);
                    staffRepository.save(staff);

                    candidatoRepository.deleteById(0);

                    return "OK";
                }

                associazioniRepository.save(associazioneEntity);

                logger.debug("Associazione inserita correttamente");

                return "OK";
            } else {

                logger.debug("Associazione duplicata");

                return "DUPLICATO";
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @DeleteMapping("/react/rimuovi/associa/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String showRimuoviCandidatiForm(
        @PathVariable("id") Integer id
    ){
        logger.info("Associazione rimuovi associa");

        try {

            associazioniRepository.deleteById(id);

            logger.debug("Associazione " + id + " rimossa");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @DeleteMapping("/react/rimuovi/candidato/associa")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteNeedCandidato(
        @RequestParam("idNeed") Integer idNeed,
        @RequestParam("idCandidato") Integer idCandidato
    ) {

        logger.info("Elimina associazione candidato");

        try {
            List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByCandidato_Id(idCandidato);

            logger.debug("Associazioni trovate");

            Need need = needRepository.findById(idNeed).get();
            need.getCandidati().removeIf(s -> Objects.equals(s.getId(), idCandidato));

            for (AssociazioneCandidatoNeed associazione: associazioni) {
                associazioniRepository.deleteById(associazione.getId());

                logger.debug("Associazione " +associazione.getId()+ " rimossa");

            }

            needRepository.save(need);

            logger.debug("Associazione aggiornata");

            return "OK";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @GetMapping("/react/match/associabili/{idCandidato}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Need> showMatchForm(
        @PathVariable("idCandidato") Integer idCandidato
    ) {
        logger.info("Need associabili al candidato: " + idCandidato);

        return needRepository.findNeedAssociabiliCandidato(idCandidato);

    }

    @RequestMapping("/react/candidato/{idCandidato}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<AssociazioneCandidatoNeed> showAssociazioniCandidatoList(
        @PathVariable("idCandidato") Integer idCandidato
    ){
        logger.info("Associazioni del candidato");

        return associazioniRepository.findByCandidato_Id(idCandidato);

    }

    @PostMapping("/react/associa")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String showAssociaCandidatiForm(
        @RequestParam("idNeed") Integer idNeed,
        @RequestParam("idCandidato") Integer idCandidato
    ){
        logger.info("Associa");

        try {
            AssociazioneCandidatoNeed associazione = new AssociazioneCandidatoNeed();
            long                      millis       = System.currentTimeMillis();
            Need                      need         = needRepository.findById(idNeed).get();
            Candidato                 candidato    = candidatoRepository.findById(idCandidato).get();
            StatoA                    statoa       = new StatoA();

            logger.debug("Trovato need con id: " + idNeed + " e candidato con id: "+ idCandidato);

            statoa.setId(1);
            statoa.setDescrizione("Pool");
            associazione.setStato(statoa);
            associazione.setDataModifica(new Date(millis));

            need.getCandidati().add(candidato);

            associazione.setNeed(need);
            associazione.setCandidato(candidato);

            Owner owner = new Owner();
            owner.setId(1);

            associazione.setOwner(owner);

            associazioniRepository.save(associazione);

            logger.debug("Associazione salvata correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }


    private Staff trasformaCandidatoInStaff(Candidato candidato){

        logger.debug("Trasforma candidato in staff");

        Staff  staff     = new Staff();
        String nomeEmail = candidato.getNome().charAt(0) + "." + candidato.getCognome();
        String email     = nomeEmail.toLowerCase() + "@inno-tek.it";

        if (controllaMailDuplicata(email)) {

            nomeEmail = candidato.getNome() + "." + candidato.getCognome();
            email     = nomeEmail.toLowerCase()+"@inno-tek.it";
        };

        staff.setAnniEsperienza(candidato.getAnniEsperienza());
        staff.setCellulare(candidato.getCellulare());
        staff.setCitta(candidato.getCitta());
        staff.setCognome(candidato.getCognome());
        staff.setDataNascita(candidato.getDataNascita());
        staff.setEmail(email);
        staff.setSkills(candidato.getSkills());
        staff.setFacolta(candidato.getFacolta()!=null?candidato.getFacolta():null);
        staff.setLivelloScolastico(candidato.getLivelloScolastico()!=null?candidato.getLivelloScolastico():null);
        staff.setNome(candidato.getNome());
        staff.setNote(candidato.getNote());
        staff.setRal(candidato.getRal());
        staff.setTipologia(candidato.getTipologia()!=null?candidato.getTipologia():null);

        return staff;
    }

    public boolean controllaMailDuplicata(String email) {
        logger.debug("Controlla mail duplicata");

        return !staffRepository.findByEmail(email).isEmpty();
    }

    public AssociazioneCandidatoNeed trasformaMappaInAssociazione(AssociazioneCandidatoNeed associazione, Map<String,String> associazioneMap) {

        logger.debug("Trasforma mappa in associazione");

        if (associazioneMap.get("stato") != null) {
            StatoA statoAssociazione = new StatoA();
            statoAssociazione.setId(Integer.parseInt(associazioneMap.get("stato")));
            associazione.setStato(statoAssociazione);
        }

        associazione.setDataModifica(associazioneMap.get("dataModifica") != null ? Date.valueOf(associazioneMap.get("dataModifica")) : null);

        if (associazioneMap.get("idCandidato") != null) {
            Candidato candidato = new Candidato();
            candidato.setId(Integer.parseInt(associazioneMap.get("idCandidato")));
            associazione.setCandidato(candidato);
        }

        if (associazioneMap.get("idNeed") != null) {
            Need need = new Need();
            need.setId(Integer.parseInt(associazioneMap.get("idNeed")));
            associazione.setNeed(need);
        }

        if (associazioneMap.get("idOwner") != null) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(associazioneMap.get("idOwner")));
            associazione.setOwner(owner);
        }

        return associazione;
    }
}
