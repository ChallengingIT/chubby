/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.EmailSenderService;
import it.innotek.wehub.entity.*;
import it.innotek.wehub.entity.staff.FileStaff;
import it.innotek.wehub.entity.staff.FileStaffId;
import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.entity.timesheet.*;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.ProgettoRepository;
import it.innotek.wehub.service.*;
import it.innotek.wehub.util.ExportExcel;
import it.innotek.wehub.util.UtilLib;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/hr")
public class HRController {

    @Autowired
    private StaffService              serviceStaff;
    @Autowired
    private SkillService              serviceSkill;
    @Autowired
    private FileService               serviceFile;
    @Autowired
    private FileStaffService          serviceFileStaff;
    @Autowired
    private ProgettoService           serviceProgetto;
    @Autowired
    private EmailSenderService        serviceEmail;
    @Autowired
    private MeseService               serviceMese;
    @Autowired
    private UserService               serviceUser;
    @Autowired
    private AuthorityService          serviceAuthority;
    @Autowired
    private LivelloService            serviceLivelli;
    @Autowired
    private TipologiaService          serviceTipologia;
    @Autowired
    private TipologiaContrattoService serviceTipologiaContratto;
    @Autowired
    private FacoltaService            serviceFacolta;
    @Autowired
    private CalendarioService         serviceCalendario;
    @Autowired
    private ProgettoRepository        progettoRepository;

    @RequestMapping
    public String getStaff(Model model){

        List<Staff>   listStaff = serviceStaff.listAll();
        LocalDate     local     = LocalDate.now();
        List<Integer> listId    = new ArrayList<>();

        for (Staff staff : listStaff) {
            listId.add(staff.getId());
        }

        serviceFileStaff.deleteFileVecchi(listId.toString(),1);
        serviceFileStaff.deleteFileVecchi(listId.toString(),2);


        listStaff = serviceStaff.listAll();

        model.addAttribute("listStaff",    listStaff);
        model.addAttribute("staffRicerca", new Staff());
        model.addAttribute("mese",         local.getMonthValue());
        model.addAttribute("anno",         local.getYear());

        if (null != model.getAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
        }
        return "staff";
    }

    @RequestMapping("/crea/utente")
    public String creaUtente(Model model){

        model.addAttribute("user",         new User());
        model.addAttribute("titoloPagina", "Aggiungi un nuovo user");

        return "user_form";
    }

    @RequestMapping("/salva/utente")
    public String creaUtente(
        Model model,
        User user
    ){
        user.setEnabled((byte) 1);
        user.getAuthority().setUsername(user.getUsername());

        Authority authority = user.getAuthority();

        user.setAuthority(null);

        BCryptPasswordEncoder encore = new BCryptPasswordEncoder();
        user.setPassword(encore.encode(user.getPassword()));

        serviceUser.save(user);
        serviceAuthority.save(authority);

        return "redirect:/hr";
    }

    @RequestMapping("/staff/ricerca")
    public String showRicercaCandidatiList(
        Model model,
        Staff staff
    ){
        String        nome      = ((null != staff.getNome())    && !staff.getNome().isEmpty())    ? staff.getNome()    : null;
        String        cognome   = ((null != staff.getCognome()) && !staff.getCognome().isEmpty()) ? staff.getCognome() : null;
        String        email     = ((null != staff.getEmail())   && !staff.getEmail().isEmpty())   ? staff.getEmail()   : null;
        LocalDate     local     = LocalDate.now();
        List<Staff>   listStaff = serviceStaff.listRicerca(nome, cognome, email);
        List<Integer> listId    = new ArrayList<>();

        for (Staff staffApp : listStaff) {
            listId.add(staffApp.getId());
        }

        serviceFileStaff.deleteFileVecchi(listId.toString(),1);
        serviceFileStaff.deleteFileVecchi(listId.toString(),2);

        listStaff = serviceStaff.listRicerca(nome,cognome,email);

        model.addAttribute("listStaff",    listStaff);
        model.addAttribute("staffRicerca", staff);
        model.addAttribute("mese",         local.getMonthValue());
        model.addAttribute("anno",         local.getYear());
        return "staff";
    }

    @RequestMapping("/staff/aggiungi")
    public String showNewForm(
        Model model,
        RedirectAttributes ra
    ){
        if (model.getAttribute("staff") != null) {
            model.addAttribute("staff", model.getAttribute("staff"));
        } else {
            model.addAttribute("staff", new Staff());
        }

        model.addAttribute("titoloPagina",            "Aggiungi Dipendente");
        model.addAttribute("listaSkillOrdinata",      serviceSkill.listAll());
        model.addAttribute("listaLivelliScolastici",  serviceLivelli.listAll());
        model.addAttribute("listaFacolta",            serviceFacolta.listAll());
        model.addAttribute("listaTipologie",          serviceTipologia.listAll());
        model.addAttribute("listaTipologieContratto", serviceTipologiaContratto.listAll());

        return "staff_form";
    }

