/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.AssociazioneCandidatoNeed;
import it.innotek.wehub.entity.Candidato;
import it.innotek.wehub.entity.File;
import it.innotek.wehub.entity.TipologiaF;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/staffing")
public class CandidatoController {

    @Autowired
    private CandidatoService     serviceCandidato;
    @Autowired
    private ClienteService       serviceCliente;
    @Autowired
    private TipologiaService     serviceTipologia;
    @Autowired
    private FacoltaService       serviceFacolta;
    @Autowired
    private StatoCService        serviceStatoC;
    @Autowired
    private LivelloService       serviceLivelli;
    @Autowired
    private SkillService         serviceSkill;
    @Autowired
    private FileService          serviceFile;
    @Autowired
    private FileCandidatoService serviceFileCandidato;
    @Autowired
    private OwnerService         serviceOwner;
    @Autowired
    private TipoService          serviceTipo;
    @Autowired
    private FornitoreService     serviceFornitore;
    @Autowired
    private AssociazioniService  serviceAssociazione;
    @Autowired
    private NeedService          serviceNeed;

    @RequestMapping
    public String showCandidatiList(Model model){

        List<Candidato> listCandidati = serviceCandidato.listAll();
        List<Integer>   listId        = new ArrayList<>();

        for (Candidato cand : listCandidati) {
            listId.add(cand.getId());
        }

        serviceFileCandidato.deleteFileVecchi(listId.toString(),1);
        serviceFileCandidato.deleteFileVecchi(listId.toString(),2);

        listCandidati = serviceCandidato.listAll();

        model.addAttribute("listCandidati",    listCandidati);
        model.addAttribute("listaTipologie",   serviceTipologia.listAll());
        model.addAttribute("listaTipi",        serviceTipo.listAll());
        model.addAttribute("listaStatiC",      serviceStatoC.listAllOrdered());
        model.addAttribute("candidatoRicerca", new Candidato());
        model.addAttribute("listaFornitori",   serviceFornitore.listAll());
        model.addAttribute("listaFacolta",     serviceFacolta.listAll());
        return "candidati";
    }

    @RequestMapping("/ricerca")
    public String showRicercaCandidatiList(
        Model model,
        Candidato candidato
    ){
        Integer idTipologia = candidato.getTipologia() != null ? candidato.getTipologia().getId() : null;
        Integer idStato     = candidato.getStato() != null ? candidato.getStato().getId() : null;
        Integer idTipo      = candidato.getTipo() != null ? candidato.getTipo().getId() : null;
        String  nome        = ( candidato.getNome() != null && !candidato.getNome().isEmpty() ) ? candidato.getNome() : null;
        String  cognome     = ( candidato.getCognome() != null && !candidato.getCognome().isEmpty() ) ? candidato.getCognome() : null;
        String  email       = ( candidato.getEmail() != null && !candidato.getEmail().isEmpty()) ? candidato.getEmail() : null;

        List<Candidato> listCandidati = serviceCandidato.listRicerca(nome, cognome, email, idTipologia, idStato, idTipo);
        List<Integer>   listId = new ArrayList<>();

        for (Candidato cand : listCandidati) {
            listId.add(cand.getId());
        }

        serviceFileCandidato.deleteFileVecchi(listId.toString(),1);
        serviceFileCandidato.deleteFileVecchi(listId.toString(),2);

        listCandidati = serviceCandidato.listRicerca(nome,cognome,email,idTipologia,idStato,idTipo);

        model.addAttribute("listCandidati",    listCandidati);
        model.addAttribute("listaTipologie",   serviceTipologia.listAll());
        model.addAttribute("listaTipi",        serviceTipo.listAll());
        model.addAttribute("listaStatiC",      serviceStatoC.listAllOrdered());
        model.addAttribute("candidatoRicerca", candidato);
        model.addAttribute("listaFornitori",   serviceFornitore.listAll());
        model.addAttribute("listaFacolta",     serviceFacolta.listAll());

        return "candidati";
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(
        Model model,
        RedirectAttributes ra
    ){

        if (null != model.getAttribute("candidato")) {
            model.addAttribute("candidato", model.getAttribute("candidato"));
        } else {
            model.addAttribute("candidato", new Candidato());
        }

        model.addAttribute("titoloPagina",           "Aggiungi staffing");
        model.addAttribute("listaTipologie",         serviceTipologia.listAll());
        model.addAttribute("listaTipi",              serviceTipo.listAll());
        model.addAttribute("listaStatiC",            serviceStatoC.listAllOrdered());
        model.addAttribute("listaLivelliScolastici", serviceLivelli.listAll());
        model.addAttribute("listaSkillOrdinata",     serviceSkill.listAll());
        model.addAttribute("listaFornitori",         serviceFornitore.listAll());
        model.addAttribute("listaFacolta",           serviceFacolta.listAll());
        model.addAttribute("listOwner",              serviceOwner.listAll());

        return "candidato_form";
    }

    @RequestMapping("/salva")
    public String saveCandidato(
        @RequestParam("file") MultipartFile file,
        @RequestParam("cf") MultipartFile cf,
        Candidato candidato,
        Model model,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        if (controllaMailDuplicata(candidato.getEmail()) == 1) {

            ra.addFlashAttribute("message", "Email già associata ad un altro profilo" );

            if (candidato.getId() == null) {
                ra.addFlashAttribute(candidato);
                return "redirect:/staffing/aggiungi";
            }
        }

        String descrizioneTipologia = recuperaDescrizioneTipologia(candidato.getTipologia().getId());
        String descrizioneLivello   = recuperaDescrizioneLivello(candidato.getLivelloScolastico().getId());

        candidato.getTipologia().setDescrizione(descrizioneTipologia);
        candidato.getLivelloScolastico().setDescrizione(descrizioneLivello);

        if (null == candidato.getRating()) {
            candidato.setRating(0.0);
        }

        if (null == candidato.getFornitore()) {
            candidato.setFornitore(null);
        }

        if (null != candidato.getFacolta()) {
            if (null == candidato.getFacolta().getId()) {
                candidato.setFacolta(null);
            }
        }

        serviceCandidato.save(candidato);

        boolean modifica = false;
        Integer idCandidato;

        if (null != candidato.getId()) {
            modifica=true;
        }

        if (modifica) {
            idCandidato = candidato.getId();
        } else {
            idCandidato = serviceCandidato.getUltimoId();
        }

        if ((null != file.getOriginalFilename()) && !file.getOriginalFilename().isEmpty()) {

            uploadFileVoid(file, idCandidato,1, ra);
        }
        if ((null != cf.getOriginalFilename()) && !cf.getOriginalFilename().isEmpty()) {

            uploadFileVoid(cf, idCandidato,2, ra);
        }

        ra.addFlashAttribute("message", "Il candidato è stato salvato con successo" );
        return "redirect:/intervista/" + idCandidato;
    }

    @RequestMapping("/visualizza/{id}")
    public String showForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            Candidato candidato = serviceCandidato.get(id);

            model.addAttribute("candidato",              candidato);
            model.addAttribute("titoloPagina",           "Modifica staffing");
            model.addAttribute("listaTipologie",         serviceTipologia.listAll());
            model.addAttribute("listaTipi",              serviceTipo.listAll());
            model.addAttribute("listaStatiC",            serviceStatoC.listAllOrdered());
            model.addAttribute("listaLivelliScolastici", serviceLivelli.listAll());
            model.addAttribute("listaSkillOrdinata",     serviceSkill.listAll());
            model.addAttribute("listaFacolta",           serviceFacolta.listAll());
            model.addAttribute("listaFornitori",         serviceFornitore.listAll());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/staffing";
        }

