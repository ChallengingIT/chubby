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
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.*;
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
    private CandidatoService   serviceCandidato;
    @Autowired
    private TipologiaService   serviceTipologia;
    @Autowired
    private TipologiaIService  serviceTipologiaI;
    @Autowired
    private StatoCService      serviceStatoC;
    @Autowired
    private OwnerService       serviceOwner;
    @Autowired
    private IntervistaService  serviceIntervista;
    @Autowired
    private EmailSenderService serviceEmail;
    @Autowired
    private TimedEmailService  serviceTimedEmail;

    @RequestMapping("/{idCandidato}")
    public String showIntervistaIdList(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model
    ) throws ElementoNonTrovatoException {
        Candidato        candidato      = serviceCandidato.get(idCandidato);
        List<Intervista> listInterviste = serviceIntervista.listAllByCandidato(idCandidato);

        model.addAttribute("listInterviste",     listInterviste);
        model.addAttribute("candidato",          candidato);
        model.addAttribute("listaOwner",         serviceOwner.listAll());
        model.addAttribute("listaStati",         serviceStatoC.listAllOrdered());
        model.addAttribute("intervistaRicerca",  new Intervista());
        model.addAttribute("ultimoIdIntervista", serviceCandidato.getUltimoIdIntervista(idCandidato));

        return "lista_interviste_candidato";
    }

    @RequestMapping("/ricerca/{idCandidato}")
    public String showRicercaFornitoriList(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model,
        Intervista intervista
    ) throws ElementoNonTrovatoException {

        Integer          idStato        = intervista.getStato() != null ? intervista.getStato().getId() : null;
        Date             dataColloquio  = ( intervista.getDataColloquio() != null ) ? intervista.getDataColloquio() : null;
        List<Intervista> listInterviste = serviceIntervista.listRicerca(idStato, null, dataColloquio, idCandidato);
        Candidato        candidato      = serviceCandidato.get(idCandidato);

        model.addAttribute("listInterviste",     listInterviste);
        model.addAttribute("candidato",          candidato);
        model.addAttribute("listaOwner",         serviceOwner.listAll());
        model.addAttribute("listaStati",         serviceStatoC.listAllOrdered());
        model.addAttribute("intervistaRicerca",  intervista);
        model.addAttribute("ultimoIdIntervista", serviceCandidato.getUltimoIdIntervista(idCandidato));

        return "lista_interviste_candidato";
    }

    @RequestMapping("/aggiungi/{idCandidato}")
    public String showNewForm(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model
    ) throws ElementoNonTrovatoException {
        Candidato  candidato        = serviceCandidato.get(idCandidato);
        Intervista ultimaIntervista = serviceIntervista.getUltimaByIdCandidato(idCandidato);

        if (null != ultimaIntervista) {
            model.addAttribute("intervista", ultimaIntervista);
        } else {
            model.addAttribute("intervista", new Intervista());
        }

        model.addAttribute("titoloPagina",    "Aggiungi un nuovo incontro");
        model.addAttribute("candidato",       candidato);
        model.addAttribute("modifica",        0);
        model.addAttribute("listaTipologie",  serviceTipologia.listAll());
        model.addAttribute("listaTipologieI", serviceTipologiaI.listAll());
        model.addAttribute("listaOwner",      serviceOwner.listAll());
        model.addAttribute("listaStati",      serviceStatoC.listAllOrdered());

        return "intervista_form";
    }

    @RequestMapping("/salva/{idCandidato}/{modifica}")
    public String saveNeed(
        @PathVariable("idCandidato") Integer idCandidato,
        Intervista intervista,
        @PathVariable("modifica") Integer modifica,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Candidato candidato    = serviceCandidato.get(idCandidato);
        Integer   idIntervista = intervista.getId();

        intervista.setCandidato(candidato);

        if ((null != idIntervista) && modifica == 0) {
            intervista.setId(idIntervista+1);
        }

        serviceIntervista.save(intervista);

        candidato.setRating(ricalcoloRating(intervista));
        candidato.setStato(intervista.getStato());

        serviceCandidato.save(candidato);

        Owner owner = serviceOwner.get(intervista.getOwner().getId());

        salvaEmailTemporizzata(
            owner.getEmail(),
            owner.getNome(),
            owner.getCognome(),
            candidato.getNome(),
            candidato.getCognome(),
            candidato.getEmail(),
            candidato.getCellulare(),
            intervista.getDataAggiornamento(),
            0
        );

        Long idEmailTemporizzata = serviceTimedEmail.getUltimoId();

        inviaEmail(
            owner.getEmail(),
            owner.getNome(),
            owner.getCognome(),
            candidato.getNome(),
            candidato.getCognome(),
            candidato.getEmail(),
            candidato.getCellulare(),
            intervista.getDataAggiornamento(),
            idEmailTemporizzata
        );

        ra.addFlashAttribute("message", "l'incontro è stato salvato con successo");
        return "redirect:/intervista/"+idCandidato;
    }

    @RequestMapping("/modifica/{idCandidato}/{id}")
    public String showEditForm(
        @PathVariable("idCandidato") Integer idCandidato,
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra) {
        try {
            Candidato  candidato  = serviceCandidato.get(idCandidato);
            Intervista intervista = serviceIntervista.get(id);

            model.addAttribute("intervista",      intervista);
            model.addAttribute("titoloPagina",    "Modifica incontro");
            model.addAttribute("candidato",       candidato);
            model.addAttribute("modifica",        1);
            model.addAttribute("listaTipologie",  serviceTipologia.listAll());
            model.addAttribute("listaOwner",      serviceOwner.listAll());
            model.addAttribute("listaStati",      serviceStatoC.listAllOrdered());
            model.addAttribute("listaTipologieI", serviceTipologiaI.listAll());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/intervista/"+idCandidato;
        }
        return "intervista_form";
    }

    @RequestMapping("/visualizza/{idCandidato}/{id}")
    public String showForm(
        @PathVariable("idCandidato") Integer idCandidato,
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Candidato  candidato  = serviceCandidato.get(idCandidato);
            Intervista intervista = serviceIntervista.get(id);

            model.addAttribute("intervista",      intervista);
            model.addAttribute("titoloPagina",    "Visualizza incontro");
            model.addAttribute("candidato",       candidato);
            model.addAttribute("titoloPagina",    "Gestisci Incontro");
            model.addAttribute("listaTipologie",  serviceTipologia.listAll());
            model.addAttribute("listaOwner",      serviceOwner.listAll());
            model.addAttribute("listaStati",      serviceStatoC.listAllOrdered());
            model.addAttribute("listaTipologieI", serviceTipologiaI.listAll());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/intervista/"+idCandidato;
        }
        return "intervista_no_edit_form";
    }

    @RequestMapping("/elimina/{idCandidato}/{id}")
    public String deleteNeed(
        @PathVariable("idCandidato") Integer idCandidato,
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            serviceIntervista.delete(id);
            ra.addFlashAttribute("message", "L'incontro è stato cancellato con successo");
        } catch(ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/intervista/"+idCandidato;
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

        serviceTimedEmail.save(emailTemporizzata);
    }
}