/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.EmailSenderService;
import it.innotek.wehub.entity.Candidato;
import it.innotek.wehub.entity.Intervista;
import it.innotek.wehub.entity.Owner;
import it.innotek.wehub.entity.TimedEmail;
import it.innotek.wehub.entity.timesheet.Email;
import it.innotek.wehub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
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

    @RequestMapping("/{idCandidato}")
    public String showIntervistaIdList(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model
    ) {
        try {
            Candidato        candidato      = candidatoRepository.findById(idCandidato).get();
            List<Intervista> listInterviste = intervistaRepository.findByCandidato_Id(idCandidato);

            model.addAttribute("listInterviste", listInterviste);
            model.addAttribute("candidato", candidato);
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStati", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("intervistaRicerca", new Intervista());
            model.addAttribute("ultimoIdIntervista", candidatoRepository.findUltimoIdIntervistaCandidato(idCandidato));

            return "lista_interviste_candidato";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca/{idCandidato}")
    public String showRicercaFornitoriList(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model,
        Intervista intervista
    ) {
        try {
            Integer          idStato        = intervista.getStato() != null ? intervista.getStato().getId() : null;
            Date             dataColloquio  = ( intervista.getDataColloquio() != null ) ? intervista.getDataColloquio() : null;
            List<Intervista> listInterviste = intervistaRepository.ricercaByStato_IdAndOwner_IdAndDataColloquioAndCandidato_Id(idStato, null, dataColloquio, idCandidato);
            Candidato        candidato      = candidatoRepository.findById(idCandidato).get();

            model.addAttribute("listInterviste", listInterviste);
            model.addAttribute("candidato", candidato);
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStati", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("intervistaRicerca", intervista);
            model.addAttribute("ultimoIdIntervista", candidatoRepository.findUltimoIdIntervistaCandidato(idCandidato));

            return "lista_interviste_candidato";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi/{idCandidato}")
    public String showNewForm(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model
    ) {
        try {
            Candidato  candidato        = candidatoRepository.findById(idCandidato).get();
            Intervista ultimaIntervista = intervistaRepository.findByCandidato_IdOrderByIdAsc(idCandidato);

            if (null != ultimaIntervista) {
                model.addAttribute("intervista", ultimaIntervista);
            } else {
                model.addAttribute("intervista", new Intervista());
            }

            model.addAttribute("titoloPagina", "Aggiungi un nuovo incontro");
            model.addAttribute("candidato", candidato);
            model.addAttribute("modifica", 0);
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipologieI", tipologiaIRepository.findAllByOrderByIdAsc());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStati", statoCRepository.findAllByOrderByIdAsc());

            return "intervista_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva/{idCandidato}/{modifica}")
    public String saveNeed(
        @PathVariable("idCandidato") Integer idCandidato,
        Intervista intervista,
        @PathVariable("modifica") Integer modifica,
        RedirectAttributes ra
    ) {
        try {
            Candidato candidato    = candidatoRepository.findById(idCandidato).get();
            Integer   idIntervista = intervista.getId();

            intervista.setCandidato(candidato);

            if (( null != idIntervista ) && modifica == 0) {
                intervista.setId(idIntervista + 1);
            }

            intervistaRepository.save(intervista);

            candidato.setRating(ricalcoloRating(intervista));
            candidato.setStato(intervista.getStato());

            candidatoRepository.save(candidato);

            Owner owner = ownerRepository.findById(intervista.getNextOwner().getId()).get();

            salvaEmailTemporizzata(owner.getEmail(), owner.getNome(), owner.getCognome(), candidato.getNome(), candidato.getCognome(), candidato.getEmail(), candidato.getCellulare(), intervista.getDataAggiornamento(), 0);

            Long idEmailTemporizzata = timedEmailRepository.findMaxId();

            inviaEmail(owner.getEmail(), owner.getNome(), owner.getCognome(), candidato.getNome(), candidato.getCognome(), candidato.getEmail(), candidato.getCellulare(), intervista.getDataAggiornamento(), idEmailTemporizzata);

            ra.addFlashAttribute("message", "l'incontro è stato salvato con successo");
            return "redirect:/intervista/" + idCandidato;

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/modifica/{idCandidato}/{id}")
    public String showEditForm(
        @PathVariable("idCandidato") Integer idCandidato,
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            Candidato  candidato  = candidatoRepository.findById(idCandidato).get();
            Intervista intervista = intervistaRepository.findById(id).get();

            model.addAttribute("intervista", intervista);
            model.addAttribute("titoloPagina", "Modifica incontro");
            model.addAttribute("candidato", candidato);
            model.addAttribute("modifica", 1);
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStati", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("listaTipologieI", tipologiaIRepository.findAllByOrderByIdAsc());

            return "intervista_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/visualizza/{idCandidato}/{id}")
    public String showForm(
        @PathVariable("idCandidato") Integer idCandidato,
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Candidato  candidato  = candidatoRepository.findById(idCandidato).get();
            Intervista intervista = intervistaRepository.findById(id).get();

            model.addAttribute("intervista", intervista);
            model.addAttribute("titoloPagina", "Visualizza incontro");
            model.addAttribute("candidato", candidato);
            model.addAttribute("titoloPagina", "Gestisci Incontro");
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStati", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("listaTipologieI", tipologiaIRepository.findAllByOrderByIdAsc());

            return "intervista_no_edit_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/{idCandidato}/{id}")
    public String deleteNeed(
        @PathVariable("idCandidato") Integer idCandidato,
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            intervistaRepository.deleteById(id);
            ra.addFlashAttribute("message", "L'incontro è stato cancellato con successo");
            return "redirect:/intervista/" + idCandidato;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    public Double ricalcoloRating(Intervista intervista){

        return ((intervista.getAderenza()      != null ? intervista.getAderenza()      : 0) +
                (intervista.getCoerenza()      != null ? intervista.getCoerenza()      : 0) +
                (intervista.getMotivazione()   != null ? intervista.getMotivazione()   : 0) +
                (intervista.getStanding()      != null ? intervista.getStanding()      : 0) +
                (intervista.getEnergia()       != null ? intervista.getEnergia()       : 0) +
                (intervista.getComunicazione() != null ? intervista.getComunicazione() : 0) +
                (intervista.getInglese()       != null ? intervista.getInglese()       : 0))/7.0;
    }

    public void inviaEmail(
        String emailOwner,
        String nomeOwner,
        String cognomeOwner,
        String nomeCandidato,
        String cognomeCandidato,
        String emailCandidato,
        String cellCandidato,
        LocalDateTime dataAggiornamento,
        Long idEmailTemporizzata
    ){
        Email               email = new Email();
        Map<String, Object> mappa = new HashMap<>();

        email.setFrom("sviluppo@inno-tek.it");
        email.setTo(emailOwner);
        mappa.put("nome", nomeOwner);
        mappa.put("cognome", cognomeOwner);
        mappa.put("nomeCandidato", nomeCandidato);
        mappa.put("cognomeCandidato", cognomeCandidato);
        mappa.put("mailCandidato", emailCandidato);
        mappa.put("cellCandidato", cellCandidato);
        email.setProperties(mappa);
        email.setSubject("Reminder per intervista successiva a " + nomeCandidato + " " + cognomeCandidato);
        email.setTemplate("reminder-email.html");

        serviceEmail.sendHtmlMessagePost(email, dataAggiornamento, idEmailTemporizzata);
    }

    public void salvaEmailTemporizzata(
        String emailOwner,
        String nomeOwner,
        String cognomeOwner,
        String nomeCandidato,
        String cognomeCandidato,
        String emailCandidato,
        String cellCandidato,
        LocalDateTime dataAggiornamento,
        Integer inviata
    ){
        TimedEmail emailTemporizzata =
            new TimedEmail(
                nomeOwner,
                cognomeOwner,
                emailOwner,
                nomeCandidato,
                cognomeCandidato,
                emailCandidato,
                cellCandidato,
                dataAggiornamento,
                inviata
            );

        timedEmailRepository.save(emailTemporizzata);
    }
}