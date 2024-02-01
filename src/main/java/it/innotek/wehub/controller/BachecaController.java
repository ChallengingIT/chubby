/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.*;
import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.repository.IntervistaRepository;
import it.innotek.wehub.repository.NeedRepository;
import it.innotek.wehub.repository.OwnerRepository;
import it.innotek.wehub.repository.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bacheca")
public class BachecaController {

    @Autowired
    private NeedRepository       needRepository;
    @Autowired
    private OwnerRepository      ownerRepository;
    @Autowired
    private IntervistaRepository intervistaRepository;
    @Autowired
    private StaffRepository      staffRepository;

    private static final Logger logger = LoggerFactory.getLogger(BachecaController.class);

    @RequestMapping
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String bacheca(Model model){
        try {
            logger.debug("bacheca");

            model.addAttribute("needData", getNeedData());
            model.addAttribute("dashboardIncontri", getDashboardIncontri());
            model.addAttribute("listaProssimiAggiornamenti", intervistaRepository.findAll());
            model.addAttribute("listOwner", ownerRepository.findAll());
            model.addAttribute("listNeedOrdinati", needRepository.findNeedOrdinati());
            model.addAttribute("colorBackground", "#ffb700");

            StringBuilder    messaggioCompleanno = null;
            List<Intervista> interviste          = intervistaRepository.findIntervisteImminenti();
            List<Staff>      staffs              = staffRepository.findAll();
            int              count               = 0;

            logger.debug("Lista interviste e staff recuperate");

            for (Staff staff : staffs) {

                LocalDate locale = staff.getDataNascita().toLocalDate();
                LocalDate oggi   = LocalDate.now();

                if (locale.getMonthValue() == oggi.getMonthValue() && locale.getDayOfMonth() == oggi.getDayOfMonth()) {
                    if (count == 0) {
                        messaggioCompleanno = new StringBuilder("- Oggi e' il compleanno di ");
                        count++;
                    }
                    messaggioCompleanno.append(staff.getNome()).append(" ").append(staff.getCognome()).append(" - ");
                }
            }

            DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatterOra  = DateTimeFormatter.ofPattern("HH:mm");

            for (Intervista intervista : interviste) {

                String data = intervista.getDataAggiornamento().format(formatterData);
                String ora  = intervista.getDataAggiornamento().format(formatterOra);

                intervista.setDataAVideo(data);
                intervista.setOraAVideo(ora);
            }

            logger.debug("Lista interviste imminenti", interviste.toArray());

            model.addAttribute("listIntervisteImminenti", interviste);

            if (messaggioCompleanno != null) {
                model.addAttribute("messaggioCompleanno", messaggioCompleanno.toString());
            }

            return "bacheca";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    private List<OccorrenzeIncontro> getDashboardIncontri() {

        List<OccorrenzeIncontro> list                    = new ArrayList<>();
        List<Intervista>         listIntervistaOwnerCur  = intervistaRepository.findIntervisteSettimanaCur();
        List<Intervista>         listIntervistaOwnerMeno = intervistaRepository.findIntervisteSettimanaCurMeno();
        List<Intervista>         listIntervistaOwnerPiu  = intervistaRepository.findIntervisteSettimanaCurPiu();
        Integer                  week                    = needRepository.findWeek();
        Integer                  weekPre                 = needRepository.findWeekPre();
        Integer                  weekSuc                 = needRepository.findWeekSuc();
        List<Owner>              owners                  = ownerRepository.findAll();
        OccorrenzeIncontro       occorrenzaPre           = new OccorrenzeIncontro();
        OccorrenzeIncontro       occorrenza              = new OccorrenzeIncontro();
        OccorrenzeIncontro       occorrenzaSuc           = new OccorrenzeIncontro();
        List<OccorrenzeOwner>    occorrenzeOwnersPre     = new ArrayList<>();
        List<OccorrenzeOwner>    occorrenzeOwners        = new ArrayList<>();
        List<OccorrenzeOwner>    occorrenzeOwnersSuc     = new ArrayList<>();

        occorrenzaPre.setSettimana(weekPre);
        occorrenza.setSettimana(week);
        occorrenzaSuc.setSettimana(weekSuc);

        for (Owner owner : owners) {
            OccorrenzeOwner occorrenzaOwnerPre = new OccorrenzeOwner();
            OccorrenzeOwner occorrenzaOwner    = new OccorrenzeOwner();
            OccorrenzeOwner occorrenzaOwnerSuc = new OccorrenzeOwner();

            occorrenzaOwnerPre.setOccorrenze(contaOccorrenzeIncontro(listIntervistaOwnerMeno,owner.getId()));
            occorrenzaOwnerPre.setDescrizione(owner.getDescrizione());
            occorrenzeOwnersPre.add(occorrenzaOwnerPre);

            occorrenzaOwner.setOccorrenze(contaOccorrenzeIncontro(listIntervistaOwnerCur,owner.getId()));
            occorrenzaOwner.setDescrizione(owner.getDescrizione());
            occorrenzeOwners.add(occorrenzaOwner);

            occorrenzaOwnerSuc.setOccorrenze(contaOccorrenzeIncontro(listIntervistaOwnerPiu,owner.getId()));
            occorrenzaOwnerSuc.setDescrizione(owner.getDescrizione());
            occorrenzeOwnersSuc.add(occorrenzaOwnerSuc);
        }

        occorrenzaPre.setOccorrenzeOwner(occorrenzeOwnersPre);
        occorrenza.setOccorrenzeOwner(occorrenzeOwners);
        occorrenzaSuc.setOccorrenzeOwner(occorrenzeOwnersSuc);

        list.add(occorrenzaPre);
        list.add(occorrenza);
        list.add(occorrenzaSuc);

        return list
            .stream()
            .sorted(Comparator.comparingInt(OccorrenzeIncontro::getSettimana))
            .collect(Collectors.toList());
    }

    private Integer contaOccorrenzeIncontro(
        List<Intervista> lista,
        int idOwner
    ){
        Integer count = 0;

        for (Intervista intervista: lista) {
            /*if (intervista.getOwner().getId() == idOwner) {
                count++;
            }*/
        }
        return count;
    }

    private List<OccorrenzeNeed> getNeedData() {
        List<Need>           listNeedPre = needRepository.findNeedSettimanaCurMeno();
        List<Need>           listNeedCur = needRepository.findNeedSettimanaCur();
        List<Need>           listNeedSuc = needRepository.findNeedSettimanaCurPiu();
        Integer              week        = needRepository.findWeek();
        Integer              weekPre     = needRepository.findWeekPre();
        Integer              weekSuc     = needRepository.findWeekSuc();
        List<OccorrenzeNeed> listAttuali = new ArrayList<>();
        Integer              chiusiP     = contaOccorrenzeNeeds(listNeedPre, 5);
        Integer              persiP      = contaOccorrenzeNeeds(listNeedPre, 4);
        Integer              vintiP      = contaOccorrenzeNeeds(listNeedPre, 3);
        Integer              prioritariP = contaOccorrenzeNeedsPrioritari(listNeedPre);
        Integer              chiusiC     = contaOccorrenzeNeeds(listNeedCur, 5);
        Integer              persiC      = contaOccorrenzeNeeds(listNeedCur, 4);
        Integer              vintiC      = contaOccorrenzeNeeds(listNeedCur, 3);
        Integer              prioritariC = contaOccorrenzeNeedsPrioritari(listNeedCur);
        Integer              chiusiS     = contaOccorrenzeNeeds(listNeedSuc, 5);
        Integer              persiS      = contaOccorrenzeNeeds(listNeedSuc, 4);
        Integer              vintiS      = contaOccorrenzeNeeds(listNeedSuc, 3);
        Integer              prioritariS = contaOccorrenzeNeedsPrioritari(listNeedSuc);
        OccorrenzeNeed       occorrenzaP = new OccorrenzeNeed(vintiP, persiP, chiusiP, prioritariP, weekPre);
        OccorrenzeNeed       occorrenzaC = new OccorrenzeNeed(vintiC, persiC, chiusiC, prioritariC, week);
        OccorrenzeNeed       occorrenzaS = new OccorrenzeNeed(vintiS, persiS, chiusiS, prioritariS, weekSuc);

        listAttuali.add(occorrenzaP);
        listAttuali.add(occorrenzaC);
        listAttuali.add(occorrenzaS);

        return listAttuali;
    }

    private Integer contaOccorrenzeNeeds(
        List<Need> lista,
        int idToCheck
    ){
        Integer count = 0;

        for (Need need: lista) {
            if (need.getStato().getId()==idToCheck) {
                count++;
            }
        }
        return count;
    }

    private Integer contaOccorrenzeNeedsPrioritari(
        List<Need> lista
    ){
        Integer count = 0;

        for (Need need: lista) {
            if (need.getPriorita() == 1) {
                count++;
            }
        }
        return count;
    }
}