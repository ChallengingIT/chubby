/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Fornitore;
import it.innotek.wehub.repository.CandidatoRepository;
import it.innotek.wehub.repository.FornitoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private FornitoreRepository fornitoreRepository;
    @Autowired
    private CandidatoRepository candidatoRepository;

    private static final Logger logger = LoggerFactory.getLogger(FornitoreController.class);

    @RequestMapping
    public String showFornitoriList(Model model){
        try {
            List<Fornitore> listFornitori = fornitoreRepository.findAll();

            model.addAttribute("listFornitori", listFornitori);
            model.addAttribute("fornitoreRicerca", new Fornitore());

            return "fornitori";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showRicercaFornitoriList(
        Model model,
        Fornitore fornitore
    ){
        try {
            String          denominazione = ( ( null != fornitore.getDenominazione() ) && !fornitore.getDenominazione().isEmpty() ) ? fornitore.getDenominazione() : null;
            String          referente     = ( ( null != fornitore.getReferente() ) && !fornitore.getReferente().isEmpty() ) ? fornitore.getReferente() : null;
            String          email         = ( ( null != fornitore.getEmail() ) && !fornitore.getEmail().isEmpty() ) ? fornitore.getEmail() : null;
            List<Fornitore> listFornitori = fornitoreRepository.ricercaByDenominazioneAndReferenteAndEmail(denominazione, referente, email);

            model.addAttribute("listFornitori", listFornitori);
            model.addAttribute("fornitoreRicerca", fornitore);

            return "fornitori";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(Model model){
        try {
            if (model.getAttribute("fornitore") != null) {
                model.addAttribute("fornitore", model.getAttribute("fornitore"));
            } else {
                model.addAttribute("fornitore", new Fornitore());
            }

            model.addAttribute("titoloPagina", "Aggiungi un nuovo fornitore");

            return "fornitore_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveFornitore(
        Fornitore fornitore,
        RedirectAttributes ra
    ){
        try {
            if (controllaDenominazioneDuplicata(fornitore.getDenominazione())) {

                ra.addFlashAttribute("message", "Denominazione già associata ad un altro fornitore");

                if (null == fornitore.getId()) {
                    ra.addFlashAttribute("fornitore", fornitore);

                    return "redirect:/fornitori/aggiungi";
                }
            }
            fornitoreRepository.save(fornitore);
            ra.addFlashAttribute("message", "Il fornitore è stato salvato con successo");

            return "redirect:/fornitori";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/modifica/{id}")
    public String showEditForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            model.addAttribute("fornitore", fornitoreRepository.findById(id).get());
            model.addAttribute("titoloPagina", "Modifica fornitore");

            return "fornitore_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/{id}")
    public String deleteFornitore(
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            if (candidatoRepository.findFornitoriAssociati(id) == 1) {
                ra.addFlashAttribute("message", "Il fornitore è associato ad alcuni candidati e non può essere cancellato");
                return "redirect:/fornitori";
            }
            fornitoreRepository.deleteById(id);
            ra.addFlashAttribute("message", "Il fornitore è stato cancellato con successo");

            return "redirect:/fornitori";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    public boolean controllaDenominazioneDuplicata(String denominazione){
        List<Fornitore> fornitori = fornitoreRepository.findByDenominazione(denominazione);
        return ((null != fornitori) && !fornitori.isEmpty());
    }
}