        return "candidato_visualizza_form";
    }

    @RequestMapping("/modifica/{id}")
    public String showEditForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            Candidato candidato = serviceCandidato.get(id);

            model.addAttribute("candidato",              candidato);
            model.addAttribute("titoloPagina",           "Modifica staffing");
            model.addAttribute("listaTipologie",         serviceTipologia.listAll());
            model.addAttribute("listaTipi",              serviceTipo.listAll());
            model.addAttribute("listaStatiC",            serviceStatoC.listAllOrdered());
            model.addAttribute("listaLivelliScolastici", serviceLivelli.listAll());
            model.addAttribute("listaSkillOrdinata",     serviceSkill.listAll());
            model.addAttribute("listaFacolta",           serviceFacolta.listAll());
            model.addAttribute("listOwner",              serviceOwner.listAll());
            model.addAttribute("listaFornitori",         serviceFornitore.listAll());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/staffing";
        }
        return "candidato_form";
    }

    @RequestMapping("/elimina/{id}")
    public String deleteCandidato(
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            Candidato candidato = serviceCandidato.get(id);

            for (AssociazioneCandidatoNeed associazione : candidato.getAssociazioni()) {
                serviceAssociazione.delete(associazione.getId());
            }

            serviceCandidato.delete(id);
            ra.addFlashAttribute("message", "Il candidato è stato cancellato con successo" );

        } catch(ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage() );
        }

        return "redirect:/staffing";
    }

    @RequestMapping("/ottieni/{id}")
    public void getCandidato(
        @PathVariable("id") Integer id,
        Model model
    ) throws ElementoNonTrovatoException {
        model.addAttribute("candidatoToUse", serviceCandidato.get(id));
    }

    public void uploadFileVoid(
        MultipartFile file,
        Integer idCandidato,
        Integer tipoFile,
        RedirectAttributes ra
    ) {
        try {
            String         descrizione          = file.getOriginalFilename();
            byte[]         arrayByte            = file.getBytes();
            String         tipo                 = file.getContentType();
            File           fileOggettoCandidato = new File();
            java.util.Date date                 = new java.util.Date();
            java.sql.Date  sqlDate              = new Date(date.getTime());
            TipologiaF     tipologia            = new TipologiaF();
            Candidato      candidato            = new Candidato();

            fileOggettoCandidato.setData(arrayByte);
            fileOggettoCandidato.setDataInserimento(new java.sql.Date(sqlDate.getTime()));
            fileOggettoCandidato.setDescrizione(descrizione);
            fileOggettoCandidato.setTipo(tipo);

            tipologia.setId(tipoFile);
            fileOggettoCandidato.setTipologia(tipologia);

            candidato.setId(idCandidato);
            fileOggettoCandidato.setCandidato(candidato);

            serviceFile.save(fileOggettoCandidato);

        } catch (Exception e) {
            ra.addFlashAttribute("message", e.getMessage() );
        }
    }

    public String recuperaDescrizioneTipologia(Integer id) throws ElementoNonTrovatoException {
        return serviceTipologia.get(id).getDescrizione();
    }

    public String recuperaDescrizioneLivello(Integer id) throws ElementoNonTrovatoException {
        return serviceLivelli.get(id).getDescrizione();
    }

    public Integer controllaMailDuplicata(String email) {
        return serviceCandidato.controllaEmail(email);
    }

}