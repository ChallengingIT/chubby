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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/intervista")
public class IntervistaController {

    @Autowired
    private CandidatoRepository  candidatoRepository;
    @Autowired
    private TipologiaRepository  tipologiaRepository;
    @Autowired
    private TipologiaIRepository tipologiaIRepository;
    @Autowired
    private StatoCRepository     statoCRepository;
    @Autowired
    private OwnerRepository      ownerRepository;
    @Autowired
    private IntervistaRepository intervistaRepository;
    @Autowired
    private EmailSenderService   serviceEmail;
    @Autowired
    private TimedEmailRepository timedEmailRepository;

    private static final Logger logger = LoggerFactory.getLogger(IntervistaController.class);

    @GetMapping("/react/tipointervista")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<TipologiaI> getAllTipologiaIntervista()
    {
        logger.info("Tipologia interviste");

        return tipologiaIRepository.findAll();
    }

    @GetMapping("/react/{idCandidato}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Intervista> showIntervistaIdList(
        @PathVariable("idCandidato") Integer idCandidato,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Interviste candidato tramite id");

        try {
            Pageable p = PageRequest.of(pagina, quantita);

            return intervistaRepository.findByCandidato_IdOrderByDataColloquioDesc(idCandidato, p).getContent();
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/mod/{idCandidato}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public IntervistaGroup showIntervistaModIdList(
        @PathVariable("idCandidato") Integer idCandidato,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Interviste candidato tramite id mod");

        try {
            Pageable                   p               = PageRequest.of(pagina, quantita);
            IntervistaGroup            intervistaGroup = new IntervistaGroup();
            List<Intervista>           interviste      = intervistaRepository.findByCandidato_IdOrderByDataColloquioDesc(idCandidato, p).getContent();
            List<IntervistaModificato> intervisteMod   = new ArrayList<>();

            for (Intervista intervista : interviste) {
                IntervistaModificato intervistaMod = new IntervistaModificato();

                intervistaMod.setAderenza(intervista.getAderenza());
                intervistaMod.setAnniEsperienza(intervista.getAnniEsperienza());
                intervistaMod.setAttuale(intervista.getAttuale());

                if (null != intervista.getCandidato()) {
                    Candidato candidato = new Candidato();

                    candidato.setId(intervista.getCandidato().getId());
                    candidato.setCognome(intervista.getCandidato().getCognome());
                    candidato.setNome(intervista.getCandidato().getNome());

                    intervistaMod.setCandidato(candidato);
                }
                intervistaMod.setCoerenza(intervista.getCoerenza());
                intervistaMod.setCognome(intervista.getCognome());
                intervistaMod.setCompetenze(intervista.getCompetenze());
                intervistaMod.setComunicazione(intervista.getComunicazione());
                intervistaMod.setDataAggiornamento(intervista.getDataAggiornamento());
                intervistaMod.setDataAVideo(intervista.getDataAVideo());
                intervistaMod.setDataColloquio(intervista.getDataColloquio());
                intervistaMod.setDataNascita(intervista.getDataNascita());
                intervistaMod.setDescrizioneCandidato(intervista.getDescrizioneCandidato());
                intervistaMod.setDescrizioneCandidatoUna(intervista.getDescrizioneCandidatoUna());
                intervistaMod.setDesiderata(intervista.getDesiderata());
                intervistaMod.setDisponibilita(intervista.getDisponibilita());
                intervistaMod.setEnergia(intervista.getEnergia());
                intervistaMod.setId(intervista.getId());
                intervistaMod.setInglese(intervista.getInglese());
                intervistaMod.setMobilita(intervista.getMobilita());
                intervistaMod.setMotivazione(intervista.getMotivazione());
                intervistaMod.setNextOwner(intervista.getNextOwner());
                intervistaMod.setNome(intervista.getNome());
                intervistaMod.setOraAVideo(intervista.getOraAVideo());
                intervistaMod.setOwner(intervista.getOwner());
                intervistaMod.setPreavviso(intervista.getPreavviso());
                intervistaMod.setProposta(intervista.getProposta());
                intervistaMod.setRecapiti(intervista.getRecapiti());
                intervistaMod.setStanding(intervista.getStanding());
                intervistaMod.setStato(intervista.getStato());
                intervistaMod.setTeamSiNo(intervista.getTeamSiNo());
                intervistaMod.setTipo(intervista.getTipo());
                intervistaMod.setTipologia(intervista.getTipologia());
                intervistaMod.setValutazione(intervista.getValutazione());

                intervisteMod.add(intervistaMod);
            }

            intervistaGroup.setInterviste(intervisteMod);
            intervistaGroup.setRecord(intervistaRepository.countByCandidato_Id(idCandidato));

            return intervistaGroup;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/ultima/{idCandidato}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public IntervistaModificato showIntervistaModIdList(
            @PathVariable("idCandidato") Integer idCandidato
    ) {
        logger.info("Interviste candidato tramite id mod");

        try {
            List<Intervista>     interviste    = intervistaRepository.findByCandidato_IdOrderByDataColloquioDesc(idCandidato);
            IntervistaModificato intervistaMod = new IntervistaModificato();

            if (null != interviste && !interviste.isEmpty()){

                Intervista intervista = interviste.getFirst();

                intervistaMod.setAderenza(intervista.getAderenza());
                intervistaMod.setAnniEsperienza(intervista.getAnniEsperienza());
                intervistaMod.setAttuale(intervista.getAttuale());

                if (null != intervista.getCandidato()) {
                    Candidato candidato = new Candidato();

                    candidato.setId(intervista.getCandidato().getId());
                    candidato.setCognome(intervista.getCandidato().getCognome());
                    candidato.setNome(intervista.getCandidato().getNome());

                    intervistaMod.setCandidato(candidato);
                }

                intervistaMod.setCoerenza(intervista.getCoerenza());
                intervistaMod.setCognome(intervista.getCognome());
                intervistaMod.setCompetenze(intervista.getCompetenze());
                intervistaMod.setComunicazione(intervista.getComunicazione());
                intervistaMod.setDataAggiornamento(intervista.getDataAggiornamento());
                intervistaMod.setDataAVideo(intervista.getDataAVideo());
                intervistaMod.setDataColloquio(intervista.getDataColloquio());
                intervistaMod.setDataNascita(intervista.getDataNascita());
                intervistaMod.setDescrizioneCandidato(intervista.getDescrizioneCandidato());
                intervistaMod.setDescrizioneCandidatoUna(intervista.getDescrizioneCandidatoUna());
                intervistaMod.setDesiderata(intervista.getDesiderata());
                intervistaMod.setDisponibilita(intervista.getDisponibilita());
                intervistaMod.setEnergia(intervista.getEnergia());
                intervistaMod.setId(intervista.getId());
                intervistaMod.setInglese(intervista.getInglese());
                intervistaMod.setMobilita(intervista.getMobilita());
                intervistaMod.setMotivazione(intervista.getMotivazione());
                intervistaMod.setNextOwner(intervista.getNextOwner());
                intervistaMod.setNome(intervista.getNome());
                intervistaMod.setOraAVideo(intervista.getOraAVideo());
                intervistaMod.setOwner(intervista.getOwner());
                intervistaMod.setPreavviso(intervista.getPreavviso());
                intervistaMod.setProposta(intervista.getProposta());
                intervistaMod.setRecapiti(intervista.getRecapiti());
                intervistaMod.setStanding(intervista.getStanding());
                intervistaMod.setStato(intervista.getStato());
                intervistaMod.setTeamSiNo(intervista.getTeamSiNo());
                intervistaMod.setTipo(intervista.getTipo());
                intervistaMod.setTipologia(intervista.getTipologia());
                intervistaMod.setValutazione(intervista.getValutazione());
            }

            return intervistaMod;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/react/mod/ricerca/{idCandidato}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public IntervistaGroup showIntervistaModIdListRicerca(
        @PathVariable("idCandidato") Integer idCandidato,
        @RequestParam("data") @Nullable String dataColloquio,
        @RequestParam("stato") @Nullable Integer stato,
        @RequestParam("owner") @Nullable Integer owner,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Interviste candidato tramite id mod");

        try {
            Pageable        p                 = PageRequest.of(pagina, quantita);
            Date            dataColloquioDate = null;
            IntervistaGroup intervistaGroup   = new IntervistaGroup();

            if(null != dataColloquio) {
                dataColloquioDate = Date.valueOf(dataColloquio);
            }

            List<Intervista> interviste = intervistaRepository.ricercaByStato_IdAndOwner_IdAndDataColloquioAndCandidato_Id(stato, owner, dataColloquioDate, idCandidato, p).getContent();
            List<IntervistaModificato> intervisteMod = new ArrayList<>();

            for (Intervista intervista : interviste) {
                IntervistaModificato intervistaMod = new IntervistaModificato();

                intervistaMod.setAderenza(intervista.getAderenza());
                intervistaMod.setAnniEsperienza(intervista.getAnniEsperienza());
                intervistaMod.setAttuale(intervista.getAttuale());

                if (null != intervista.getCandidato()) {
                    Candidato candidato = new Candidato();

                    candidato.setId(intervista.getCandidato().getId());
                    candidato.setCognome(intervista.getCandidato().getCognome());
                    candidato.setNome(intervista.getCandidato().getNome());

                    intervistaMod.setCandidato(candidato);
                }
                intervistaMod.setCoerenza(intervista.getCoerenza());
                intervistaMod.setCognome(intervista.getCognome());
                intervistaMod.setCompetenze(intervista.getCompetenze());
                intervistaMod.setComunicazione(intervista.getComunicazione());
                intervistaMod.setDataAggiornamento(intervista.getDataAggiornamento());
                intervistaMod.setDataAVideo(intervista.getDataAVideo());
                intervistaMod.setDataColloquio(intervista.getDataColloquio());
                intervistaMod.setDataNascita(intervista.getDataNascita());
                intervistaMod.setDescrizioneCandidato(intervista.getDescrizioneCandidato());
                intervistaMod.setDescrizioneCandidatoUna(intervista.getDescrizioneCandidatoUna());
                intervistaMod.setDesiderata(intervista.getDesiderata());
                intervistaMod.setDisponibilita(intervista.getDisponibilita());
                intervistaMod.setEnergia(intervista.getEnergia());
                intervistaMod.setId(intervista.getId());
                intervistaMod.setInglese(intervista.getInglese());
                intervistaMod.setMobilita(intervista.getMobilita());
                intervistaMod.setMotivazione(intervista.getMotivazione());
                intervistaMod.setNextOwner(intervista.getNextOwner());
                intervistaMod.setNome(intervista.getNome());
                intervistaMod.setOraAVideo(intervista.getOraAVideo());
                intervistaMod.setOwner(intervista.getOwner());
                intervistaMod.setPreavviso(intervista.getPreavviso());
                intervistaMod.setProposta(intervista.getProposta());
                intervistaMod.setRecapiti(intervista.getRecapiti());
                intervistaMod.setStanding(intervista.getStanding());
                intervistaMod.setStato(intervista.getStato());
                intervistaMod.setTeamSiNo(intervista.getTeamSiNo());
                intervistaMod.setTipo(intervista.getTipo());
                intervistaMod.setTipologia(intervista.getTipologia());
                intervistaMod.setValutazione(intervista.getValutazione());

                intervisteMod.add(intervistaMod);
            }

            intervistaGroup.setInterviste(intervisteMod);
            intervistaGroup.setRecord(intervistaRepository.countRicercaByStato_IdAndOwner_IdAndDataColloquioAndCandidato_Id(stato, owner, dataColloquioDate, idCandidato));

            return intervistaGroup;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @PostMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveNeed(
        @RequestParam("idCandidato") Integer idCandidato,
        @RequestBody Map<String,String> intervistaMap,
        @RequestParam("modifica") Integer modifica,
        @RequestParam("note") @Nullable String note
    ) {
        logger.info("Salva Intervista");

        try {

            Intervista intervista = new Intervista();

            if(intervistaMap.get("id") != null) {
                intervista = intervistaRepository.findById(Integer.parseInt(intervistaMap.get("id"))).get();

                logger.debug("Intervista trovata si procede in modifica");
            }

            trasformaMappaInIntervista(intervista, intervistaMap);

            Candidato candidato    = candidatoRepository.findById(idCandidato).get();
            Integer   idIntervista = intervista.getId();

            candidato.setNote(note);

            if (( null != idIntervista ) && modifica == 0) {
                intervista.setId(intervistaRepository.findMaxId() + 1);
            }

            List<Intervista> interviste = intervistaRepository.findByCandidato_Id(idCandidato);

            interviste.add(intervista);

            candidato.setRating(ricalcoloRating(interviste));

            if (intervista.getTeamSiNo() == 3) {
                StatoC stato = new StatoC();
                stato.setId(22);
                candidato.setStato(stato);
            } else {
                candidato.setStato(calcolaStato(candidato.getRating()));
            }

            intervista.setCandidato(candidato);

            intervistaRepository.save(intervista);

            logger.debug("Intervista salvata correttamente");

            return "OK";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @DeleteMapping("/react/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteIntervista(
        @PathVariable("id") Integer id
    ){
        logger.info("Elimina intervista");

        try {
            intervistaRepository.deleteById(id);

            logger.debug("Intervista eliminata correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public Double ricalcoloRating(List<Intervista> interviste){
        logger.debug("Ricalcolo rating");

        double rating = 0.0;

        for (Intervista intervista : interviste) {
            rating += (((intervista.getCompetenze()      != null ? intervista.getCompetenze()      : 0) +
                (intervista.getCoerenza()      != null ? intervista.getCoerenza()      : 0) +
                (intervista.getValutazione()   != null ? intervista.getValutazione()   : 0) +
                (intervista.getStanding()      != null ? intervista.getStanding()      : 0) +
                (intervista.getEnergia()       != null ? intervista.getEnergia()       : 0) +
                (intervista.getComunicazione() != null ? intervista.getComunicazione() : 0) +
                (intervista.getInglese()       != null ? intervista.getInglese()       : 0))/7.0);
        }

        return rating/interviste.size();
    }

    public StatoC calcolaStato(Double rating) {
        StatoC stato = new StatoC();
        if(rating > 0 && rating <= 1.9) {
            stato.setId(21);
        } else if (rating >= 2.0 && rating <= 2.9) {
            stato.setId(20);
        } else if (rating >= 3.0) {
            stato.setId(19);
        }
        return stato;
    }

    public void trasformaMappaInIntervista(Intervista intervista, Map<String,String> staffMap) {
        logger.debug("Trasforma mappa in intervista");

        intervista.setAnniEsperienza(staffMap.get("anniEsperienza") != null ? Double.parseDouble(staffMap.get("anniEsperienza")) : null);
        intervista.setAderenza(staffMap.get("aderenza") != null ? Integer.parseInt(staffMap.get("aderenza")) : null);
        intervista.setAttuale(staffMap.get("attuale") != null ? staffMap.get("attuale") : null);
        intervista.setCoerenza(staffMap.get("coerenza") != null ? Integer.parseInt(staffMap.get("coerenza")) : null);
        intervista.setCognome(staffMap.get("cognome") != null ? staffMap.get("cognome") : null);
        intervista.setCompetenze(staffMap.get("competenze") != null ? Integer.parseInt(staffMap.get("competenze")) : null);;
        intervista.setComunicazione(staffMap.get("comunicazione") != null ? Integer.parseInt(staffMap.get("comunicazione")) : null);
        intervista.setDataNascita(staffMap.get("dataNascita") != null ? Date.valueOf(staffMap.get("dataNascita")) : null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        intervista.setDataAggiornamento(staffMap.get("dataAggiornamento") != null ? LocalDateTime.parse(staffMap.get("dataAggiornamento"), formatter) : null);
        intervista.setDataAVideo(staffMap.get("dataAVideo") != null ? staffMap.get("dataAVideo") : null);
        intervista.setDataColloquio(staffMap.get("dataColloquio") != null ? Date.valueOf(staffMap.get("dataColloquio")) : null);
        intervista.setDescrizioneCandidato(staffMap.get("descrizioneCandidato") != null ? staffMap.get("descrizioneCandidato") : null);
        intervista.setNome(staffMap.get("nome") != null ? staffMap.get("nome") : null);
        intervista.setDescrizioneCandidatoUna(staffMap.get("descrizioneCandidatoUna") != null ? staffMap.get("descrizioneCandidatoUna") : null);
        intervista.setDesiderata(staffMap.get("desiderata") != null ? staffMap.get("desiderata") : null);
        intervista.setDisponibilita(staffMap.get("disponibilita") != null ? staffMap.get("disponibilita") : null);
        intervista.setEnergia(staffMap.get("energia") != null ? Integer.parseInt(staffMap.get("energia")) : null);

        if (staffMap.get("idNextOwner") != null) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(staffMap.get("idNextOwner")));

            intervista.setNextOwner(owner);
        }

        if (staffMap.get("idOwner") != null) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(staffMap.get("idOwner")));

            intervista.setOwner(owner);
        }

        intervista.setInglese(staffMap.get("inglese") != null ? Integer.parseInt(staffMap.get("inglese")) : null);
        intervista.setMobilita(staffMap.get("mobilita") != null ? staffMap.get("mobilita") : null);
        intervista.setMotivazione(staffMap.get("motivazione") != null ? Integer.parseInt(staffMap.get("motivazione")) : null);
        intervista.setOraAVideo(staffMap.get("oraAVideo") != null ? staffMap.get("oraAVideo") : null);
        intervista.setPreavviso(staffMap.get("preavviso") != null ? staffMap.get("preavviso") : null);
        intervista.setProposta(staffMap.get("proposta") != null ? staffMap.get("proposta") : null);
        intervista.setRecapiti(staffMap.get("recapiti") != null ? staffMap.get("recapiti") : null);;
        intervista.setStanding(staffMap.get("standing") != null ? Integer.parseInt(staffMap.get("standing")) : null);
        intervista.setTeamSiNo(staffMap.get("teamSiNo") != null ? Integer.parseInt(staffMap.get("teamSiNo")) : null);
        intervista.setTipologia(staffMap.get("tipologia") != null ? staffMap.get("tipologia") : null);
        intervista.setValutazione(staffMap.get("valutazione") != null ? Integer.parseInt(staffMap.get("valutazione")) : null);

        if (staffMap.get("stato") != null) {
            StatoC stato = new StatoC();
            stato.setId(Integer.parseInt(staffMap.get("stato")));

            intervista.setStato(stato);
        }

        if (staffMap.get("tipo") != null) {
            TipologiaI tipo = new TipologiaI();
            tipo.setId(Integer.parseInt(staffMap.get("tipo")));

            intervista.setTipo(tipo);
        }
    }
}

