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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Controller
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

    @RequestMapping("/ricerca/{idCandidato}")
    public String showRicercaAssociazioniList(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model,
        AssociazioneCandidatoNeed associazione
    ) {

        try {
            Need       needAssociazione      = associazione.getNeed();
            StatoA     statoAssociazione     = associazione.getStato();
            Date       dataAssociazione      = associazione.getDataModifica();
            Integer    idCliente             = needAssociazione != null ? ( needAssociazione.getCliente() != null ? needAssociazione.getCliente().getId() : null ) : null;
            Integer    idStato               = statoAssociazione != null ? statoAssociazione.getId() : null;
            List<Need> listaNeedsAssociabili = needRepository.findNeedAssociabiliCandidato(idCandidato);
            List<AssociazioneCandidatoNeed> listAssociazioni  =
                associazioniRepository.ricercaByCandidato_IdAndCliente_IdAndStato_IdAndDataModifica(
                    idCandidato,
                    idCliente,
                    idStato,
                    dataAssociazione
                );

            model.addAttribute("listAssociazioni",    listAssociazioni);
            model.addAttribute("associazioneRicerca", associazione);
            model.addAttribute("listaAziende",        clienteRepository.findAll());
            model.addAttribute("listaStatiA",         statoARepository.findAll());
            model.addAttribute("candidato",           candidatoRepository.findById(idCandidato).get());
            model.addAttribute("listNeed",            listaNeedsAssociabili);

            return "lista_associazioni_candidato";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi/{idNeed}/{idCandidato}")
    public String showNewForm(@PathVariable("idNeed") Integer idNeed, @PathVariable("idCandidato") Integer idCandidato, Model model ) {

        try {
            Candidato                 candidato    = candidatoRepository.findById(idCandidato).get();
            Need                      need         = needRepository.findById(idNeed).get();
            AssociazioneCandidatoNeed associazione = new AssociazioneCandidatoNeed();

            associazione.setNeed(need);
            associazione.setCandidato(candidato);

            model.addAttribute("associazione", associazione);
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStati", statoARepository.findAll());
            model.addAttribute("titoloPagina", "Modifica stato associazione");

            return "associazione_form";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveFornitore(AssociazioneCandidatoNeed associazione, RedirectAttributes ra) {

        try {
            Integer                         idNeed       = associazione.getNeed().getId();
            Integer                         idCandidato  = associazione.getCandidato().getId();
            List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByNeed_IdAndCandidato_IdAndStato_IdAndDataModifica(idNeed, idCandidato, associazione.getStato().getId(), associazione.getDataModifica());

            if (( null == associazioni ) || associazioni.isEmpty()) {
                if (associazione.getStato().getId() == 11) {  //Staffato
                    Need    need    = needRepository.findById(associazione.getNeed().getId()).get();
                    Integer risorse = need.getNumeroRisorse();

                    need.setNumeroRisorse(risorse - 1);

                    if (risorse == 1) {
                        StatoN statoNeed = new StatoN();

                        statoNeed.setId(3); //Vinto
                        need.setStato(statoNeed);
                    }

                    needRepository.save(need);
                    associazioniRepository.save(associazione);

                    Candidato candidato = candidatoRepository.findById(idCandidato).get();
                    Staff     staff     = trasformaCandidatoInStaff(candidato);
                    Calendar  calendar  = Calendar.getInstance();

                    calendar.setTime(associazione.getDataModifica());

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

                    candidatoRepository.deleteById(idCandidato);

                    return "redirect:/need";
                }

                associazioniRepository.save(associazione);

                ra.addFlashAttribute("message", "L'associazione è stata salvata con successo");

                return "redirect:/need/match/" + idNeed;
            } else {
                ra.addFlashAttribute("message", "Associazione già presente");
                return "redirect:/associazioni/aggiungi/" + idNeed + "/" + idCandidato;
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/{idNeed}/{id}")
    public String deleteAssociazione(
        @PathVariable("idNeed") Integer idNeed,
        @PathVariable("id") Integer id,
        RedirectAttributes ra) {

        try {

            associazioniRepository.deleteById(id);

            return "redirect:/need/match/" + idNeed;

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/candidato/{idNeed}/{idCandidato}")
    public String deleteNeddCandidato(
        @PathVariable("idNeed") Integer idNeed,
        @PathVariable("idCandidato") Integer idCandidato,
        RedirectAttributes ra) {

        try {
            Need need = needRepository.findById(idNeed).get();
            Candidato candidato = candidatoRepository.findById(idCandidato).get();

            List<AssociazioneCandidatoNeed> associazioni =
                candidato.getAssociazioni()
                         .stream()
                         .filter(a -> a.getNeed().getId() == idNeed)
                         .toList();

            candidato.getNeeds().remove(need);
            candidato.getAssociazioni().removeAll(associazioni);
            candidatoRepository.save(candidato);

            need.getAssociazioni().removeAll(associazioni);

            needRepository.save(need);

            for (AssociazioneCandidatoNeed associazione: associazioni) {
                associazioniRepository.deleteById(associazione.getId());
            }

            return "redirect:/need/match/" + idNeed;

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    private Staff trasformaCandidatoInStaff(Candidato candidato){

        Staff  staff     = new Staff();
        String nomeEmail = candidato.getNome().charAt(0) + "." + candidato.getCognome();
        String email     = nomeEmail.toLowerCase() + "@challenging.cloud";

        if (controllaMailDuplicata(email)) {

            nomeEmail = candidato.getNome() + "." + candidato.getCognome();
            email     = nomeEmail.toLowerCase()+"@challenging.cloud";
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

        return !staffRepository.findByEmail(email).isEmpty();
    }

    @RequestMapping("/{idCandidato}")
    public String showAssociazioniCandidatoList(
        @PathVariable("idCandidato") Integer idCandidato,
        Model model
    ){
        try {
            Candidato                       candidato             = candidatoRepository.findById(idCandidato).get();
            List<AssociazioneCandidatoNeed> listAssociazioni      = associazioniRepository.findByCandidato_Id(idCandidato);
            List<Need>                      listaNeedsAssociabili = needRepository.findNeedAssociabiliCandidato(idCandidato);

            model.addAttribute("candidato", candidato);
            model.addAttribute("associazioneRicerca", new AssociazioneCandidatoNeed());
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaStatiA", statoARepository.findAll());
            model.addAttribute("listNeed", listaNeedsAssociabili);
            model.addAttribute("listAssociazioni", listAssociazioni);

            return "lista_associazioni_candidato";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/associa/{idNeed}/{idCandidato}")
    public String showAssociazioniAssociaCandidato(
        @PathVariable("idCandidato") Integer idCandidato,
        @PathVariable("idNeed") Integer idNeed,
        Model model,
        RedirectAttributes ra
    ) {

        try {
            AssociazioneCandidatoNeed       associazione          = new AssociazioneCandidatoNeed();
            StatoA                          statoa                = new StatoA();
            Need                            need                  = needRepository.findById(idNeed).get();
            Candidato                       candidato             = candidatoRepository.findById(idCandidato).get();
            long                            millis                = System.currentTimeMillis();
            List<AssociazioneCandidatoNeed> listAssociazioni      = associazioniRepository.findByCandidato_Id(idCandidato);
            List<Need>                      listaNeedsAssociabili = needRepository.findNeedAssociabiliCandidato(idCandidato);

            statoa.setId(1);
            associazione.setCandidato(candidato);
            associazione.setNeed(need);
            associazione.setStato(statoa);
            associazione.setDataModifica(new Date(millis));

            associazioniRepository.save(associazione);

            candidato.getNeeds().add(need);
            candidatoRepository.save(candidato);

            model.addAttribute("listAssociazioni", listAssociazioni);
            model.addAttribute("candidato", candidato);
            model.addAttribute("associazioneRicerca", new AssociazioneCandidatoNeed());
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaStatiA", statoARepository.findAll());
            model.addAttribute("listaNeed", listaNeedsAssociabili);
            model.addAttribute("listAssociazioni", listAssociazioni);

            return "redirect:/associazioni/" + idCandidato;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }
}
