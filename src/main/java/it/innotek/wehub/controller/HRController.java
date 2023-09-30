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
import it.innotek.wehub.repository.*;
import it.innotek.wehub.util.ExportExcel;
import it.innotek.wehub.util.UtilLib;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private StaffRepository              staffRepository;
    @Autowired
    private SkillRepository              skillRepository;
    @Autowired
    private FileRepository               fileRepository;
    @Autowired
    private FileStaffRepository          fileStaffRepository;
    @Autowired
    private ProgettoRepository           progettoRepository;
    @Autowired
    private EmailSenderService           serviceEmail;
    @Autowired
    private MeseRepository               meseRepository;
    @Autowired
    private UserRepository               userRepository;
    @Autowired
    private AuthorityRepository          authorityRepository;
    @Autowired
    private LivelloRepository            livelloRepository;
    @Autowired
    private TipologiaRepository          tipologiaRepository;
    @Autowired
    private TipologiaContrattoRepository tipologiaContrattoRepository;
    @Autowired
    private FacoltaRepository            facoltaRepository;
    @Autowired
    private CalendarioRepository         calendarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(HRController.class);

    @RequestMapping
    public String getStaff(Model model){
        try {
            List<Staff>   listStaff = staffRepository.findAll();
            LocalDate     local     = LocalDate.now();
            List<Integer> listId    = new ArrayList<>();

            for (Staff staff : listStaff) {
                listId.add(staff.getId());
            }

            fileStaffRepository.elimina_file_vecchi_staff(listId.toString(), 1);
            fileStaffRepository.elimina_file_vecchi_staff(listId.toString(), 2);

            listStaff = staffRepository.findAll();

            model.addAttribute("listStaff", listStaff);
            model.addAttribute("staffRicerca", new Staff());
            model.addAttribute("mese", local.getMonthValue());
            model.addAttribute("anno", local.getYear());

            if (null != model.getAttribute("message")) {
                model.addAttribute("message", model.getAttribute("message"));
            }
            return "staff";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/crea/utente")
    public String creaUtente(Model model){
        try {
            model.addAttribute("user", new User());
            model.addAttribute("titoloPagina", "Aggiungi un nuovo user");

            return "user_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva/utente")
    public String creaUtente(
        Model model,
        User user
    ){
        try {
            user.setEnabled((byte)1);
            user.getAuthority().setUsername(user.getUsername());

            Authority authority = user.getAuthority();

            user.setAuthority(null);

            BCryptPasswordEncoder encore = new BCryptPasswordEncoder();
            user.setPassword(encore.encode(user.getPassword()));

            userRepository.save(user);
            authorityRepository.save(authority);

            return "redirect:/hr";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/staff/ricerca")
    public String showRicercaCandidatiList(
        Model model,
        Staff staff
    ){
        try {
            String        nome      = ( ( null != staff.getNome() ) && !staff.getNome().isEmpty() ) ? staff.getNome() : null;
            String        cognome   = ( ( null != staff.getCognome() ) && !staff.getCognome().isEmpty() ) ? staff.getCognome() : null;
            String        email     = ( ( null != staff.getEmail() ) && !staff.getEmail().isEmpty() ) ? staff.getEmail() : null;
            LocalDate     local     = LocalDate.now();
            List<Staff>   listStaff = staffRepository.ricercaByNomeAndCognomeAndEmail(nome, cognome, email);
            List<Integer> listId    = new ArrayList<>();

            for (Staff staffApp : listStaff) {
                listId.add(staffApp.getId());
            }

            fileStaffRepository.elimina_file_vecchi_staff(listId.toString(), 1);
            fileStaffRepository.elimina_file_vecchi_staff(listId.toString(), 2);

            listStaff = staffRepository.ricercaByNomeAndCognomeAndEmail(nome, cognome, email);

            model.addAttribute("listStaff", listStaff);
            model.addAttribute("staffRicerca", staff);
            model.addAttribute("mese", local.getMonthValue());
            model.addAttribute("anno", local.getYear());
            return "staff";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/staff/aggiungi")
    public String showNewForm(
        Model model,
        RedirectAttributes ra
    ){
        try {
            if (model.getAttribute("staff") != null) {
                model.addAttribute("staff", model.getAttribute("staff"));
            } else {
                model.addAttribute("staff", new Staff());
            }

            model.addAttribute("titoloPagina", "Aggiungi Dipendente");
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipologieContratto", tipologiaContrattoRepository.findAll());

            return "staff_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/staff/visualizza/{id}")
    public String showForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Staff staff = staffRepository.findById(id).get();

            model.addAttribute("staff", staff);
            model.addAttribute("titoloPagina", "Modifica dipendente");
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipologieContratto", tipologiaContrattoRepository.findAll());

            return "staff_visualizza_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/staff/salva")
    public String saveCandidato(
        @RequestParam("file") MultipartFile[] file,
        Staff staff,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            if (null != staff.getFacolta()) {
                if (null == staff.getFacolta().getId()) {
                    staff.setFacolta(null);
                }
            }

            if (null != staff.getId()) {
                staffRepository.save(staff);

                for (MultipartFile f : file) {
                    if (( null != f.getOriginalFilename() ) && !f.getOriginalFilename().isEmpty()) {
                        uploadFileVoid(f, staff.getId(), 3, ra);
                    }
                }
            } else {
                Progetto          progetto  = new Progetto();
                TipologiaProgetto tipologia = new TipologiaProgetto();

                progetto.setDescription("Ferie, Permessi e Malattia");
                tipologia.setId(1);
                progetto.setTipologia(tipologia);

                progettoRepository.save(progetto);

                Calendario calendario = creaCalendario(staff.getTimesheet(), progetto);

                staff.setTimesheet(calendario);
                staff.getProgetti().add(progetto);

                staffRepository.save(staff);

                for (MultipartFile f : file) {

                    if (( null != f.getOriginalFilename() ) && !f.getOriginalFilename().isEmpty()) {
                        uploadFileVoid(f, staff.getId(), 3, ra);
                    }
                }
            }
            ra.addFlashAttribute("message", "Il dipendente e' stato salvato con successo");
            return "redirect:/hr";
            
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/staff/elimina/{id}")
    public String saveCandidato(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            staffRepository.deleteById(id);

            ra.addFlashAttribute("message", "Il dipendente e' stato eliminato con successo");
            return "redirect:/hr";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/staff/sollecito")
    public String inviaSollecitoChiusuraTimesheet(
        Model model,
        RedirectAttributes ra
    ) throws MessagingException {
        try {
            Anno        anno;
            List<Staff> staffs      = staffRepository.findAll();
            LocalDate   dataOdierna = LocalDate.now();
            Mese        mese        = null;

            for (Staff staff : staffs) {
                anno = UtilLib.prendiAnno(staff.getTimesheet().getAnni(), dataOdierna.getYear());
                if (null != anno) {
                    mese = UtilLib.prendiMese(anno.getMesi(), dataOdierna.getMonthValue());
                }

                if (( null != mese ) && !mese.isInviato()) {

                    Email email = getEmail(staff, mese, anno);

                    serviceEmail.sendHtmlMessage(email);
                }
            }

            ra.addFlashAttribute("message", "Sollecito inviato con successo");

            return "redirect:/hr";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
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
            Staff staff = staffRepository.findById(id).get();

            model.addAttribute("staff", staff);
            model.addAttribute("titoloPagina", "Modifica Staff");
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipologieContratto", tipologiaContrattoRepository.findAll());

            return "staff_form";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    public void uploadFileVoid(MultipartFile file,Integer idStaff, Integer tipoFile, RedirectAttributes ra) {
        try {
            File        fileOggettoStaff = getFile(file, tipoFile);
            FileStaff   fileStaff        = new FileStaff();
            FileStaffId fileStaffId      = new FileStaffId();

            fileRepository.save(fileOggettoStaff);

            fileStaffId.setIdFile(fileOggettoStaff.getId());
            fileStaffId.setIdStaff(idStaff);
            fileStaff.setId(fileStaffId);

            fileStaffRepository.save(fileStaff);

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
        try {
            List<Integer> listaAnni = new ArrayList<>();

            for (int i = 2023; i < 2051; i++) {
                listaAnni.add(i);
            }

            model.addAttribute("ricercaReport", new RicercaReport());
            model.addAttribute("listaAnni", listaAnni);
            model.addAttribute("cercato", 0);

            return "report";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/report/estrai")
    public String reportEstrai(
        Model model,
        RicercaReport ricerca
    ){
        try {
            List<Integer> listaAnni   = new ArrayList<>();
            Integer       fineMese    = UtilLib.calcolaFineMese(ricerca.getMese(), ricerca.getAnno());
            List<Report>  listaReport = new ArrayList<>();

            for (int i = 2023; i < 2051; i++) {
                listaAnni.add(i);
            }

            if (( ( null != ricerca.getGiornoInizio() ) && ricerca.getGiornoInizio() > fineMese ) || ( ( null != ricerca.getGiornoFine() ) && ricerca.getGiornoFine() > fineMese )) {

                model.addAttribute("message", "Data non valida");
                model.addAttribute("listaReport", listaReport);
                model.addAttribute("ricercaReport", ricerca);
                model.addAttribute("listaAnni", listaAnni);
                model.addAttribute("cercato", 0);

                return "report";
            }

            listaReport = prendiListaReport(ricerca, fineMese);

            model.addAttribute("listaReport", listaReport);
            model.addAttribute("ricercaReport", ricerca);
            model.addAttribute("listaAnni", listaAnni);
            model.addAttribute("giornoInizio", ( null == ricerca.getGiornoInizio() ) ? 1 : ricerca.getGiornoInizio());
            model.addAttribute("giornoFine", ( null == ricerca.getGiornoFine() ) ? fineMese : ricerca.getGiornoFine());
            model.addAttribute("cercato", 1);

            return "report";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
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
        try {
            List<Integer> listaAnni = new ArrayList<>();
            Integer       fineMese  = UtilLib.calcolaFineMese(mese, anno);
            List<Report>  listaReport;
            RicercaReport ricerca   = new RicercaReport();

            ricerca.setAnno(anno);
            ricerca.setMese(mese);

            if (( null != giornoInizio ) && !giornoInizio.equals("null")) {
                ricerca.setGiornoInizio(Integer.parseInt(giornoInizio));
            }
            if (( null != giornoFine ) && !giornoFine.equals("null")) {
                ricerca.setGiornoFine(Integer.parseInt(giornoFine));
            }
            for (int i = 2023; i < 2051; i++) {
                listaAnni.add(i);
            }

            listaReport = prendiListaReport(ricerca, fineMese);

            ExportExcel.export(response, listaReport, mese, anno);

        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public List<Report> prendiListaReport(
        RicercaReport ricerca,
        Integer fineMese
    ){
        List<Staff>  listStaff   = staffRepository.findByAnnoAndMese(ricerca.getAnno(), ricerca.getMese());
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

    public Calendario creaCalendario(
        Calendario calendario,
        Progetto progetto
    ) {

        if (null == calendario) {

            Calendar          calendar  = Calendar.getInstance();
            TipologiaProgetto tipologia = new TipologiaProgetto();
            int               anno      = calendar.get(Calendar.YEAR);
            int               mese      = calendar.get(Calendar.MONTH) + 1;

            calendar.setTime(new java.util.Date());
            tipologia.setId(1);

            return UtilLib.creazioneCalendarioLib(anno, mese, 1, progetto);
        } else {
            return calendarioRepository.findById(calendario.getId()).get();
        }
    }
}