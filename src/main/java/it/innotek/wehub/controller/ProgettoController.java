/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.TipologiaProgetto;
import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.entity.timesheet.Progetto;
import it.innotek.wehub.repository.ClienteRepository;
import it.innotek.wehub.repository.ProgettoRepository;
import it.innotek.wehub.repository.StaffRepository;
import it.innotek.wehub.util.UtilLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/progetti")
public class ProgettoController {

    @Autowired
    private ProgettoRepository progettoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private StaffRepository   staffRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProgettoController.class);

    @RequestMapping
    public String getProgetti(Model model){
        try {
            List<Progetto> listProgetti = progettoRepository.findAll();

            for (Progetto progetto : listProgetti) {
                if (( null != progetto.getTipologia() ) && progetto.getTipologia().getId() != 1) {
                    if (null != progetto.getStaff()) {
                        progetto.setDurataEffettiva(progettoRepository.getOreEffettive(progetto.getId(), progetto.getStaff().getId()));
                    } else {
                        progetto.setDurataEffettiva(0);
                    }

                    if (( null != progetto.getDurataStimata() ) && ( null != progetto.getMargine() )) {
                        progetto.setMolTotale(( progetto.getMargine() * progetto.getDurataStimata() ) + progetto.getDurataEffettiva());
                    } else {
                        progetto.setMolTotale(0);
                    }

                    if (null != progetto.getRate()) {
                        progetto.setValoreTotale(progetto.getRate() * ( progetto.getDurataStimata() - progetto.getDurataEffettiva() ));
                    } else {
                        progetto.setValoreTotale(0);
                    }

                    progettoRepository.save(progetto);
                }
            }

            model.addAttribute("listProgetti", listProgetti);
            model.addAttribute("progettoRicerca", new Progetto());
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaDipendenti", staffRepository.findAll());

            return "progetti";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showRicercaProgettiList(
        Model model,
        Progetto progetto
    ){
        try {
            String         denominazione = ( ( null != progetto.getDescription() ) && !progetto.getDescription().isEmpty() ) ? progetto.getDescription() : null;
            Integer        idCliente     = ( ( null != progetto.getCliente() ) && ( null != progetto.getCliente().getId() ) ) ? progetto.getCliente().getId() : null;
            List<Progetto> listProgetti  = progettoRepository.ricercaByDenominazioneAndIdCliente(denominazione, idCliente);

            model.addAttribute("listProgetti", listProgetti);
            model.addAttribute("progettoRicerca", progetto);
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaDipendenti", staffRepository.findAll());

            return "progetti";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(Model model){
        try {
            if (null != model.getAttribute("progetto")) {
                model.addAttribute("progetto", model.getAttribute("progetto"));
            } else {
                model.addAttribute("progetto", new Progetto());
            }

            model.addAttribute("titoloPagina", "Aggiungi un nuovo progetto");
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaDipendenti", staffRepository.findAll());

            return "progetto_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveProgetto(
        Progetto progetto,
        RedirectAttributes ra
    ) {
        try {
            if (null != progetto.getMargine()) {
                if (null != progetto.getRate()) {
                    progetto.setMarginePerc(progetto.getMargine() / progetto.getRate());
                } else {
                    progetto.setMarginePerc(0);
                }
            } else {
                progetto.setMarginePerc(0);
            }
            progetto.setDurataEffettiva(0);

            if (( null != progetto.getDurataStimata() ) && ( null != progetto.getMargine() )) {
                progetto.setMolTotale(( progetto.getMargine() * progetto.getDurataStimata() ) + progetto.getDurataEffettiva());
            } else {
                progetto.setMolTotale(0);
            }

            if (null != progetto.getRate()) {
                progetto.setValoreTotale(progetto.getRate() * ( progetto.getDurataStimata() - progetto.getDurataEffettiva() ));
            } else {
                progetto.setValoreTotale(0);
            }

            if (null != progetto.getId()) {

                Progetto progettoVecchio = progettoRepository.findById(progetto.getId()).get();
                Staff    staff           = staffRepository.findByProgetto_Id(progetto.getId());

                if (null == staff) {
                    TipologiaProgetto tipologia = new TipologiaProgetto();
                    tipologia.setId(2);
                    progetto.setTipologia(tipologia);
                    progettoRepository.save(progetto);

                    staff = staffRepository.findById(progetto.getStaff().getId()).get();

                    staff.setTimesheet(UtilLib.aggiornaProgettoCalendario(staff.getTimesheet(), progetto));
                    staffRepository.save(staff);
                } else if (staff.getId().equals(progetto.getStaff().getId())) {

                    LocalDate dataInizioVecchia = progettoVecchio.getInizio();
                    LocalDate dataFineVecchia   = progettoVecchio.getScadenza();
                    LocalDate dataInizio        = progetto.getInizio();
                    LocalDate dataFine          = progetto.getScadenza();

                    if (!dataInizioVecchia.isEqual(dataInizio)) {
                        if (dataInizio.getMonthValue() != dataInizioVecchia.getMonthValue() || dataInizio.getYear() != dataInizioVecchia.getYear()) {

                            LocalDate dataInizioMod;
                            LocalDate dataInizioVecchiaMod;

                            if (dataInizio.isAfter(dataInizioVecchia)) {
                                dataInizioMod        = LocalDate.of(dataInizio.getYear(), dataInizio.getMonthValue(), 1).minusDays(1);
                                dataInizioVecchiaMod = LocalDate.of(dataInizioVecchia.getYear(), dataInizioVecchia.getMonthValue(), 1);

                                staff.setTimesheet(UtilLib.rimuoviProgettoCalendarioConDate(staff.getTimesheet(), progetto, dataInizioVecchiaMod, dataInizioMod));

                            } else {
                                dataInizioMod        = LocalDate.of(dataInizio.getYear(), dataInizio.getMonthValue(), 1);
                                dataInizioVecchiaMod = LocalDate.of(dataInizioVecchia.getYear(), dataInizioVecchia.getMonthValue(), 1).minusDays(1);

                                staff.setTimesheet(UtilLib.aggiornaProgettoCalendarioConDate(staff.getTimesheet(), progetto, dataInizioMod, dataInizioVecchiaMod));
                            }
                        }
                    }
                    if (!dataFineVecchia.isEqual(dataFine)) {

                        if (dataFine.getMonthValue() != dataFineVecchia.getMonthValue() || dataFine.getYear() != dataFineVecchia.getYear()) {
                            if (dataFine.isBefore(dataFineVecchia)) {

                                LocalDate dataFineMod        = LocalDate.of(dataFine.getYear(), dataFine.getMonthValue(), UtilLib.calcolaFineMese(dataFine.getMonthValue(), dataFine.getYear())).plusDays(1);
                                LocalDate dataFineVecchiaMod = LocalDate.of(dataFineVecchia.getYear(), dataFineVecchia.getMonthValue(), UtilLib.calcolaFineMese(dataFineVecchia.getMonthValue(), dataFineVecchia.getYear()));

                                staff.setTimesheet(UtilLib.rimuoviProgettoCalendarioConDate(staff.getTimesheet(), progetto, dataFineMod, dataFineVecchiaMod));
                            } else {
                                LocalDate dataFineVecchiaMod = LocalDate.of(dataFineVecchia.getYear(), dataFineVecchia.getMonthValue(), UtilLib.calcolaFineMese(dataFineVecchia.getMonthValue(), dataFineVecchia.getYear())).plusDays(1);
                                LocalDate dataFineMod        = LocalDate.of(dataFine.getYear(), dataFine.getMonthValue(), UtilLib.calcolaFineMese(dataFine.getMonthValue(), dataFine.getYear()));

                                staff.setTimesheet(UtilLib.aggiornaProgettoCalendarioConDate(staff.getTimesheet(), progetto, dataFineVecchiaMod, dataFineMod));
                            }
                        }
                    }

                    TipologiaProgetto tipologia = new TipologiaProgetto();
                    tipologia.setId(2);
                    progetto.setTipologia(tipologia);
                    progettoRepository.save(progetto);

                    staffRepository.save(staff);
                } else {
                    ra.addFlashAttribute("message", "Il progetto e' associato ad un dipendente diverso, cancellare il progetto per riassegnarne uno nuovo");
                    return "redirect:/progetti/modifica/" + progetto.getId();
                }
            } else {

                TipologiaProgetto tipologia = new TipologiaProgetto();
                tipologia.setId(2);
                progetto.setTipologia(tipologia);
                progettoRepository.save(progetto);

                Staff staff = staffRepository.findById(progetto.getStaff().getId()).get();

                staff.setTimesheet(UtilLib.aggiornaProgettoCalendario(staff.getTimesheet(), progetto));
                staffRepository.save(staff);

            }
            ra.addFlashAttribute("message", "Il progetto e' stato salvato con successo");
            return "redirect:/progetti";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/modifica/{id}")
    public String showEditForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Progetto progetto = progettoRepository.findById(id).get();

            model.addAttribute("progetto", progetto);
            model.addAttribute("titoloPagina", "Modifica progetto");
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaDipendenti", staffRepository.findAll());

            return "progetto_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/{id}")
    public String deleteProgetto(
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            progettoRepository.deleteById(id);

            ra.addFlashAttribute("message", "Il progetto e' stato cancellato con successo");
            return "redirect:/progetti";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/match/{idProgetto}")
    public String showMatchForm(
        @PathVariable("idProgetto") Integer idProgetto,
        Model model
    ) {
        try {
            model.addAttribute("progetto", progettoRepository.findById(idProgetto).get());
            model.addAttribute("titoloPagina", "Associazione staff progetto");
            model.addAttribute("listStaffNonAssociati", staffRepository.findStaffNonAssociati(idProgetto));
            model.addAttribute("listStaffAssociati", staffRepository.findStaffAssociati(idProgetto));
            model.addAttribute("staffRicerca", new Staff());

            return "liste_match_progetto";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca/match/{idProgetto}")
    public String showRicercaMatchForm(
        @PathVariable("idProgetto") Integer idProgetto,
        Model model,
        Staff staff
    ) {
        try {
            String nome    = ( ( null != staff.getNome() ) && !staff.getNome().isEmpty() ) ? staff.getNome() : null;
            String cognome = ( ( null != staff.getCognome() ) && !staff.getCognome().isEmpty() ) ? staff.getCognome() : null;
            String email   = ( ( null != staff.getEmail() ) && !staff.getEmail().isEmpty() ) ? staff.getEmail() : null;

            model.addAttribute("listStaffNonAssociati", staffRepository.ricercaStaffNonAssociati(idProgetto, nome, cognome, email));

            model.addAttribute("progetto", progettoRepository.findById(idProgetto).get());
            model.addAttribute("titoloPagina", "Match del need");
            model.addAttribute("staffRicerca", staff);
            model.addAttribute("listStaffAssociati", staffRepository.findStaffAssociati(idProgetto));

            return "liste_match_progetto";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/associa/{idProgetto}/{idStaff}")
    public String showAssociaStaffForm(
        @PathVariable("idProgetto") Integer idProgetto,
        @PathVariable("idStaff") Integer idStaff,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Progetto progetto = progettoRepository.findById(idProgetto).get();
            Staff    staff    = staffRepository.findById(idStaff).get();

            staff.setTimesheet(UtilLib.aggiornaProgettoCalendario(staff.getTimesheet(), progetto));
            staff.getProgetti().add(progetto);

            staffRepository.save(staff);

            model.addAttribute("progetto", progetto);
            model.addAttribute("titoloPagina", "Match del progetto");
            model.addAttribute("listStaffNonAssociati", staffRepository.findStaffNonAssociati(idProgetto));
            model.addAttribute("listStaffAssociati", staffRepository.findStaffAssociati(idProgetto));
            model.addAttribute("staffRicerca", new Staff());

            return "liste_match_progetto";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/rimuovi/{idProgetto}/{idStaff}")
    public String showRimuoviStaffForm(
        @PathVariable("idProgetto") Integer idProgetto,
        @PathVariable("idStaff") Integer idStaff,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Progetto progetto = progettoRepository.findById(idProgetto).get();
            Staff    staff    = staffRepository.findById(idStaff).get();

            staff.setProgetti(staff.getProgetti().stream().filter(p -> !p.getId().equals(idProgetto)).collect(Collectors.toList()));

            staff.setTimesheet(UtilLib.rimuoviProgettoCalendario(staff.getTimesheet(), idProgetto));

            staffRepository.save(staff);

            model.addAttribute("progetto", progetto);
            model.addAttribute("titoloPagina", "Match del progetto");
            model.addAttribute("listStaffNonAssociati", staffRepository.findStaffNonAssociati(idProgetto));
            model.addAttribute("listStaffAssociati", staffRepository.findStaffAssociati(idProgetto));
            model.addAttribute("staffRicerca", new Staff());

            return "liste_match_progetto";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }
}