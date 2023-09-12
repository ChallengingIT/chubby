/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.entity.timesheet.*;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.MeseService;
import it.innotek.wehub.service.ProgettoService;
import it.innotek.wehub.service.StaffService;
import it.innotek.wehub.util.UtilLib;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/timesheet")
public class TimesheetController {

    @Autowired
    private StaffService    serviceStaff;
    @Autowired
    private ProgettoService serviceProgetto;
    @Autowired
    private MeseService     serviceMese;

    @PreAuthorize("hasRole(@roles.USER)")
    @RequestMapping("/user")
    public String timesheet(Model model) throws ElementoNonTrovatoException {

        Authentication auth     = SecurityContextHolder.getContext().getAuthentication();
        String         username = ((User)auth.getPrincipal()).getUsername();
        Staff          staff    = serviceStaff.getByUsername(username);
        LocalDate      local    = LocalDate.now();
        Anno           anno     = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), local.getYear());
        Mese           mese;

        if (null != anno) {
            mese = UtilLib.prendiMese(anno.getMesi(), local.getMonthValue());

            if (null == mese) {
                mese = UtilLib.ordinaMesi(anno.getMesi()).stream().findFirst().get();
            }

        } else {
            anno = UtilLib.ordinaAnni(staff.getTimesheet().getAnni()).stream().findFirst().get();
            mese = UtilLib.ordinaMesi(anno.getMesi()).stream().findFirst().get();
        }

        List<Giorno>   giorni        = UtilLib.ordinaGiorni(mese.getDays());
        List<Progetto> progetti      = new ArrayList<>();
        List<Progetto> progettiStaff = serviceProgetto.getByIdStaff(staff.getId());

        mese.setDays(giorni);

        for (Progetto progetto: progettiStaff) {

            if (
                UtilLib.progettoNelMese(
                    mese,
                    progetto.getId(),
                    progetto.getTipologia().getId()
                )
            ) {
                progetti.add(progetto);
            }
        }

        LocalDate dataInizio =
            LocalDate.of(
                anno.getAnno(),
                mese.getValue(),
                1
            );

        LocalDate dataFine =
            LocalDate.of(
                anno.getAnno(),
                mese.getValue(),
                UtilLib.calcolaFineMese(mese.getValue(),anno.getAnno())
            );

        model.addAttribute("mese",              mese);
        model.addAttribute("meseCorrente",      UtilLib.meseItaliano(mese.getDescription()));
        model.addAttribute("meseInviato",       mese.isInviato());
        model.addAttribute("dataInizio",        dataInizio);
        model.addAttribute("dataFine",          dataFine);
        model.addAttribute("numeroMese",        mese.getValue());
        model.addAttribute("annoCorrente",      anno.getAnno());
        model.addAttribute("aggiornaTimesheet", new AggiornaTimesheet());
        model.addAttribute("listaProgetti",     progetti);
        model.addAttribute("numeroProgetti",    progetti.size());
        model.addAttribute("idStaff",           staff.getId());
        model.addAttribute("totaleOre",         UtilLib.contaOre(mese.getDays()));

        if (null != model.getAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
        }
        return "timesheet_user";
    }

    @PreAuthorize("hasRole(@roles.USER)")
    @RequestMapping("/user/{anno}/{mese}")
    public String timesheet(
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        Model model
    ) throws ElementoNonTrovatoException {

        Authentication auth     = SecurityContextHolder.getContext().getAuthentication();
        String         username = ((User)auth.getPrincipal()).getUsername();
        Staff          staff    = serviceStaff.getByUsername(username);
        Anno           anno     = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), annoI);
        Mese           mese;

        if (null != anno) {
            mese = UtilLib.prendiMese(anno.getMesi(), meseI);

            if (null == mese) {
                mese = UtilLib.ordinaMesi(anno.getMesi()).stream().findFirst().get();
            }
        } else {
            anno = UtilLib.ordinaAnni(staff.getTimesheet().getAnni()).stream().findFirst().get();
            mese = UtilLib.ordinaMesi(anno.getMesi()).stream().findFirst().get();
        }

        List<Giorno>   giorni        = UtilLib.ordinaGiorni(mese.getDays());
        List<Progetto> progetti      = new ArrayList<>();
        List<Progetto> progettiStaff = serviceProgetto.getByIdStaff(staff.getId());

        mese.setDays(giorni);

        for (Progetto progetto: progettiStaff) {

            if (
                UtilLib.progettoNelMese(
                    mese,
                    progetto.getId(),
                    progetto.getTipologia().getId()
                )
            ) {
                progetti.add(progetto);
            }
        }

        LocalDate dataInizio =
            LocalDate.of(
                anno.getAnno(),
                mese.getValue(),
                1
            );

        LocalDate dataFine =
            LocalDate.of(
                anno.getAnno(),
                mese.getValue(),
                UtilLib.calcolaFineMese(mese.getValue(),anno.getAnno())
            );

        model.addAttribute("mese",              mese);
        model.addAttribute("meseCorrente",      UtilLib.meseItaliano(mese.getDescription()));
        model.addAttribute("meseInviato",       mese.isInviato());
        model.addAttribute("dataInizio",        dataInizio);
        model.addAttribute("dataFine",          dataFine);
        model.addAttribute("numeroMese",        mese.getValue());
        model.addAttribute("annoCorrente",      anno.getAnno());
        model.addAttribute("aggiornaTimesheet", new AggiornaTimesheet());
        model.addAttribute("listaProgetti",     progetti);
        model.addAttribute("numeroProgetti",    progetti.size());
        model.addAttribute("idStaff",           staff.getId());
        model.addAttribute("totaleOre",         UtilLib.contaOre(mese.getDays()));

        if (null != model.getAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
        }

        return "timesheet_user";
    }

    @RequestMapping("/user/aggiorna/{id}/{anno}/{mese}")
    public String aggiornaTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        AggiornaTimesheet aggiorna,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Staff  staff     = serviceStaff.get(id);
        String controllo = controllaAggiornamento(aggiorna);

        if (!controllo.equals("")) {
            ra.addFlashAttribute("message", controllo);
            return "redirect:/timesheet/user/"+annoI+"/"+meseI;
        }
        if (null == aggiorna.getDataFinePeriodo()) {

            Anno   anno   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
            Mese   mese   = UtilLib.prendiMese(anno.getMesi(), aggiorna.getData().getMonthValue());
            Giorno giorno = UtilLib.prendiGiorno(mese.getDays(), aggiorna.getData().getDayOfMonth());

            if (null != giorno) {
                staff.setTimesheet(UtilLib.aggiornaOreCalendario(staff.getTimesheet(), anno, mese, giorno, aggiorna));
            }
        } else {

            if (Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() > 1 ||
                    (Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() == 1 &&
                            Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getDays() >= 1)) {

                ra.addFlashAttribute("message", "Il range di date supera un mese");

                return "redirect:/timesheet/user/"+annoI+"/"+meseI;

            }

            Anno   annoInizio   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
            Mese   meseInizio   = UtilLib.prendiMese(annoInizio.getMesi(), aggiorna.getData().getMonthValue());
            Giorno giornoInizio = UtilLib.prendiGiorno(meseInizio.getDays(), aggiorna.getData().getDayOfMonth());
            Anno   annoFine     = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getDataFinePeriodo().getYear());

            if (null != giornoInizio) {

                if (aggiorna.getData().getMonthValue() != aggiorna.getDataFinePeriodo().getMonthValue()) {

                    staff.setTimesheet(
                        UtilLib.aggiornaCalendarioCompletaMese(
                            staff.getTimesheet(),
                            annoInizio,
                            giornoInizio,
                            aggiorna
                        )
                    );

                    staff.setTimesheet(
                        UtilLib.aggiornaCalendarioInizioMese(
                            staff.getTimesheet(),
                            annoFine,
                            aggiorna
                        )
                    );

                } else {
                    staff.setTimesheet(
                        UtilLib.aggiornaCalendario(
                            staff.getTimesheet(),
                            annoInizio,
                            meseInizio,
                            giornoInizio,
                            aggiorna
                        )
                    );
                }
            }
        }

        serviceStaff.save(staff);

        return "redirect:/timesheet/user/"+annoI+"/"+meseI;
    }

    @RequestMapping("/user/successivo/{id}/{anno}/{mese}")
    public String getTimesheetSuccessivo(@PathVariable("id") Integer id, @PathVariable("anno") Integer anno, @PathVariable("mese") Integer mese, Model model, RedirectAttributes ra) throws ElementoNonTrovatoException {

        Staff     staff   = serviceStaff.get(id);
        Anno      annoObj;
        Mese      meseObj;

        if (mese == 12) {
            annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno+1);
        } else {
            annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno);
        }

        if (null != annoObj) {
            meseObj = UtilLib.prendiMese(annoObj.getMesi(), mese + 1 == 13 ? 1 : mese + 1);

            if (null == meseObj) {
                meseObj = UtilLib.ordinaMesi(annoObj.getMesi()).stream().findFirst().get();
            }

        } else {
            annoObj = UtilLib.ordinaAnni(staff.getTimesheet().getAnni()).stream().findFirst().get();
            meseObj = UtilLib.ordinaMesi(annoObj.getMesi()).stream().findFirst().get();
        }

        List<Giorno>   giorni        = UtilLib.ordinaGiorni(meseObj.getDays());
        List<Progetto> progetti      = new ArrayList<>();
        List<Progetto> progettiStaff = serviceProgetto.getByIdStaff(staff.getId());

        meseObj.setDays(giorni);

        for (Progetto progetto: progettiStaff) {
            if (UtilLib.progettoNelMese(meseObj, progetto.getId(), progetto.getTipologia().getId())) {
                progetti.add(progetto);
            }
        }

        LocalDate dataInizio =
            LocalDate.of(
                annoObj.getAnno(),
                meseObj.getValue(),
                1
            );

        LocalDate dataFine =
            LocalDate.of(
                annoObj.getAnno(),
                meseObj.getValue(),
                UtilLib.calcolaFineMese(meseObj.getValue(),annoObj.getAnno())
            );

        model.addAttribute("mese",              meseObj);
        model.addAttribute("meseCorrente",      UtilLib.meseItaliano(meseObj.getDescription()));
        model.addAttribute("meseInviato",       meseObj.isInviato());
        model.addAttribute("dataInizio",        dataInizio);
        model.addAttribute("dataFine",          dataFine);
        model.addAttribute("numeroMese",        meseObj.getValue());
        model.addAttribute("annoCorrente",      annoObj.getAnno());
        model.addAttribute("aggiornaTimesheet", new AggiornaTimesheet());
        model.addAttribute("listaProgetti",     progetti);
        model.addAttribute("numeroProgetti",    progetti.size());
        model.addAttribute("idStaff",           id);
        model.addAttribute("totaleOre",         UtilLib.contaOre(meseObj.getDays()));

        return "timesheet_user";
    }

    @RequestMapping("/user/precedente/{id}/{anno}/{mese}")
    public String getTimesheetPrecedente(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Staff staff = serviceStaff.get(id);
        Anno  annoObj;
        Mese  meseObj;

        if (mese == 1) {
            annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno - 1);
        } else {
            annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno);
        }

        if (null != annoObj) {
            meseObj = UtilLib.prendiMese(annoObj.getMesi(), mese - 1 == 0 ? 12 : mese - 1);

            if (null == meseObj) {
                meseObj = UtilLib.ordinaMesi(annoObj.getMesi()).stream().findFirst().get();
            }
        } else {
            annoObj = UtilLib.ordinaAnni(staff.getTimesheet().getAnni()).stream().findFirst().get();
            meseObj = UtilLib.ordinaMesi(annoObj.getMesi()).stream().findFirst().get();
        }

        List<Giorno>   giorni        = UtilLib.ordinaGiorni(meseObj.getDays());
        List<Progetto> progetti      = new ArrayList<>();
        List<Progetto> progettiStaff = serviceProgetto.getByIdStaff(staff.getId());

        meseObj.setDays(giorni);

        for (Progetto progetto: progettiStaff) {
            if (UtilLib.progettoNelMese(meseObj, progetto.getId(), progetto.getTipologia().getId())) {
                progetti.add(progetto);
            }
        }

        LocalDate dataInizio =
            LocalDate.of(
                annoObj.getAnno(),
                meseObj.getValue(),
                1
            );

        LocalDate dataFine =
            LocalDate.of(
                annoObj.getAnno(),
                meseObj.getValue(),
                UtilLib.calcolaFineMese(meseObj.getValue(),annoObj.getAnno())
            );

        model.addAttribute("mese",              meseObj);
        model.addAttribute("meseCorrente",      UtilLib.meseItaliano(meseObj.getDescription()));
        model.addAttribute("numeroMese",        meseObj.getValue());
        model.addAttribute("dataInizio",        dataInizio);
        model.addAttribute("dataFine",          dataFine);
        model.addAttribute("meseInviato",       meseObj.isInviato());
        model.addAttribute("annoCorrente",      annoObj.getAnno());
        model.addAttribute("aggiornaTimesheet", new AggiornaTimesheet());
        model.addAttribute("listaProgetti",     progetti);
        model.addAttribute("numeroProgetti",    progetti.size());
        model.addAttribute("idStaff",           id);
        model.addAttribute("totaleOre",         UtilLib.contaOre(meseObj.getDays()));

        return "timesheet_user";
    }

    @RequestMapping("/user/salva/{id}/{anno}/{mese}")
    public String salvaTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Staff        staff   = serviceStaff.get(id);
        Anno         annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno);
        Mese         meseObj = UtilLib.prendiMese(annoObj.getMesi(), mese);
        List<Giorno> giorni  = UtilLib.ordinaGiorni(meseObj.getDays());

        for (Giorno giorno : giorni) {
            List<Giorno> giorniUguali   = UtilLib.prendiGiorniUguali(giorni, giorno.getGiorno());
            boolean      ferie          = false;
            boolean      malattia       = false;
            int          oreTotali      = 0;
            Progetto     progettoGiorno = giorno.getProgetto();

            for (Giorno g : giorniUguali) {
                Progetto progetto = g.getProgetto();
                if (null != progetto && progetto.getTipologia().getId()==1) {

                    if (g.isFerie()) {
                        ferie=true;
                    }

                    if (g.isMalattia()) {
                        malattia = true;
                    }

                } else {
                    oreTotali += g.getOreTotali();
                }
            }

            if (ferie || malattia) {
                if (oreTotali > 0) {
                    ra.addFlashAttribute("message","Errore nel calcolo delle ore nei giorni di ferie");
                    return "redirect:/timesheet/user/"+anno+"/"+mese;
                }
            }

            if (null != progettoGiorno && progettoGiorno.getTipologia().getId() != 1) {

                if (giorno.getOreTotali() != 0) {

                    Progetto progetto = serviceProgetto.get(progettoGiorno.getId());

                    if (controlloTemporaleProgetto(giorno.getData(), progetto)) {
                        ra.addFlashAttribute("message","Errore caricamento ore in giorni non consentiti dal progetto " + progetto.getDescription());
                        return "redirect:/timesheet/user/"+id+"/"+anno+"/"+mese;
                    }
                }
            }
        }
        meseObj.setDays(giorni);
        meseObj.setInviato(true);

        serviceMese.save(meseObj);

        return "redirect:/timesheet/user/"+anno+"/"+mese;
    }
    public String controllaAggiornamento(AggiornaTimesheet aggiorna){

        String  errore                   = "";
        Integer orePermesso              = (null != aggiorna.getOrePermesso()) ? aggiorna.getOrePermesso() : 0;
        Boolean permesso                 = (null != aggiorna.getPermesso()) ? aggiorna.getPermesso() : false;
        Integer ore                      = (null != aggiorna.getOre()) ? aggiorna.getOre() : 0;
        Integer idTipologiaProgetto      = (null != aggiorna.getProgetto() && null != aggiorna.getProgetto().getTipologia()) ? aggiorna.getProgetto().getTipologia().getId() : null;
        Boolean ferie                    = (null != aggiorna.getFerie()) ? aggiorna.getFerie() : false;
        Boolean malattia                 = (null != aggiorna.getMalattia()) ? aggiorna.getMalattia() : false;
        Integer oreStraordinarie         = (null != aggiorna.getOreStraordinarie()) ? aggiorna.getOreStraordinarie() : 0;
        Integer oreStraordinarieNotturne = (null != aggiorna.getOreStraordinarieNotturne()) ? aggiorna.getOreStraordinarieNotturne() : 0;

        if ((permesso && ferie && malattia) ||
            (permesso && ferie) ||
            (permesso && malattia) ||
            (ferie && malattia)
        ) {
            return "Piu' di una checkbox selezionata";
        }
        if (idTipologiaProgetto == 1 &&
            !ferie &&
            !permesso &&
            !malattia &&
            ore!=0
        ) {
            return "Selezionare almeno una checkbox";
        }
        if (permesso && orePermesso == 0) {
            return "Checkbox Permesso selezionata senza caricare le ore permesso";
        }
        if (permesso && orePermesso > 8) {
            return "Ore permesso maggiori dell'intera giornata";
        }
        if (ore > 0 && orePermesso > 0) {
            return "Se Permesso caricare solamente ore permesso e non ore ordinarie, in caso contrario solo ore ordinarie";
        }
        if (permesso && ore != 0) {
            return "Caricare ore permesso non ore ordinarie";
        }
        if ((ore+orePermesso + oreStraordinarie+oreStraordinarieNotturne) > 16) {
            return "Ore totali in eccesso";
        }
        if ((ferie || malattia) && (orePermesso > 0 || oreStraordinarie > 0 || oreStraordinarieNotturne > 0)) {
            return "Impossibile caricare ore non ordinarie in Ferie o Malattia";
        }
        if ((ferie || malattia) && ore != 8) {
            return "Caricare 8 ore per ferie o malattia";
        }

        return errore;
    }

    @RequestMapping("/staff/{id}/{anno}/{mese}")
    public String getTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Staff staff = serviceStaff.get(id);
        Anno  anno  = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), annoI);
        Mese  mese;

        if (null != anno) {
            mese = UtilLib.prendiMese(anno.getMesi(), meseI);

            if (null == mese) {
                mese = UtilLib.ordinaMesi(anno.getMesi()).stream().findFirst().get();
            }
        } else {
            anno = UtilLib.ordinaAnni(staff.getTimesheet().getAnni()).stream().findFirst().get();
            mese = UtilLib.ordinaMesi(anno.getMesi()).stream().findFirst().get();
        }

        List<Giorno>   giorni        = UtilLib.ordinaGiorni(mese.getDays());
        List<Progetto> progetti      = new ArrayList<>();
        List<Progetto> progettiStaff = serviceProgetto.getByIdStaff(id);

        mese.setDays(giorni);

        for (Progetto progetto: progettiStaff) {
            if (UtilLib.progettoNelMese(mese, progetto.getId(), progetto.getTipologia().getId())) {
                progetti.add(progetto);
            }
        }

        LocalDate dataInizio =
            LocalDate.of(
                anno.getAnno(),
                mese.getValue(),
                1
            );

        LocalDate dataFine =
            LocalDate.of(
                anno.getAnno(),
                mese.getValue(),
                UtilLib.calcolaFineMese(mese.getValue(),anno.getAnno())
            );

        model.addAttribute("mese",              mese);
        model.addAttribute("meseCorrente",      UtilLib.meseItaliano(mese.getDescription()));
        model.addAttribute("meseInviato",       mese.isInviato());
        model.addAttribute("dataInizio",        dataInizio);
        model.addAttribute("dataFine",          dataFine);
        model.addAttribute("numeroMese",        mese.getValue());
        model.addAttribute("annoCorrente",      anno.getAnno());
        model.addAttribute("aggiornaTimesheet", new AggiornaTimesheet());
        model.addAttribute("listaProgetti",     progetti);
        model.addAttribute("numeroProgetti",    progetti.size());
        model.addAttribute("idStaff",           id);
        model.addAttribute("totaleOre",         UtilLib.contaOre(mese.getDays()));

        if (null != model.getAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
        }

        return "timesheet";
    }

    @RequestMapping("/staff/aggiorna/{id}/{anno}/{mese}")
    public String aggiornaStaffTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        AggiornaTimesheet aggiorna,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Staff  staff     = serviceStaff.get(id);
        String controllo = controllaAggiornamento(aggiorna);

        if (!controllo.equals("")) {
            ra.addFlashAttribute("message", controllo);
            return "redirect:/timesheet/staff/"+id+"/"+annoI+"/"+meseI;
        }

        if (null == aggiorna.getDataFinePeriodo()) {

            Anno   anno   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
            Mese   mese   = UtilLib.prendiMese(anno.getMesi(), aggiorna.getData().getMonthValue());
            Giorno giorno = UtilLib.prendiGiorno(mese.getDays(), aggiorna.getData().getDayOfMonth());

            if (null != giorno) {
                staff.setTimesheet(UtilLib.aggiornaOreCalendario(staff.getTimesheet(), anno, mese, giorno, aggiorna));
            }
        } else {
            if (Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() > 1 ||
                (Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() == 1 &&
                    Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getDays() >= 1)) {

                ra.addFlashAttribute("message", "Il range di date supera un mese");

                return "redirect:/timesheet/staff/"+id+"/"+annoI+"/"+meseI;

            }

            Anno   annoInizio   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
            Mese   meseInizio   = UtilLib.prendiMese(annoInizio.getMesi(), aggiorna.getData().getMonthValue());
            Giorno giornoInizio = UtilLib.prendiGiorno(meseInizio.getDays(), aggiorna.getData().getDayOfMonth());
            Anno   annoFine     = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getDataFinePeriodo().getYear());

            if (null != giornoInizio) {

                if (aggiorna.getData().getMonthValue() != aggiorna.getDataFinePeriodo().getMonthValue()) {

                    staff.setTimesheet(
                        UtilLib.aggiornaCalendarioCompletaMese(
                            staff.getTimesheet(),
                            annoInizio,
                            giornoInizio,
                            aggiorna
                        )
                    );

                    staff.setTimesheet(
                        UtilLib.aggiornaCalendarioInizioMese(
                            staff.getTimesheet(),
                            annoFine,
                            aggiorna
                        )
                    );

                } else {
                    staff.setTimesheet(
                        UtilLib.aggiornaCalendario(
                            staff.getTimesheet(),
                            annoInizio,
                            meseInizio,
                            giornoInizio,
                            aggiorna
                        )
                    );
                }
            }
        }

        serviceStaff.save(staff);

        return "redirect:/timesheet/staff/"+id+"/"+annoI+"/"+meseI;
    }

    @RequestMapping("/staff/successivo/{id}/{anno}/{mese}")
    public String getStaffTimesheetSuccessivo(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Staff staff   = serviceStaff.get(id);
        Anno  annoObj;
        Mese  meseObj;

        if (mese == 12) {
            annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno + 1);
        } else {
            annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno);
        }

        if (null != annoObj) {
            meseObj = UtilLib.prendiMese(annoObj.getMesi(), mese + 1 == 13 ? 1 : mese + 1);

            if (null == meseObj) {
                meseObj = UtilLib.ordinaMesi(annoObj.getMesi()).stream().findFirst().get();
            }
        } else {
            annoObj = UtilLib.ordinaAnni(staff.getTimesheet().getAnni()).stream().findFirst().get();
            meseObj = UtilLib.ordinaMesi(annoObj.getMesi()).stream().findFirst().get();
        }

        List<Giorno>   giorni        = UtilLib.ordinaGiorni(meseObj.getDays());
        List<Progetto> progetti      = new ArrayList<>();
        List<Progetto> progettiStaff = serviceProgetto.getByIdStaff(id);

        meseObj.setDays(giorni);

        for (Progetto progetto: progettiStaff) {

            if (UtilLib.progettoNelMese(meseObj, progetto.getId(), progetto.getTipologia().getId())) {
                progetti.add(progetto);
            }
        }

        LocalDate dataInizio =
            LocalDate.of(
                annoObj.getAnno(),
                meseObj.getValue(),
                1
            );

        LocalDate dataFine =
            LocalDate.of(
                annoObj.getAnno(),
                meseObj.getValue(),
                UtilLib.calcolaFineMese(meseObj.getValue(),annoObj.getAnno())
            );

        model.addAttribute("mese",              meseObj);
        model.addAttribute("meseCorrente",      UtilLib.meseItaliano(meseObj.getDescription()));
        model.addAttribute("meseInviato",       meseObj.isInviato());
        model.addAttribute("dataInizio",        dataInizio);
        model.addAttribute("dataFine",          dataFine);
        model.addAttribute("numeroMese",        meseObj.getValue());
        model.addAttribute("annoCorrente",      annoObj.getAnno());
        model.addAttribute("aggiornaTimesheet", new AggiornaTimesheet());
        model.addAttribute("listaProgetti",     progetti);
        model.addAttribute("numeroProgetti",    progetti.size());
        model.addAttribute("idStaff",           id);
        model.addAttribute("totaleOre",         UtilLib.contaOre(meseObj.getDays()));

        return "timesheet";
    }

    @RequestMapping("/staff/precedente/{id}/{anno}/{mese}")
    public String getStaffTimesheetPrecedente(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Staff staff = serviceStaff.get(id);
        Anno annoObj = null;
        Mese meseObj = null;

        if (mese == 1) {
            annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno - 1);
        } else {
            annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno);
        }

        if (null != annoObj) {
            meseObj = UtilLib.prendiMese(annoObj.getMesi(), mese - 1 == 0 ? 12 : mese - 1);

            if (null == meseObj) {
                meseObj = UtilLib.ordinaMesi(annoObj.getMesi()).stream().findFirst().get();
            }
        } else {
            annoObj = UtilLib.ordinaAnni(staff.getTimesheet().getAnni()).stream().findFirst().get();
            meseObj = UtilLib.ordinaMesi(annoObj.getMesi()).stream().findFirst().get();
        }

        List<Giorno>   giorni        = UtilLib.ordinaGiorni(meseObj.getDays());
        List<Progetto> progetti      = new ArrayList<>();
        List<Progetto> progettiStaff = serviceProgetto.getByIdStaff(id);

        meseObj.setDays(giorni);

        for (Progetto progetto: progettiStaff) {

            if (UtilLib.progettoNelMese(meseObj, progetto.getId(), progetto.getTipologia().getId())) {
                progetti.add(progetto);
            }
        }

        LocalDate dataInizio =
            LocalDate.of(
                annoObj.getAnno(),
                meseObj.getValue(),
                1
            );

        LocalDate dataFine =
            LocalDate.of(
                annoObj.getAnno(),
                meseObj.getValue(),
                UtilLib.calcolaFineMese(meseObj.getValue(),annoObj.getAnno())
            );

        model.addAttribute("mese",              meseObj);
        model.addAttribute("meseCorrente",      UtilLib.meseItaliano(meseObj.getDescription()));
        model.addAttribute("numeroMese",        meseObj.getValue());
        model.addAttribute("dataInizio",        dataInizio);
        model.addAttribute("dataFine",          dataFine);
        model.addAttribute("meseInviato",       meseObj.isInviato());
        model.addAttribute("annoCorrente",      annoObj.getAnno());
        model.addAttribute("aggiornaTimesheet", new AggiornaTimesheet());
        model.addAttribute("listaProgetti",     progetti);
        model.addAttribute("numeroProgetti",    progetti.size());
        model.addAttribute("idStaff",           id);
        model.addAttribute("totaleOre",         UtilLib.contaOre(meseObj.getDays()));

        return "timesheet";
    }

    @RequestMapping("/staff/salva/{id}/{anno}/{mese}")
    public String salvaStaffTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException, MessagingException {

        Staff        staff   = serviceStaff.get(id);
        Anno         annoObj = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), anno);
        Mese         meseObj = UtilLib.prendiMese(annoObj.getMesi(), mese);
        List<Giorno> giorni  = UtilLib.ordinaGiorni(meseObj.getDays());

        for (Giorno giorno : giorni) {
            List<Giorno> giorniUguali   = UtilLib.prendiGiorniUguali(giorni, giorno.getGiorno());
            boolean      ferie          = false;
            boolean      malattia       = false;
            int          oreTotali      = 0;
            Progetto     progettoGiorno = giorno.getProgetto();

            for (Giorno g : giorniUguali) {

                Progetto progetto = g.getProgetto();

                if ((null != progetto) && progetto.getTipologia().getId() == 1) {
                    if (g.isFerie()) {
                        ferie = true;
                    }
                    if (g.isMalattia()) {
                        malattia = true;
                    }
                } else {
                    oreTotali += g.getOreTotali();
                }
            }

            if (ferie || malattia) {
                if (oreTotali > 0) {
                    ra.addFlashAttribute("message","Errore nel calcolo delle ore nei giorni di ferie");
                    return "redirect:/timesheet/staff/"+id+"/"+anno+"/"+mese;
                }
            }

            if ((null != progettoGiorno) && progettoGiorno.getTipologia().getId() != 1) {
                if (giorno.getOreTotali() != 0) {

                    Progetto progetto = serviceProgetto.get(progettoGiorno.getId());

                    if (controlloTemporaleProgetto(giorno.getData(), progetto)) {
                        ra.addFlashAttribute("message","Errore caricamento ore in giorni non consentiti dal progetto " + progetto.getDescription());
                        return "redirect:/timesheet/staff/"+id+"/"+anno+"/"+mese;
                    }
                }
            }
        }
        meseObj.setDays(giorni);
        meseObj.setInviato(true);

        serviceMese.save(meseObj);

        return "redirect:/timesheet/staff/"+id+"/"+anno+"/"+mese;
    }

    public boolean controlloTemporaleProgetto(
        LocalDate data,
        Progetto progetto
    ){

        return data.isBefore(progetto.getInizio()) || data.isAfter(progetto.getScadenza());
    }
}