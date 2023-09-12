/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.*;
import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.service.IntervistaService;
import it.innotek.wehub.service.NeedService;
import it.innotek.wehub.service.OwnerService;
import it.innotek.wehub.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bacheca")
public class BachecaController {

    @Autowired
    private NeedService       serviceNeed;
    @Autowired
    private OwnerService      serviceOwner;
    @Autowired
    private IntervistaService serviceIntervista;
    @Autowired
    private StaffService      serviceStaff;

    @RequestMapping
    public String bacheca(Model model){

        model.addAttribute("needData",          getNeedData());
        model.addAttribute("dashboardIncontri", getDashboardIncontri());
        model.addAttribute("listOwner",         serviceOwner.listAll());
        model.addAttribute("listNeedOrdinati",  serviceNeed.getNeedOrdinati());
        model.addAttribute("colorBackground",   "#ffb700");

        StringBuilder    messaggioCompleanno = null;
        List<Intervista> interviste          = serviceIntervista.listIntervisteImminenti();
        List<Staff>      staffs              = serviceStaff.listAll();
        int              count               = 0;

        for (Staff staff: staffs) {

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

        model.addAttribute("listIntervisteImminenti", interviste);

        if (messaggioCompleanno != null) {
            model.addAttribute("messaggioCompleanno", messaggioCompleanno.toString());
        }

        return "bacheca";
    }
    private List<OccorrenzeIncontro> getDashboardIncontri() {

        List<OccorrenzeIncontro> list                    = new ArrayList<>();
        List<Intervista>         listIntervistaOwnerCur  = serviceIntervista.getIntervisteSettimanaCur();
        List<Intervista>         listIntervistaOwnerMeno = serviceIntervista.getIntervisteSettimanaCurMeno();
        List<Intervista>         listIntervistaOwnerPiu  = serviceIntervista.getIntervisteSettimanaCurPiu();
        Integer                  week                    = serviceNeed.getWeek();
        Integer                  weekPre                 = serviceNeed.getWeekPre();
        Integer                  weekSuc                 = serviceNeed.getWeekSuc();
        List<Owner>              owners                  = serviceOwner.listAll();
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
            if (intervista.getOwner().getId() == idOwner) {
                count++;
            }
        }
        return count;
    }

    private List<OccorrenzeNeed> getNeedData() {
        List<Need>           listNeedPre = serviceNeed.getNeedSettimanaCurMeno();
        List<Need>           listNeedCur = serviceNeed.getNeedSettimanaCur();
        List<Need>           listNeedSuc = serviceNeed.getNeedSettimanaCurPiu();
        Integer              week        = serviceNeed.getWeek();
        Integer              weekPre     = serviceNeed.getWeekPre();
        Integer              weekSuc     = serviceNeed.getWeekSuc();
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