    @RequestMapping("/staff/visualizza/{id}")
    public String showForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Staff staff = serviceStaff.get(id);

            model.addAttribute("staff",                   staff);
            model.addAttribute("titoloPagina",            "Modifica dipendente");
            model.addAttribute("listaSkillOrdinata",      serviceSkill.listAll());
            model.addAttribute("listaLivelliScolastici",  serviceLivelli.listAll());
            model.addAttribute("listaFacolta",            serviceFacolta.listAll());
            model.addAttribute("listaTipologie",          serviceTipologia.listAll());
            model.addAttribute("listaTipologieContratto", serviceTipologiaContratto.listAll());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/hr";
        }
        return "staff_visualizza_form";
    }

    @RequestMapping("/staff/salva")
    public String saveCandidato(
        @RequestParam("file") MultipartFile[] file,
        Staff staff,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        if (null != staff.getFacolta()) {
            if (null == staff.getFacolta().getId()) {
                staff.setFacolta(null);
            }
        }

        if (null != staff.getId()) {
            serviceStaff.save(staff);

            for (MultipartFile f : file) {
                if ((null != f.getOriginalFilename()) && !f.getOriginalFilename().isEmpty()) {
                    uploadFileVoid(f, staff.getId(), 3, ra);
                }
            }
        } else {
            Progetto          progetto  = new Progetto();
            TipologiaProgetto tipologia = new TipologiaProgetto();

            progetto.setDescription("Ferie, Permessi e Malattia");
            tipologia.setId(1);
            progetto.setTipologia(tipologia);

            serviceProgetto.save(progetto);

            Calendario calendario = creaCalendario(staff.getTimesheet(), progetto);

            staff.setTimesheet(calendario);
            staff.getProgetti().add(progetto);

            serviceStaff.save(staff);

            for (MultipartFile f : file) {

                if ((null != f.getOriginalFilename()) && !f.getOriginalFilename().isEmpty()) {
                    uploadFileVoid(f, staff.getId(), 3, ra);
                }
            }
        }
        ra.addFlashAttribute("message", "Il dipendente e' stato salvato con successo");
        return "redirect:/hr";
    }

    @RequestMapping("/staff/elimina/{id}")
    public String saveCandidato(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        serviceStaff.delete(id);

        ra.addFlashAttribute("message", "Il dipendente e' stato eliminato con successo");
        return "redirect:/hr";
    }

    @RequestMapping("/staff/sollecito")
    public String inviaSollecitoChiusuraTimesheet(
        Model model,
        RedirectAttributes ra
    ) throws MessagingException {

        Anno        anno;
        List<Staff> staffs      = serviceStaff.listAll();
        LocalDate   dataOdierna = LocalDate.now();
        Mese        mese        = null;
        
        for (Staff staff : staffs) {
            anno = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), dataOdierna.getYear());
            if (null != anno) {
                mese = UtilLib.prendiMese(anno.getMesi(), dataOdierna.getMonthValue());
            }

            if ((null != mese) && !mese.isInviato()) {

                Email email = getEmail(staff, mese, anno);

                serviceEmail.sendHtmlMessage(email);
            }
        }

        ra.addFlashAttribute("message","Sollecito inviato con successo");

        return "redirect:/hr";
    }

    @NotNull
    private static Email getEmail(Staff staff, Mese mese, Anno anno) {

        Email               email = new Email();
        Map<String, Object> mappa = new HashMap<>();

        email.setFrom("sviluppo@inno-tek.it");
        email.setTo(staff.getEmail());
        mappa.put("nome", staff.getNome());
        mappa.put("cognome", staff.getCognome());
        mappa.put("mese",UtilLib.meseItaliano(mese.getDescription()));
        mappa.put("anno", anno.getAnno());
        email.setProperties(mappa);
        email.setSubject("Ritardo invio timesheet di " + UtilLib.meseItaliano(mese.getDescription()) );
        email.setTemplate("ritardo-email.html");

        return email;
    }

    @RequestMapping("/staff/modifica/{id}")
    public String showEditForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Staff staff = serviceStaff.get(id);

            model.addAttribute("staff",                   staff);
            model.addAttribute("titoloPagina",            "Modifica Staff");
            model.addAttribute("listaSkillOrdinata",      serviceSkill.listAll());
            model.addAttribute("listaLivelliScolastici",  serviceLivelli.listAll());
            model.addAttribute("listaFacolta",            serviceFacolta.listAll());
            model.addAttribute("listaTipologie",          serviceTipologia.listAll());
            model.addAttribute("listaTipologieContratto", serviceTipologiaContratto.listAll());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/hr";
        }
        return "staff_form";
    }

    public void uploadFileVoid(MultipartFile file,Integer idStaff, Integer tipoFile, RedirectAttributes ra) {
        try {
            File        fileOggettoStaff = getFile(file, tipoFile);
            FileStaff   fileStaff        = new FileStaff();
            FileStaffId fileStaffId      = new FileStaffId();

            serviceFile.save(fileOggettoStaff);

            fileStaffId.setIdFile(fileOggettoStaff.getId());
            fileStaffId.setIdStaff(idStaff);
            fileStaff.setId(fileStaffId);

            serviceFileStaff.save(fileStaff);

        } catch (Exception e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
    }

    @NotNull
    private static File getFile(
        MultipartFile file,
        Integer tipoFile
    ) throws IOException {

        String         descrizione      = file.getOriginalFilename();
        byte[]         arrayByte        = file.getBytes();
        String         tipo             = file.getContentType();
        File           fileOggettoStaff = new File();
        java.util.Date date             = new java.util.Date();
        Date           sqlDate          = new Date(date.getTime());
        TipologiaF     tipologia        = new TipologiaF();

        fileOggettoStaff.setData(arrayByte);
        fileOggettoStaff.setDataInserimento(new Date(sqlDate.getTime()));
        fileOggettoStaff.setDescrizione(descrizione);
        fileOggettoStaff.setTipo(tipo);

        tipologia.setId(tipoFile);

        fileOggettoStaff.setTipologia(tipologia);

        return fileOggettoStaff;
    }

    @RequestMapping("/report")
    public String report(Model model){

        List<Integer> listaAnni = new ArrayList<>();

        for (int i = 2023; i<  2051; i++) {
            listaAnni.add(i);
        }

        model.addAttribute("ricercaReport", new RicercaReport());
        model.addAttribute("listaAnni",     listaAnni);
        model.addAttribute("cercato",       0);

        return "report";
    }

    @RequestMapping("/report/estrai")
    public String reportEstrai(
        Model model,
        RicercaReport ricerca
    ){
        List<Integer> listaAnni   = new ArrayList<>();
        Integer       fineMese    = UtilLib.calcolaFineMese(ricerca.getMese(), ricerca.getAnno());
        List<Report>  listaReport = new ArrayList<>();

        for (int i = 2023; i < 2051; i++) {
            listaAnni.add(i);
        }

        if (((null != ricerca.getGiornoInizio()) && ricerca.getGiornoInizio() > fineMese) || ((null != ricerca.getGiornoFine()) && ricerca.getGiornoFine() > fineMese)){

            model.addAttribute("message",       "Data non valida");
            model.addAttribute("listaReport",   listaReport);
            model.addAttribute("ricercaReport", ricerca);
            model.addAttribute("listaAnni",     listaAnni);
            model.addAttribute("cercato",       0);

            return "report";
        }

        listaReport = prendiListaReport(ricerca, fineMese);

        model.addAttribute("listaReport",   listaReport);
        model.addAttribute("ricercaReport", ricerca);
        model.addAttribute("listaAnni",     listaAnni);
        model.addAttribute("giornoInizio",  (null == ricerca.getGiornoInizio()) ? 1 : ricerca.getGiornoInizio());
        model.addAttribute("giornoFine",    (null == ricerca.getGiornoFine()) ? fineMese : ricerca.getGiornoFine());
        model.addAttribute("cercato",       1);

        return "report";
    }

    @RequestMapping("/report/excel/{anno}/{mese}/{giornoInizio}/{giornoFine}")
    public void reportExcel(
        Model model,
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        @PathVariable("giornoInizio") String giornoInizio,
        @PathVariable("giornoFine") String giornoFine,
        HttpServletResponse response
    ) throws IOException {

        List<Integer> listaAnni   = new ArrayList<>();
        Integer       fineMese    = UtilLib.calcolaFineMese(mese, anno);
        List<Report>  listaReport;
        RicercaReport ricerca     = new RicercaReport();

        ricerca.setAnno(anno);
        ricerca.setMese(mese);

        if ((null != giornoInizio) && !giornoInizio.equals("null")) {
            ricerca.setGiornoInizio(Integer.parseInt(giornoInizio));
        }
        if ((null != giornoFine) && !giornoFine.equals("null")) {
            ricerca.setGiornoFine(Integer.parseInt(giornoFine));
        }
        for (int i = 2023; i < 2051; i++) {
            listaAnni.add(i);
        }

        listaReport = prendiListaReport(ricerca, fineMese);

        ExportExcel.export(response, listaReport,mese,anno);
    }

    public List<Report> prendiListaReport(
        RicercaReport ricerca,
        Integer fineMese
    ){
        List<Staff>  listStaff   = serviceStaff.listByCalendario(ricerca.getAnno(), ricerca.getMese());
        List<Report> listaReport = new ArrayList<>();

        for (Staff staff : listStaff) {

            Mese mese =
                UtilLib.prendiMese(
                    UtilLib.prendiAnno(
                        staff.getTimesheet().getAnni(),
                        ricerca.getAnno()
                    ).getMesi(),
                    ricerca.getMese()
                );

            if ((null == ricerca.getGiornoInizio()) && (null == ricerca.getGiornoFine())) {
                Report report = new Report();

                report.setNome(staff.getNome() + " " + staff.getCognome());
                report.setGiorni(
                    new ArrayList<>(
                        UtilLib.unisciGiorniUguali(
                            mese.getDays(),
                            1,
                            fineMese)
                    )
                );

                listaReport.add(report);
            } else {
                Report report = new Report();
                report.setNome(staff.getNome() + " " + staff.getCognome());
                report.setGiorni(
                    new ArrayList<>(
                        UtilLib.unisciGiorniUguali(
                            UtilLib.prendiGiorni(
                                mese,
                                ricerca.getGiornoInizio(),
                                ricerca.getGiornoFine()
                            ),
                            (null == ricerca.getGiornoInizio()) ? 1 : ricerca.getGiornoInizio(),
                            (null == ricerca.getGiornoFine()) ? fineMese : ricerca.getGiornoFine()
                        )
                    )
                );
                listaReport.add(report);
            }
        }
        return listaReport;
    }

    public String controllaAggiornamento(AggiornaTimesheet aggiorna){

        String   errore                   = "";
        Progetto progetto                 = aggiorna.getProgetto();
        Integer  orePermesso              = ( null != aggiorna.getOrePermesso() ) ? aggiorna.getOrePermesso() : 0;
        Boolean  permesso                 = ( null != aggiorna.getPermesso() ) ? aggiorna.getPermesso() : false;
        Integer  ore                      = ( null != aggiorna.getOre() ) ? aggiorna.getOre() : 0;
        Integer  idTipologiaProgetto      = ( null != progetto && null != progetto.getTipologia() ) ? progetto.getTipologia().getId() : null;
        Boolean  ferie                    = ( null != aggiorna.getFerie() ) ? aggiorna.getFerie() : false;
        Boolean  malattia                 = ( null != aggiorna.getMalattia() ) ? aggiorna.getMalattia() : false;
        Integer  oreStraordinarie         = ( null != aggiorna.getOreStraordinarie() ) ? aggiorna.getOreStraordinarie() : 0;
        Integer  oreStraordinarieNotturne = ( null != aggiorna.getOreStraordinarieNotturne() ) ? aggiorna.getOreStraordinarieNotturne() : 0;

        if ((permesso && ferie && malattia) ||
            (permesso && ferie) ||
            (permesso && malattia) ||
            (ferie && malattia)
        ) {
            return "Piu' di una checkbox selezionata";
        }
        if (idTipologiaProgetto == 1 && !ferie && !permesso && !malattia && ore != 0) {
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
        if ((ore + orePermesso + oreStraordinarie + oreStraordinarieNotturne) > 16) {
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

    public boolean controlloTemporaleProgetto(
        LocalDate data,
        Progetto progetto
    ){
        return data.isBefore(progetto.getInizio()) || data.isAfter(progetto.getScadenza());
    }

    public Calendario creaCalendario(
        Calendario calendario,
        Progetto progetto
    ) throws ElementoNonTrovatoException {

        if (null == calendario) {

            Calendar          calendar  = Calendar.getInstance();
            TipologiaProgetto tipologia = new TipologiaProgetto();
            int               anno      = calendar.get(Calendar.YEAR);
            int               mese      = calendar.get(Calendar.MONTH) + 1;

            calendar.setTime(new java.util.Date());
            tipologia.setId(1);

            return UtilLib.creazioneCalendarioLib(anno, mese, 1, progetto);
        } else {
            return serviceCalendario.get(calendario.getId());
        }
    }
}