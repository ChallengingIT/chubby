/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.EmailSenderService;
import it.innotek.wehub.entity.*;
import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.entity.staff.StaffModificato;
import it.innotek.wehub.entity.timesheet.*;
import it.innotek.wehub.repository.*;
import it.innotek.wehub.util.ExportExcel;
import it.innotek.wehub.util.UtilLib;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hr")
public class HRController {

    @Autowired
    private StaffRepository              staffRepository;
    @Autowired
    private SkillRepository              skillRepository;
    @Autowired
    private FileRepository               fileRepository;
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

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Staff> getAll()
    {
        logger.info("Staff");

        return staffRepository.findAll();
    }

    @GetMapping("/react/modificato")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<StaffModificato> getAllMenuTendina()
    {
        logger.info("Staff");

        List<Staff> staffs = staffRepository.findAll();

        List<StaffModificato> staffModificati = new ArrayList<>();

        for (Staff staff : staffs) {
            StaffModificato staffModificato = new StaffModificato();

            staffModificato.setCognome(staff.getCognome());
            staffModificato.setEmail(staff.getEmail());
            staffModificato.setId(staff.getId());
            staffModificato.setNome(staff.getNome());
            staffModificato.setNote(staff.getNote());
            staffModificato.setAnniEsperienza(staff.getAnniEsperienza());
            staffModificato.setCellulare(staff.getCellulare());
            staffModificato.setCitta(staff.getCitta());
            staffModificato.setCodFiscale(staff.getCodFiscale());
            staffModificato.setDataInizio(staff.getDataInizio());
            staffModificato.setDataNascita(staff.getDataNascita());
            staffModificato.setDataScadenza(staff.getDataScadenza());
            staffModificato.setFacolta(staff.getFacolta());
            staffModificato.setFiles(staff.getFiles());
            staffModificato.setLivelloScolastico(staff.getLivelloScolastico());
            staffModificato.setIban(staff.getIban());
            staffModificato.setLuogoNascita(staff.getLuogoNascita());
            staffModificato.setRal(staff.getRal());
            staffModificato.setSkills(staff.getSkills());
            staffModificato.setStipendio(staff.getStipendio());
            staffModificato.setTipologia(staff.getTipologia());
            staffModificato.setTipologiaContratto(staff.getTipologiaContratto());

            staffModificati.add(staffModificato);
        }

        return staffModificati;
    }

