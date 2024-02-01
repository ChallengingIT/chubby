/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Cliente;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/progetti")
public class ProgettoController {

    @Autowired
    private ProgettoRepository progettoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private StaffRepository   staffRepository;


    private static final Logger logger = LoggerFactory.getLogger(ProgettoController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Progetto> getAll()
    {
        try {
            List<Progetto> listProgetti = new ArrayList<>();
            listProgetti = progettoRepository.findAll().stream().filter(p -> p.getTipologia().getId() != 1).toList();

            for (Progetto progetto : listProgetti) {
                if (( null != progetto.getTipologia() ) && progetto.getTipologia().getId() != 1) {
                    if (null != progetto.getIdStaff()) {
                        progetto.setDurataEffettiva(progettoRepository.getOreEffettive(progetto.getId(), progetto.getIdStaff()));

                        if (null == progetto.getDurataEffettiva()) {
                            progetto.setDurataEffettiva(0);
                        }

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

            return listProgetti;
        } catch (Exception e) {
            logger.error(e.toString());

            return null;
        }
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Progetto getById(@PathVariable("id") Integer id)
    {
        return progettoRepository.findById(id).get();
    }

    @RequestMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveProgetto(
        @RequestBody Map<String,String> progettoMap
    ) {
        logger.debug("progetti salva");

        try {

            Progetto progetto = new Progetto();

            if(progettoMap.get("id") != null) {
                progetto = progettoRepository.findById(Integer.parseInt(progettoMap.get("id"))).get();
            }

            trasformaMappaInProgetto(progetto, progettoMap);

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
            Integer idStaff = Integer.parseInt(progettoMap.get("idStaff"));
            if (null != progetto.getId()) {


                Progetto progettoVecchio = progettoRepository.findById(progetto.getId()).get();
                Staff    staff           = staffRepository.findByProgetto_Id(progetto.getId());

                if (null == staff) {
                    TipologiaProgetto tipologia = new TipologiaProgetto();
                    tipologia.setId(2);
                    progetto.setTipologia(tipologia);
                    //progettoRepository.save(progetto);

                    staff = staffRepository.findById(idStaff).get();

                    staff.setTimesheet(UtilLib.aggiornaProgettoCalendario(staff.getTimesheet(), progetto));
                    staff.getProgetti().add(progetto);
                    staffRepository.save(staff);
                } else if (staff.getId().equals(progetto.getIdStaff())) {

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
                    //progettoRepository.save(progetto);

                    staff.getProgetti().add(progetto);

                    staffRepository.save(staff);
                } else {
                    return "PROGETTO GIA' ASSOCIATO";
                }
            } else {

                TipologiaProgetto tipologia = new TipologiaProgetto();
                tipologia.setId(2);
                progetto.setTipologia(tipologia);
                //progettoRepository.save(progetto);

                Staff staff = staffRepository.findById(idStaff).get();

                staff.setTimesheet(UtilLib.aggiornaProgettoCalendario(staff.getTimesheet(), progetto));
                staff.getProgetti().add(progetto);
                staffRepository.save(staff);

            }
            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }


    @DeleteMapping("/react/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String deleteProgetto(
        @PathVariable("id") Integer id
    ){
        logger.debug("progetti elimina");

        try {

            Progetto progetto = progettoRepository.findById(id).get();

            Staff staff = staffRepository.findById(progetto.getIdStaff()).get();

            UtilLib.rimuoviProgettoCalendario(staff.getTimesheet(), id);

            staff.getProgetti().removeIf(p -> Objects.equals(p.getId(), id));

            staffRepository.save(staff);

            progettoRepository.deleteById(id);

            return "OK";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public void trasformaMappaInProgetto(Progetto need, Map<String,String> needMap) {

        need.setRate(needMap.get("rate") != null ? Integer.parseInt(needMap.get("rate")) : null);
        need.setCosto(needMap.get("costo") != null ? Integer.parseInt(needMap.get("costo")) : null);
        need.setNote(needMap.get("note") != null ? needMap.get("note") : null);;
        need.setMargine(needMap.get("margine") != null ? Integer.parseInt(needMap.get("margine")) : null);
        need.setMarginePerc(needMap.get("marginePerc") != null ? Integer.parseInt(needMap.get("marginePerc")) : null);
        need.setIdStaff(needMap.get("idStaff") != null ? Integer.parseInt(needMap.get("idStaff")) : null);
        need.setDescription(needMap.get("description") != null ? needMap.get("description") : null);

        if (needMap.get("idAzienda") != null) {
            Cliente cliente = new Cliente();
            cliente.setId(Integer.parseInt(needMap.get("idAzienda")));

            need.setCliente(cliente);
        }

        need.setMolTotale(needMap.get("molTotale") != null ? Integer.parseInt(needMap.get("molTotale")) : null);
        need.setValoreTotale(needMap.get("valoreTotale") != null ? Integer.parseInt(needMap.get("valoreTotale")) : null);
        need.setInizio(needMap.get("inizio") != null ? LocalDate.parse(needMap.get("inizio")) : null);
        need.setScadenza(needMap.get("scadenza") != null ? LocalDate.parse(needMap.get("scadenza")) : null);
        need.setDurata(needMap.get("durata") != null ? Integer.parseInt(needMap.get("durata")) : null);
        need.setDurataEffettiva(needMap.get("durataEffettiva") != null ? Integer.parseInt(needMap.get("durataEffettiva")) : null);
        need.setDurataStimata(needMap.get("durataStimata") != null ? Integer.parseInt(needMap.get("durataStimata")) : null);

        if (needMap.get("tipologia") != null) {
            TipologiaProgetto tipologia = new TipologiaProgetto();
            tipologia.setId(Integer.parseInt(needMap.get("tipologia")));

            need.setTipologia(tipologia);
        }
    }

    /*@RequestMapping("/rimuovi/{idProgetto}/{idStaff}")
    public String showRimuoviStaffForm(
        @PathVariable("idProgetto") Integer idProgetto,
        @PathVariable("idStaff") Integer idStaff,
        Model model,
        RedirectAttributes ra
    ){
        logger.debug("progetti rimuovi");

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
    }*/
}