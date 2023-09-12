/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.*;
import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.entity.timesheet.Calendario;
import it.innotek.wehub.entity.timesheet.Progetto;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.*;
import it.innotek.wehub.util.UtilLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/associazioni")
public class AssociazioneController {

    @Autowired
    private StatoAService       serviceStato;
    @Autowired
    private CandidatoService    serviceCandidato;
    @Autowired
    private ClienteService      serviceCliente;
    @Autowired
    private AssociazioniService serviceAssociazione;
    @Autowired
    private NeedService         serviceNeed;
    @Autowired
    private StaffService        serviceStaff;
    @Autowired
    private ProgettoService     serviceProgetto;
    @Autowired
    private StatoAService        serviceStatoA;

    @RequestMapping("/ricerca/{idCandidato}")
    public String showRicercaAssociazioniList(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model,
        AssociazioneCandidatoNeed associazione
    ) throws ElementoNonTrovatoException {

        Need                            needAssociazione  = associazione.getNeed();
        StatoA                          statoAssociazione = associazione.getStato();
        Date                            dataAssociazione  = associazione.getDataModifica();
        Integer                         idCliente         = needAssociazione != null ? ( needAssociazione.getCliente() != null ? needAssociazione.getCliente().getId() : null ) : null;
        Integer                         idStato           = statoAssociazione != null ? statoAssociazione.getId() : null;
        List<AssociazioneCandidatoNeed> listAssociazioni  =
            serviceAssociazione.listRicerca(
                idCandidato,
                idCliente,
                idStato,
                dataAssociazione
            );

        model.addAttribute("listAssociazioni",    listAssociazioni);
        model.addAttribute("associazioneRicerca", associazione);
        model.addAttribute("listaAziende",        serviceCliente.listAll());
        model.addAttribute("listaStatiA",         serviceStato.listAll());
        model.addAttribute("candidato",           serviceCandidato.get(idCandidato));

        return "lista_associazioni_candidato";
    }

    @RequestMapping("/aggiungi/{idNeed}/{idCandidato}")
    public String showNewForm(@PathVariable("idNeed") Integer idNeed, @PathVariable("idCandidato") Integer idCandidato, Model model ) throws ElementoNonTrovatoException {

        Candidato                 candidato    = serviceCandidato.get(idCandidato);
        Need                      need         = serviceNeed.get(idNeed);
        AssociazioneCandidatoNeed associazione = new AssociazioneCandidatoNeed();

        associazione.setNeed(need);
        associazione.setCandidato(candidato);

        model.addAttribute("associazione", associazione);
        model.addAttribute("listaStati",   serviceStato.listAll());
        model.addAttribute("titoloPagina", "Modifica stato associazione");

        return "associazione_form";
    }

    @RequestMapping("/salva")
    public String saveFornitore(AssociazioneCandidatoNeed associazione, RedirectAttributes ra) throws ElementoNonTrovatoException {

        Integer                         idNeed       = associazione.getNeed().getId();
        Integer                         idCandidato  = associazione.getCandidato().getId();
        List<AssociazioneCandidatoNeed> associazioni = serviceAssociazione.getAssociazione(idNeed,idCandidato, associazione.getStato().getId(),associazione.getDataModifica() );

        if ((null == associazioni) || associazioni.isEmpty()) {
            if (associazione.getStato().getId() == 11 ) {  //Staffato
                Need    need    = serviceNeed.get(associazione.getNeed().getId());
                Integer risorse = need.getNumeroRisorse();

                need.setNumeroRisorse(risorse-1);

                if (risorse == 1) {
                    StatoN statoNeed = new StatoN();

                    statoNeed.setId(3); //Vinto
                    need.setStato(statoNeed);
                }

                serviceNeed.save(need);
                serviceAssociazione.save(associazione);

                Candidato  candidato = serviceCandidato.get(idCandidato);
                Set<Skill> skills    = candidato.getSkills();
                Staff      staff     = trasformaCandidatoInStaff(candidato);
                Calendar   calendar  = Calendar.getInstance();

                calendar.setTime(associazione.getDataModifica());

                int               anno       = calendar.get(Calendar.YEAR);
                int               mese       = calendar.get(Calendar.MONTH) + 1;
                Progetto          progetto   = new Progetto();
                TipologiaProgetto tipologia  = new TipologiaProgetto();
                Calendario        calendario = UtilLib.creazioneCalendarioLib(anno, mese, 1, progetto);

                tipologia.setId(1);
                progetto.setDescription("Ferie, Permessi e Malattia");
                progetto.setTipologia(tipologia);

                serviceProgetto.save(progetto);

                staff.getProgetti().add(progetto);
                staff.setTimesheet(calendario);
                serviceStaff.save(staff);

                serviceCandidato.delete(idCandidato);

                return "redirect:/need";
            }

            serviceAssociazione.save(associazione);

            ra.addFlashAttribute("message", "L'associazione è stata salvata con successo" );

            return "redirect:/need/match/"+idNeed;
        } else {
            ra.addFlashAttribute("message", "Associazione già presente");
            return "redirect:/associazioni/aggiungi/" + idNeed + "/" + idCandidato;
        }

    }