    @GetMapping("/react/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Staff getById(@PathVariable("id") Integer id)
    {
        logger.info("Staff tramite id");

        return staffRepository.findById(id).get();
    }

    @GetMapping("/react/tipocontratto")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<TipologiaContratto> getAllTipoContratto()
    {
        logger.info("Tipi contratto");

        return tipologiaContrattoRepository.findAll();
    }

    /*@CrossOrigin(origins = "*")
    @PostMapping("/react/salva/utente")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String creaUtente(
        @RequestBody User user
    ){
        logger.debug("hr salva utente");

        try {
            user.setEnabled((byte)1);
            user.getAuthority().setUsername(user.getUsername());

            //BCryptPasswordEncoder encore = new BCryptPasswordEncoder();
            //user.setPassword(encore.encode(user.getPassword()));

            userRepository.save(user);

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }*/

    @PostMapping("/react/staff/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveCandidato(
        @RequestBody Map<String,String> staffMap,
        @RequestParam ("skill") List<Integer> skillList

    ) {
        logger.info("Salvataggio staff");

        try {

            Staff staffEntity = new Staff();

            if(staffMap.get("id") != null) {
                staffEntity = staffRepository.findById(Integer.parseInt(staffMap.get("id"))).get();

                logger.debug("Staff trovato si procede in modifica");
            }

            trasformaMappaInStaff(staffEntity, staffMap, skillList);

            if (controllaMailDuplicata(staffEntity.getEmail())) {

                if (staffEntity.getId() == null) {
                    logger.debug("Staff duplicato");

                    return "DUPLICATO";
                }
            }

            if (null != staffEntity.getFacolta()) {
                if (null == staffEntity.getFacolta().getId()) {
                    staffEntity.setFacolta(null);
                }
            }

            if (null != staffEntity.getId()) {

                staffRepository.save(staffEntity);

                logger.debug("Staff salvato correttamente");

            } else {

                staffRepository.save(staffEntity);

                Integer idStaff = staffEntity.getId();

                Progetto          progetto  = new Progetto();
                TipologiaProgetto tipologia = new TipologiaProgetto();

                progetto.setDescription("Ferie, Permessi e Malattia");
                tipologia.setId(1);
                progetto.setTipologia(tipologia);
                progetto.setIdStaff(idStaff);

                progettoRepository.save(progetto);

                logger.debug("Progetto ferie permessi e malattia salvato correttamente");

                logger.debug("Creazione timesheet...");

                Calendario calendario = creaCalendario(null, progetto);

                logger.debug("Timesheet creato correttamente");
                logger.debug(calendario.toString());
                logger.debug(progetto.toString());

                staffEntity.setTimesheet(calendario);
                staffEntity.getProgetti().add(progetto);

                staffRepository.save(staffEntity);

                logger.debug("Staff salvato correttamente");
            }
            return ""+staffEntity.getId();
            
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @PostMapping("/react/staff/salva/file/{idStaff}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveCandidato(
        @PathVariable("idStaff") Integer idStaff,
        @RequestParam("file") MultipartFile file
    ) {
        logger.info("Staff salva file");

        try {

            Staff staffEntity =  staffRepository.findById(idStaff).get();

            if (null != staffEntity.getId()) {

                if (( null != file.getOriginalFilename() ) && !file.getOriginalFilename().isEmpty()) {
                    staffEntity.getFiles().add(fileVoid(file, 3));
                }

                staffRepository.save(staffEntity);
            }

            logger.debug("Salvataggio effettuato correttamente");

            return "OK";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @DeleteMapping("/react/staff/elimina/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String saveCandidato(
        @PathVariable("id") Integer id
    ) {
        logger.info("Elimina staff");

        try {

            Staff staff = staffRepository.findById(id).get();

            if (null != staff.getTimesheet()) {
                calendarioRepository.deleteById(staff.getTimesheet().getId());
            }

            staffRepository.deleteById(id);

            logger.debug("Eliminazione effettuata correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @PostMapping("/react/staff/sollecito")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String inviaSollecitoChiusuraTimesheet() {

        logger.info("Invio sollecito");

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

                    logger.debug("Invio email a: " + staff.getEmail());

                    serviceEmail.sendHtmlMessage(email);
                }
            }

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @NotNull
    private static Email getEmail(Staff staff, Mese mese, Anno anno) {
        logger.info("get email");

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

    public File fileVoid(MultipartFile file, Integer tipoFile) {
        logger.info("hr fileVoid");

        try {
            File        fileOggettoStaff = getFile(file, tipoFile);

            return fileOggettoStaff;

        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;
    }

    @NotNull
    private static File getFile(
        MultipartFile file,
        Integer tipoFile
    ) throws IOException {
        logger.info("hr getFile");

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

    @GetMapping("/report/estrai")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Report> reportEstrai(
        @RequestParam Map<String,String> ricercaMap
    ){
        logger.info("Estrai report");

        try {
            RicercaReport ricerca = trasformaMappaInRicerca(ricercaMap);

            Integer       fineMese    = UtilLib.calcolaFineMese(ricerca.getMese(), ricerca.getAnno());
            List<Report>  listaReport = new ArrayList<>();


            if (( ( null != ricerca.getGiornoInizio() ) && ricerca.getGiornoInizio() > fineMese ) || ( ( null != ricerca.getGiornoFine() ) && ricerca.getGiornoFine() > fineMese )) {

                return listaReport;
            }

            listaReport = prendiListaReport(ricerca, fineMese);

            return listaReport;

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return null;
        }
    }

    @GetMapping("/report/excel/{anno}/{mese}/{giornoInizio}/{giornoFine}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public void reportExcel(
        @PathVariable("anno") Integer anno,
        @PathVariable("mese") Integer mese,
        @PathVariable("giornoInizio") String giornoInizio,
        @PathVariable("giornoFine") String giornoFine,
        HttpServletResponse response
    ) throws IOException {

        logger.info("Download excel file");

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
        logger.info("Prendi lista report");

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
        logger.info("Crea calendario");

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

    public boolean controllaMailDuplicata(String email) {
        logger.info("Controlla mail duplicata");

        List<Staff> staffs = staffRepository.findByEmail(email);
        return ((null != staffs) && !staffs.isEmpty());
    }

    public void trasformaMappaInStaff(Staff staff, Map<String,String> staffMap, List<Integer> skillList) {
        logger.info("Trasforma mappa in staff");

        staff.setAnniEsperienza(staffMap.get("anniEsperienza") != null ? Double.parseDouble(staffMap.get("anniEsperienza")) : null);
        staff.setCellulare(staffMap.get("cellulare") != null ? staffMap.get("cellulare") : null);
        staff.setCodFiscale(staffMap.get("codFiscale") != null ? staffMap.get("codFiscale") : null);
        staff.setCitta(staffMap.get("citta") != null ? staffMap.get("citta") : null);
        staff.setEmail(staffMap.get("email") != null ? staffMap.get("email") : null);
        staff.setNote(staffMap.get("note") != null ? staffMap.get("note") : null);;
        staff.setCognome(staffMap.get("cognome") != null ? staffMap.get("cognome") : null);
        staff.setDataNascita(staffMap.get("dataNascita") != null ? Date.valueOf(staffMap.get("dataNascita")) : null);
        staff.setDataScadenza(staffMap.get("dataScadenza") != null ? Date.valueOf(staffMap.get("dataScadenza")) : null);
        staff.setDataInizio(staffMap.get("dataInizio") != null ? Date.valueOf(staffMap.get("dataInizio")) : null);
        staff.setLuogoNascita(staffMap.get("luogoNascita") != null ? staffMap.get("luogoNascita") : null);

        if (staffMap.get("facolta") != null) {
            Facolta facolta = new Facolta();
            facolta.setId(Integer.parseInt(staffMap.get("facolta")));

            staff.setFacolta(facolta);
        }
        if (staffMap.get("livelloScolastico") != null) {
            LivelloScolastico livello = new LivelloScolastico();
            livello.setId(Integer.parseInt(staffMap.get("livelloScolastico")));

            staff.setLivelloScolastico(livello);
        }
        staff.setIban(staffMap.get("iban") != null ? staffMap.get("iban") : null);
        staff.setNome(staffMap.get("nome") != null ? staffMap.get("nome") : null);

        staff.setRal(staffMap.get("ral") != null ? staffMap.get("ral") : null);
        staff.setStipendio(staffMap.get("stipendio") != null ? staffMap.get("stipendio") : null);

        if (staffMap.get("tipologiaContratto") != null) {
            TipologiaContratto tipo = new TipologiaContratto();
            tipo.setId(Integer.parseInt(staffMap.get("tipologiaContratto")));

            staff.setTipologiaContratto(tipo);
        }

        if (staffMap.get("tipologia") != null) {
            Tipologia tipologia = new Tipologia();
            tipologia.setId(Integer.parseInt(staffMap.get("tipologia")));

            staff.setTipologia(tipologia);
        }

        Set<Skill> skillListNew = new HashSet<>();

        for (Integer skillId: skillList) {
            Skill skill = new Skill();
            skill.setId(skillId);

            skillListNew.add(skill);
        }

        staff.setSkills(skillListNew);

    }

    public RicercaReport trasformaMappaInRicerca(Map<String,String> ricercaMap) {
        logger.info("Trasforma mappa in ricerca");

        RicercaReport ricerca = new RicercaReport();

        ricerca.setAnno(ricercaMap.get("anno") != null ? Integer.parseInt(ricercaMap.get("anno")) : null);
        ricerca.setMese(ricercaMap.get("mese") != null ? Integer.parseInt(ricercaMap.get("mese")) : null);
        ricerca.setGiornoInizio(ricercaMap.get("giornoInizio") != null ? Integer.parseInt(ricercaMap.get("giornoInizio")) : null);
        ricerca.setGiornoFine(ricercaMap.get("giornoFine") != null ? Integer.parseInt(ricercaMap.get("giornoFine")) : null);

        return ricerca;
    }
}