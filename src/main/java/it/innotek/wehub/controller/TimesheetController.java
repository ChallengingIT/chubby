/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Timesheet;
import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.entity.timesheet.*;
import it.innotek.wehub.repository.MeseRepository;
import it.innotek.wehub.repository.ProgettoRepository;
import it.innotek.wehub.repository.StaffRepository;
import it.innotek.wehub.util.UtilLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/timesheet")
public class TimesheetController {

    @Autowired
    private StaffRepository    staffRepository;
    @Autowired
    private ProgettoRepository progettoRepository;
    @Autowired
    private MeseRepository     meseRepository;

    private static final Logger logger = LoggerFactory.getLogger(TimesheetController.class);

    @GetMapping("/user/{anno}/{mese}")
    //@PreAuthorize("hasRole('USER')")
    public Timesheet timesheetUser(
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        @RequestParam("username") String username
    ) throws Exception {
        logger.debug("timesheet user");

        try {

            Staff          staff    = staffRepository.findByUsername(username);
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
            List<Progetto> progettiStaff = progettoRepository.findByIdStaff(staff.getId());

            mese.setDays(giorni);

            for (Progetto progetto : progettiStaff) {

                if (UtilLib.progettoNelMese(mese, progetto.getId(), progetto.getTipologia().getId())) {
                    progetti.add(progetto);
                }
            }

            LocalDate dataInizio = LocalDate.of(anno.getAnno(), mese.getValue(), 1);

            LocalDate dataFine = LocalDate.of(anno.getAnno(), mese.getValue(), UtilLib.calcolaFineMese(mese.getValue(), anno.getAnno()));

            Timesheet timesheet = new Timesheet();

            timesheet.setAnnoCorrente(anno.getAnno());
            timesheet.setDataFine(dataFine);
            timesheet.setDataInizio(dataInizio);
            timesheet.setMese(mese);
            timesheet.setMeseCorrenteItaliano( UtilLib.meseItaliano(mese.getDescription()));
            timesheet.setMeseInviato(mese.isInviato());
            timesheet.setNumeroMese(mese.getValue());
            timesheet.setProgetti(progetti);
            timesheet.setNumeroProgetti(progetti.size());
            timesheet.setTotaleOre(UtilLib.contaOre(mese.getDays()));

            return timesheet;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            throw new Exception(exception);
        }
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/react/user/{anno}/{mese}")
    public Timesheet timesheet(
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        @RequestParam("username") String username
    )
        throws Exception {
        logger.debug("timesheet user anno mese");

        try {
            Staff          staff    = staffRepository.findByUsername(username);
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
            List<Progetto> progettiStaff = progettoRepository.findByIdStaff(staff.getId());

            mese.setDays(giorni);

            for (Progetto progetto : progettiStaff) {

                if (UtilLib.progettoNelMese(mese, progetto.getId(), progetto.getTipologia().getId())) {
                    progetti.add(progetto);
                }
            }

            LocalDate dataInizio = LocalDate.of(anno.getAnno(), mese.getValue(), 1);

            LocalDate dataFine = LocalDate.of(anno.getAnno(), mese.getValue(), UtilLib.calcolaFineMese(mese.getValue(), anno.getAnno()));


            Timesheet timesheet = new Timesheet();

            timesheet.setAnnoCorrente(anno.getAnno());
            timesheet.setDataFine(dataFine);
            timesheet.setDataInizio(dataInizio);
            timesheet.setMese(mese);
            timesheet.setMeseCorrenteItaliano( UtilLib.meseItaliano(mese.getDescription()));
            timesheet.setMeseInviato(mese.isInviato());
            timesheet.setNumeroMese(mese.getValue());
            timesheet.setProgetti(progetti);
            timesheet.setNumeroProgetti(progetti.size());
            timesheet.setTotaleOre(UtilLib.contaOre(mese.getDays()));

            return timesheet;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            throw new Exception(exception);
        }
    }

    @GetMapping("/react/user/primo")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String primoTimesheetUser(
        @RequestParam("username") String username
    ) {
        Staff staff = staffRepository.findByUsername(username);

        Anno anno = UtilLib.prendiPrimoAnno(staff.getTimesheet().getAnni());
        Mese mese = null;

        if(null != anno) {
            mese = UtilLib.prendiPrimoMese(anno.getMesi());
        }

        String primo = "";

        if (null != anno && null != mese) {
            primo = mese.getValue() + "-" + anno.getAnno();
        }

        return primo;
    }

    @GetMapping("/react/staff/primo/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String primoTimesheet(
        @PathVariable("id") Integer id
    ) {
        Staff staff = staffRepository.findById(id).get();

        Anno anno = UtilLib.prendiPrimoAnno(staff.getTimesheet().getAnni());
        Mese mese = null;

        if(null != anno) {
            mese = UtilLib.prendiPrimoMese(anno.getMesi());
        }

        String primo = "";

        if (null != anno && null != mese) {
            primo = mese.getValue() + "-" + anno.getAnno();
        }

        return primo;
    }

    @PostMapping("/react/user/cancella/{anno}/{mese}")
    //@PreAuthorize("hasRole('USER')")
    public String cancellaTimesheet(
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        @RequestParam("username") String username,
        @RequestBody Map<String,String> aggiornaMap
    ) {
        logger.debug("timesheet user cancella");

        try {
            Staff             staff     = staffRepository.findByUsername(username);
            AggiornaTimesheet aggiorna  = trasformaMappaInAggiorna(aggiornaMap);
            /*String            controllo = controllaAggiornamento(aggiorna);

            if (!controllo.equals("")) {
                return controllo;
            }*/
            if (null == aggiorna.getDataFinePeriodo()) {

                Anno   anno   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
                Mese   mese   = UtilLib.prendiMese(anno.getMesi(), aggiorna.getData().getMonthValue());
                Giorno giorno = UtilLib.prendiGiorno(mese.getDays(), aggiorna.getData().getDayOfMonth());

                if (null != giorno) {
                    staff.setTimesheet(UtilLib.aggiornaOreCalendario(staff.getTimesheet(), anno, mese, giorno, aggiorna));
                }
            } else {

                if (Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() > 1 || ( Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() == 1 && Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getDays() >= 1 )) {

                    return "Il range di date supera un mese";
                }

                Anno   annoInizio   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
                Mese   meseInizio   = UtilLib.prendiMese(annoInizio.getMesi(), aggiorna.getData().getMonthValue());
                Giorno giornoInizio = UtilLib.prendiGiorno(meseInizio.getDays(), aggiorna.getData().getDayOfMonth());
                Anno   annoFine     = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getDataFinePeriodo().getYear());

                if (null != giornoInizio) {

                    if (aggiorna.getData().getMonthValue() != aggiorna.getDataFinePeriodo().getMonthValue()) {

                        staff.setTimesheet(UtilLib.aggiornaCalendarioCompletaMese(staff.getTimesheet(), annoInizio, giornoInizio, aggiorna));

                        staff.setTimesheet(UtilLib.aggiornaCalendarioInizioMese(staff.getTimesheet(), annoFine, aggiorna));

                    } else {
                        staff.setTimesheet(UtilLib.aggiornaCalendario(staff.getTimesheet(), annoInizio, meseInizio, giornoInizio, aggiorna));
                    }
                }
            }

            staffRepository.save(staff);

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @PostMapping("/react/user/aggiorna/{anno}/{mese}")
    //@PreAuthorize("hasRole('USER')")
    public String aggiornaTimesheet(
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        @RequestParam("username") String username,
        @RequestBody Map<String,String> aggiornaMap
    ) {
        logger.debug("timesheet user aggiorna");

        try {
            Staff             staff     = staffRepository.findByUsername(username);
            AggiornaTimesheet aggiorna  = trasformaMappaInAggiorna(aggiornaMap);
            String            controllo = controllaAggiornamento(aggiorna);

            if (!controllo.equals("")) {
                return controllo;
            }
            if (null == aggiorna.getDataFinePeriodo()) {

                Anno   anno   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
                Mese   mese   = UtilLib.prendiMese(anno.getMesi(), aggiorna.getData().getMonthValue());
                Giorno giorno = UtilLib.prendiGiorno(mese.getDays(), aggiorna.getData().getDayOfMonth());

                if (null != giorno) {
                    staff.setTimesheet(UtilLib.aggiornaOreCalendario(staff.getTimesheet(), anno, mese, giorno, aggiorna));
                }
            } else {

                if (Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() > 1 || ( Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() == 1 && Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getDays() >= 1 )) {

                    return "Il range di date supera un mese";
                }

                Anno   annoInizio   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
                Mese   meseInizio   = UtilLib.prendiMese(annoInizio.getMesi(), aggiorna.getData().getMonthValue());
                Giorno giornoInizio = UtilLib.prendiGiorno(meseInizio.getDays(), aggiorna.getData().getDayOfMonth());
                Anno   annoFine     = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getDataFinePeriodo().getYear());

                if (null != giornoInizio) {

                    if (aggiorna.getData().getMonthValue() != aggiorna.getDataFinePeriodo().getMonthValue()) {

                        staff.setTimesheet(UtilLib.aggiornaCalendarioCompletaMese(staff.getTimesheet(), annoInizio, giornoInizio, aggiorna));

                        staff.setTimesheet(UtilLib.aggiornaCalendarioInizioMese(staff.getTimesheet(), annoFine, aggiorna));

                    } else {
                        staff.setTimesheet(UtilLib.aggiornaCalendario(staff.getTimesheet(), annoInizio, meseInizio, giornoInizio, aggiorna));
                    }
                }
            }

            staffRepository.save(staff);

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @GetMapping("/react/user/successivo/{anno}/{mese}")
    //@PreAuthorize("hasRole('USER')")
    public Timesheet getTimesheetSuccessivo(
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        @RequestParam("username") String username
    ) throws Exception {
        logger.debug("timesheet user successivo");

        try {
            Staff staff = staffRepository.findByUsername(username);
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
            List<Progetto> progettiStaff = progettoRepository.findByIdStaff(staff.getId());

            meseObj.setDays(giorni);

            for (Progetto progetto : progettiStaff) {
                if (UtilLib.progettoNelMese(meseObj, progetto.getId(), progetto.getTipologia().getId())) {
                    progetti.add(progetto);
                }
            }

            LocalDate dataInizio = LocalDate.of(annoObj.getAnno(), meseObj.getValue(), 1);

            LocalDate dataFine = LocalDate.of(annoObj.getAnno(), meseObj.getValue(), UtilLib.calcolaFineMese(meseObj.getValue(), annoObj.getAnno()));

            Timesheet timesheet = new Timesheet();

            timesheet.setAnnoCorrente(annoObj.getAnno());
            timesheet.setDataFine(dataFine);
            timesheet.setDataInizio(dataInizio);
            timesheet.setMese(meseObj);
            timesheet.setMeseCorrenteItaliano( UtilLib.meseItaliano(meseObj.getDescription()));
            timesheet.setMeseInviato(meseObj.isInviato());
            timesheet.setNumeroMese(meseObj.getValue());
            timesheet.setProgetti(progetti);
            timesheet.setNumeroProgetti(progetti.size());
            timesheet.setTotaleOre(UtilLib.contaOre(meseObj.getDays()));

            return timesheet;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            throw new Exception(exception);
        }
    }

    @GetMapping("/react/user/precedente/{anno}/{mese}")
    //@PreAuthorize("hasRole('USER')")
    public Timesheet getTimesheetPrecedente(
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        @RequestParam("username") String username
    ) throws Exception {
        logger.debug("timesheet user precedente");

        try {
            Staff staff = staffRepository.findByUsername(username);
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
            List<Progetto> progettiStaff = progettoRepository.findByIdStaff(staff.getId());

            meseObj.setDays(giorni);

            for (Progetto progetto : progettiStaff) {
                if (UtilLib.progettoNelMese(meseObj, progetto.getId(), progetto.getTipologia().getId())) {
                    progetti.add(progetto);
                }
            }

            LocalDate dataInizio = LocalDate.of(annoObj.getAnno(), meseObj.getValue(), 1);

            LocalDate dataFine = LocalDate.of(annoObj.getAnno(), meseObj.getValue(), UtilLib.calcolaFineMese(meseObj.getValue(), annoObj.getAnno()));

            Timesheet timesheet = new Timesheet();

            timesheet.setAnnoCorrente(annoObj.getAnno());
            timesheet.setDataFine(dataFine);
            timesheet.setDataInizio(dataInizio);
            timesheet.setMese(meseObj);
            timesheet.setMeseCorrenteItaliano( UtilLib.meseItaliano(meseObj.getDescription()));
            timesheet.setMeseInviato(meseObj.isInviato());
            timesheet.setNumeroMese(meseObj.getValue());
            timesheet.setProgetti(progetti);
            timesheet.setNumeroProgetti(progetti.size());
            timesheet.setTotaleOre(UtilLib.contaOre(meseObj.getDays()));

            return timesheet;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            throw new Exception(exception);
        }
    }

    @PostMapping("/react/user/salva/{anno}/{mese}")
    //@PreAuthorize("hasRole('USER')")
    public String salvaTimesheet(
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        @RequestParam("username") String username
    ) {
        logger.debug("timesheet user salva");

        try {
            Staff        staff   = staffRepository.findByUsername(username);
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
                    if (null != progetto && progetto.getTipologia().getId() == 1) {

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
                        return "Errore nel calcolo delle ore nei giorni di ferie";
                    }
                }

                if (null != progettoGiorno && progettoGiorno.getTipologia().getId() != 1) {

                    if (giorno.getOreTotali() != 0) {

                        Progetto progetto = progettoRepository.findById(progettoGiorno.getId()).get();

                        if (controlloTemporaleProgetto(giorno.getData(), progetto)) {
                            return "Errore caricamento ore in giorni non consentiti dal progetto";
                        }
                    }
                }
            }
            meseObj.setDays(giorni);
            meseObj.setInviato(true);

            meseRepository.save(meseObj);

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public String controllaAggiornamento(AggiornaTimesheet aggiorna){
        logger.debug("timesheet user controllaAggiornamento");

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

    @GetMapping("/react/staff/{id}/{anno}/{mese}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Timesheet getTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI
    )
        throws Exception {
        logger.debug("timesheet staff");

        try {
            Staff staff = staffRepository.findById(id).get();
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
            List<Progetto> progettiStaff = progettoRepository.findByIdStaff(id);

            mese.setDays(giorni);

            for (Progetto progetto : progettiStaff) {
                if (UtilLib.progettoNelMese(mese, progetto.getId(), progetto.getTipologia().getId())) {
                    progetti.add(progetto);
                }
            }

            LocalDate dataInizio = LocalDate.of(anno.getAnno(), mese.getValue(), 1);

            LocalDate dataFine = LocalDate.of(anno.getAnno(), mese.getValue(), UtilLib.calcolaFineMese(mese.getValue(), anno.getAnno()));

            Timesheet timesheet = new Timesheet();

            timesheet.setAnnoCorrente(anno.getAnno());
            timesheet.setDataFine(dataFine);
            timesheet.setDataInizio(dataInizio);
            timesheet.setMese(mese);
            timesheet.setMeseCorrenteItaliano( UtilLib.meseItaliano(mese.getDescription()));
            timesheet.setMeseInviato(mese.isInviato());
            timesheet.setNumeroMese(mese.getValue());
            timesheet.setProgetti(progetti);
            timesheet.setNumeroProgetti(progetti.size());
            timesheet.setTotaleOre(UtilLib.contaOre(mese.getDays()));

            return timesheet;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            throw new Exception(exception);
        }
    }

    @PostMapping("/react/staff/aggiorna/{id}/{anno}/{mese}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String aggiornaStaffTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        @RequestBody Map<String,String> aggiornaMap
    ) {
        logger.debug("timesheet staff aggiorna");

        try {
            Staff             staff     = staffRepository.findById(id).get();
            AggiornaTimesheet aggiorna  = trasformaMappaInAggiorna(aggiornaMap);
            String            controllo = controllaAggiornamento(aggiorna);

            if (!controllo.equals("")) {
                return controllo;
            }

            if (null == aggiorna.getDataFinePeriodo()) {

                Anno   anno   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
                Mese   mese   = UtilLib.prendiMese(anno.getMesi(), aggiorna.getData().getMonthValue());
                Giorno giorno = UtilLib.prendiGiorno(mese.getDays(), aggiorna.getData().getDayOfMonth());

                if (null != giorno) {
                    staff.setTimesheet(UtilLib.aggiornaOreCalendario(staff.getTimesheet(), anno, mese, giorno, aggiorna));
                }
            } else {
                if (Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() > 1 || ( Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() == 1 && Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getDays() >= 1 )) {

                    return "Il range di date supera un mese";
                }

                Anno   annoInizio   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
                Mese   meseInizio   = UtilLib.prendiMese(annoInizio.getMesi(), aggiorna.getData().getMonthValue());
                Giorno giornoInizio = UtilLib.prendiGiorno(meseInizio.getDays(), aggiorna.getData().getDayOfMonth());
                Anno   annoFine     = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getDataFinePeriodo().getYear());

                if (null != giornoInizio) {

                    if (aggiorna.getData().getMonthValue() != aggiorna.getDataFinePeriodo().getMonthValue()) {

                        staff.setTimesheet(UtilLib.aggiornaCalendarioCompletaMese(staff.getTimesheet(), annoInizio, giornoInizio, aggiorna));

                        staff.setTimesheet(UtilLib.aggiornaCalendarioInizioMese(staff.getTimesheet(), annoFine, aggiorna));

                    } else {
                        staff.setTimesheet(UtilLib.aggiornaCalendario(staff.getTimesheet(), annoInizio, meseInizio, giornoInizio, aggiorna));
                    }
                }
            }

            staffRepository.save(staff);

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @PostMapping("/react/staff/cancella/{id}/{anno}/{mese}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String cancellaStaffTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer annoI,
        @PathVariable("mese") Integer meseI,
        @RequestBody Map<String,String> aggiornaMap
    ) {
        logger.debug("timesheet staff aggiorna");

        try {
            Staff             staff     = staffRepository.findById(id).get();
            AggiornaTimesheet aggiorna  = trasformaMappaInAggiorna(aggiornaMap);
            /*String            controllo = controllaAggiornamento(aggiorna);

            if (!controllo.equals("")) {
                return controllo;
            }*/

            if (null == aggiorna.getDataFinePeriodo()) {

                Anno   anno   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
                Mese   mese   = UtilLib.prendiMese(anno.getMesi(), aggiorna.getData().getMonthValue());
                Giorno giorno = UtilLib.prendiGiorno(mese.getDays(), aggiorna.getData().getDayOfMonth());

                if (null != giorno) {
                    staff.setTimesheet(UtilLib.aggiornaOreCalendario(staff.getTimesheet(), anno, mese, giorno, aggiorna));
                }
            } else {
                if (Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() > 1 || ( Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getMonths() == 1 && Period.between(aggiorna.getData(), aggiorna.getDataFinePeriodo()).getDays() >= 1 )) {

                    return "Il range di date supera un mese";
                }

                Anno   annoInizio   = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getData().getYear());
                Mese   meseInizio   = UtilLib.prendiMese(annoInizio.getMesi(), aggiorna.getData().getMonthValue());
                Giorno giornoInizio = UtilLib.prendiGiorno(meseInizio.getDays(), aggiorna.getData().getDayOfMonth());
                Anno   annoFine     = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), aggiorna.getDataFinePeriodo().getYear());

                if (null != giornoInizio) {

                    if (aggiorna.getData().getMonthValue() != aggiorna.getDataFinePeriodo().getMonthValue()) {

                        staff.setTimesheet(UtilLib.aggiornaCalendarioCompletaMese(staff.getTimesheet(), annoInizio, giornoInizio, aggiorna));

                        staff.setTimesheet(UtilLib.aggiornaCalendarioInizioMese(staff.getTimesheet(), annoFine, aggiorna));

                    } else {
                        staff.setTimesheet(UtilLib.aggiornaCalendario(staff.getTimesheet(), annoInizio, meseInizio, giornoInizio, aggiorna));
                    }
                }
            }

            staffRepository.save(staff);

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @GetMapping("/react/staff/successivo/{id}/{anno}/{mese}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Timesheet getStaffTimesheetSuccessivo(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese
    )
        throws Exception {
        logger.debug("timesheet staff successivo");

        try {
            Staff staff = staffRepository.findById(id).get();
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
            List<Progetto> progettiStaff = progettoRepository.findByIdStaff(id);

            meseObj.setDays(giorni);

            for (Progetto progetto : progettiStaff) {

                if (UtilLib.progettoNelMese(meseObj, progetto.getId(), progetto.getTipologia().getId())) {
                    progetti.add(progetto);
                }
            }

            LocalDate dataInizio = LocalDate.of(annoObj.getAnno(), meseObj.getValue(), 1);

            LocalDate dataFine = LocalDate.of(annoObj.getAnno(), meseObj.getValue(), UtilLib.calcolaFineMese(meseObj.getValue(), annoObj.getAnno()));

            Timesheet timesheet = new Timesheet();

            timesheet.setAnnoCorrente(annoObj.getAnno());
            timesheet.setDataFine(dataFine);
            timesheet.setDataInizio(dataInizio);
            timesheet.setMese(meseObj);
            timesheet.setMeseCorrenteItaliano( UtilLib.meseItaliano(meseObj.getDescription()));
            timesheet.setMeseInviato(meseObj.isInviato());
            timesheet.setNumeroMese(meseObj.getValue());
            timesheet.setProgetti(progetti);
            timesheet.setNumeroProgetti(progetti.size());
            timesheet.setTotaleOre(UtilLib.contaOre(meseObj.getDays()));

            return timesheet;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            throw new Exception(exception);
        }
    }

    @GetMapping("/react/staff/precedente/{id}/{anno}/{mese}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Timesheet getStaffTimesheetPrecedente(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese
    )
        throws Exception {
        logger.debug("timesheet staff precedente");

        try {
            Staff staff   = staffRepository.findById(id).get();
            Anno  annoObj = null;
            Mese  meseObj = null;

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
            List<Progetto> progettiStaff = progettoRepository.findByIdStaff(id);

            meseObj.setDays(giorni);

            for (Progetto progetto : progettiStaff) {

                if (UtilLib.progettoNelMese(meseObj, progetto.getId(), progetto.getTipologia().getId())) {
                    progetti.add(progetto);
                }
            }

            LocalDate dataInizio = LocalDate.of(annoObj.getAnno(), meseObj.getValue(), 1);

            LocalDate dataFine = LocalDate.of(annoObj.getAnno(), meseObj.getValue(), UtilLib.calcolaFineMese(meseObj.getValue(), annoObj.getAnno()));

            Timesheet timesheet = new Timesheet();

            timesheet.setAnnoCorrente(annoObj.getAnno());
            timesheet.setDataFine(dataFine);
            timesheet.setDataInizio(dataInizio);
            timesheet.setMese(meseObj);
            timesheet.setMeseCorrenteItaliano( UtilLib.meseItaliano(meseObj.getDescription()));
            timesheet.setMeseInviato(meseObj.isInviato());
            timesheet.setNumeroMese(meseObj.getValue());
            timesheet.setProgetti(progetti);
            timesheet.setNumeroProgetti(progetti.size());
            timesheet.setTotaleOre(UtilLib.contaOre(meseObj.getDays()));

            return timesheet;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            throw new Exception(exception);
        }
    }

    @PostMapping("/react/staff/salva/{id}/{anno}/{mese}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String salvaStaffTimesheet(
        @PathVariable("id") Integer id,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese
    ) {
        logger.debug("timesheet staff salva");

        try {
            Staff        staff   = staffRepository.findById(id).get();
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

                    if (( null != progetto ) && progetto.getTipologia().getId() == 1) {
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
                        return "Errore nel calcolo delle ore nei giorni di ferie";
                    }
                }

                if (( null != progettoGiorno ) && progettoGiorno.getTipologia().getId() != 1) {
                    if (giorno.getOreTotali() != 0) {

                        Progetto progetto = progettoRepository.findById(progettoGiorno.getId()).get();

                        if (controlloTemporaleProgetto(giorno.getData(), progetto)) {
                            return "Errore caricamento ore in giorni non consentiti dal progetto";
                        }
                    }
                }
            }
            meseObj.setDays(giorni);
            meseObj.setInviato(true);

            meseRepository.save(meseObj);

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    public boolean controlloTemporaleProgetto(
        LocalDate data,
        Progetto progetto
    ){
        logger.debug("timesheet staff controlloTemporaleProgetto");

        return data.isBefore(progetto.getInizio()) || data.isAfter(progetto.getScadenza());
    }

    public AggiornaTimesheet trasformaMappaInAggiorna(Map<String,String> staffMap) {

        AggiornaTimesheet aggiornaTimesheet = new AggiornaTimesheet();

        aggiornaTimesheet.setFerie(staffMap.get("ferie") != null ? Boolean.valueOf(staffMap.get("ferie")) : null);
        aggiornaTimesheet.setMalattia(staffMap.get("malattia") != null ? Boolean.valueOf(staffMap.get("malattia")) : null);
        aggiornaTimesheet.setOre(staffMap.get("ore") != null ? Integer.parseInt(staffMap.get("ore")) : null);
        aggiornaTimesheet.setOrePermesso(staffMap.get("orePermesso") != null ? Integer.parseInt(staffMap.get("orePermesso")) : null);
        aggiornaTimesheet.setOreStraordinarie(staffMap.get("oreStraordinarie") != null ? Integer.parseInt(staffMap.get("oreStraordinarie")) : null);
        aggiornaTimesheet.setPermesso(staffMap.get("permesso") != null ? Boolean.valueOf(staffMap.get("permesso")) : null);;
        aggiornaTimesheet.setOreStraordinarieNotturne(staffMap.get("oreStraordinarieNotturne") != null ? Integer.parseInt(staffMap.get("oreStraordinarieNotturne")) : null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        aggiornaTimesheet.setData(staffMap.get("data") != null ? LocalDate.parse(staffMap.get("data"),formatter) : null);
        aggiornaTimesheet.setDataFinePeriodo(staffMap.get("dataFinePeriodo") != null ? LocalDate.parse(staffMap.get("dataFinePeriodo"),formatter) : null);

        if (staffMap.get("progetto") != null) {

            Progetto progetto = progettoRepository.findById(Integer.parseInt(staffMap.get("progetto"))).get();

            aggiornaTimesheet.setProgetto(progetto);
        }

        return aggiornaTimesheet;
    }
}