    private Staff trasformaCandidatoInStaff(Candidato candidato) throws ElementoNonTrovatoException {

        Staff  staff     = new Staff();
        String nomeEmail = candidato.getNome().charAt(0) + "." + candidato.getCognome();
        String email     = nomeEmail.toLowerCase() + "@inno-tek.it";

        if (controllaMailDuplicata(email) == 1) {

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

    public Integer controllaMailDuplicata(String email) throws ElementoNonTrovatoException {

        return serviceStaff.controllaEmail(email);
    }

    @RequestMapping("/{idCandidato}")
    public String showAssociazioniCandidatoList(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model
    ) throws ElementoNonTrovatoException {

        Candidato                       candidato             = serviceCandidato.get(idCandidato);
        List<AssociazioneCandidatoNeed> listAssociazioni      = serviceAssociazione.listAllByCandidato(idCandidato);
        List<Need>                      listaNeedsAssociabili = serviceNeed.listNeedAssociabiliCandidato(idCandidato);

        model.addAttribute("candidato",           candidato);
        model.addAttribute("associazioneRicerca", new AssociazioneCandidatoNeed());
        model.addAttribute("listaAziende",        serviceCliente.listAll());
        model.addAttribute("listaStatiA",         serviceStatoA.listAll());
        model.addAttribute("listNeed",            listaNeedsAssociabili);
        model.addAttribute("listAssociazioni",    listAssociazioni);

        return "lista_associazioni_candidato";
    }

    @RequestMapping("/associa/{idNeed}/{idCandidato}")
    public String showAssociazioniAssociaCandidato(
        @PathVariable("idCandidato") Integer idCandidato,
        @PathVariable("idNeed") Integer idNeed,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {
        try {
            AssociazioneCandidatoNeed       associazione          = new AssociazioneCandidatoNeed();
            StatoA                          statoa                = new StatoA();
            Need                            need                  = serviceNeed.get(idNeed);
            Candidato                       candidato             = serviceCandidato.get(idCandidato);
            long                            millis                = System.currentTimeMillis();
            List<AssociazioneCandidatoNeed> listAssociazioni      = serviceAssociazione.listAllByCandidato(idCandidato);
            List<Need>                      listaNeedsAssociabili = serviceNeed.listNeedAssociabiliCandidato(idCandidato);

            statoa.setId(1);
            associazione.setCandidato(candidato);
            associazione.setNeed(need);
            associazione.setStato(statoa);
            associazione.setDataModifica(new Date(millis));

            serviceAssociazione.save(associazione);

            candidato.getNeeds().add(need);
            serviceCandidato.save(candidato);

            model.addAttribute("listAssociazioni",    listAssociazioni);
            model.addAttribute("candidato",           candidato);
            model.addAttribute("associazioneRicerca", new AssociazioneCandidatoNeed());
            model.addAttribute("listaAziende",        serviceCliente.listAll());
            model.addAttribute("listaStatiA",         serviceStatoA.listAll());
            model.addAttribute("listaNeed",           listaNeedsAssociabili);
            model.addAttribute("listAssociazioni",    listAssociazioni);

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/associazioni/"+idCandidato;
        }

        return "redirect:/associazioni/"+idCandidato;
    }
}
