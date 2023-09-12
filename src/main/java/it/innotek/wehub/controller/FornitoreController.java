/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Fornitore;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.CandidatoService;
import it.innotek.wehub.service.FornitoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/fornitori")
public class FornitoreController {
    @Autowired private FornitoreService serviceFornitore;
    @Autowired private CandidatoService serviceCandidato;

    @RequestMapping
    public String showFornitoriList(Model model){

        List<Fornitore> listFornitori = serviceFornitore.listAll();

        model.addAttribute("listFornitori",    listFornitori);
        model.addAttribute("fornitoreRicerca", new Fornitore());

        return "fornitori";
    }

    @RequestMapping("/ricerca")
    public String showRicercaFornitoriList(
        Model model,
        Fornitore fornitore
    ){
        String          denominazione = ((null != fornitore.getDenominazione()) && !fornitore.getDenominazione().isEmpty() ) ? fornitore.getDenominazione() : null;
        String          referente     = ((null != fornitore.getReferente()) && !fornitore.getReferente().isEmpty() ) ? fornitore.getReferente() : null;
        String          email         = ((null != fornitore.getEmail()) && !fornitore.getEmail().isEmpty()) ? fornitore.getEmail() : null;
        List<Fornitore> listFornitori = serviceFornitore.listRicerca(denominazione,referente,email);

        model.addAttribute("listFornitori",    listFornitori);
        model.addAttribute("fornitoreRicerca", fornitore);

        return "fornitori";
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(Model model){

        if (model.getAttribute("fornitore") != null) {
            model.addAttribute("fornitore", model.getAttribute("fornitore"));
        } else {
            model.addAttribute("fornitore", new Fornitore());
        }

        model.addAttribute("titoloPagina", "Aggiungi un nuovo fornitore");

        return "fornitore_form";
    }

    @RequestMapping("/salva")
    public String saveFornitore(
        Fornitore fornitore,
        RedirectAttributes ra
    ){
        if (controllaDenominazioneDuplicata(fornitore.getDenominazione()) == 1) {

            ra.addFlashAttribute("message", "Denominazione già associata ad un altro fornitore");

            if (null == fornitore.getId()) {
                ra.addFlashAttribute("fornitore", fornitore);

                return "redirect:/fornitori/aggiungi";
            }
        }
        serviceFornitore.save(fornitore);
        ra.addFlashAttribute("message", "Il fornitore è stato salvato con successo");

        return "redirect:/fornitori";
    }

    @RequestMapping("/modifica/{id}")
    public String showEditForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            model.addAttribute("fornitore",    serviceFornitore.get(id));
            model.addAttribute("titoloPagina", "Modifica fornitore");

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/fornitori";
        }
        return "fornitore_form";
    }

    @RequestMapping("/elimina/{id}")
    public String deleteFornitore(
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            if (serviceCandidato.getAssociatiFornitori(id) == 1) {
                ra.addFlashAttribute("message", "Il fornitore è associato ad alcuni candidati e non può essere cancellato");
                return "redirect:/fornitori";
            }
            serviceFornitore.delete(id);
            ra.addFlashAttribute("message", "Il fornitore è stato cancellato con successo");
        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/fornitori";
    }

    public Integer controllaDenominazioneDuplicata(String denominazione){
        return serviceFornitore.controllaDenominazione(denominazione);
    }
}
