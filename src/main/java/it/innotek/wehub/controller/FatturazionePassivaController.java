/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.FatturazionePassiva;
import it.innotek.wehub.repository.FatturazionePassivaRepository;
import it.innotek.wehub.repository.FornitoreRepository;
import it.innotek.wehub.repository.StatoFPRepository;
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
@RequestMapping("/fatturazione/passiva")
public class FatturazionePassivaController {

    @Autowired
    private FatturazionePassivaRepository fatturazionePassivaRepository;
    @Autowired
    private FornitoreRepository           fornitoreRepository;
    @Autowired
    private StatoFPRepository             statoFPRepository;

    private static final Logger logger = LoggerFactory.getLogger(FatturazionePassivaController.class);

    @RequestMapping
    public String showFatturazionePassivaList(Model model){

        try {
            model.addAttribute("fatturazionePassivaRicerca", new FatturazionePassiva());
            model.addAttribute("listaFatPassive", fatturazionePassivaRepository.findAll());
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());
            model.addAttribute("listaStati", statoFPRepository.findAll());

            return "fatturazione_passiva";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showRicercaFattPassivaList(
        Model model,
        FatturazionePassiva fatturazione
    ){
        try {
            Integer                   idFornitore = fatturazione.getFornitore() != null ? fatturazione.getFornitore().getId() : null;
            Integer                   idStato     = fatturazione.getStato() != null ? fatturazione.getStato().getId() : null;
            List<FatturazionePassiva> listFatture = fatturazionePassivaRepository.ricercaByIdFornitoreAndIdStato(idFornitore, idStato);

            model.addAttribute("listaFatPassive", listFatture);
            model.addAttribute("fatturazionePassivaRicerca", fatturazione);
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());
            model.addAttribute("listaStati", statoFPRepository.findAll());

            return "fatturazione_passiva";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi")
    public String showNewFormFattPassiva(Model model){
        try {
            if (model.getAttribute("fatturazione") != null) {
              model.addAttribute("fatturazione", model.getAttribute("fatturazione"));
            } else {
              model.addAttribute("fatturazione", new FatturazionePassiva());
            }

            model.addAttribute("titoloPagina", "Aggiungi una nuova fatturazione passiva");
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());
            model.addAttribute("listaStati", statoFPRepository.findAll());

            return "fatturazione_passiva_form";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveFattPassiva(
        FatturazionePassiva fatturazione,
        RedirectAttributes ra
    ){
        try {
            fatturazionePassivaRepository.save(fatturazione);
            ra.addFlashAttribute("message", "La fattura è stata salvata con successo");
            return "redirect:/fatturazione/passiva";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/modifica/{id}")
    public String showEditFormFattPass(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            model.addAttribute("fatturazione", fatturazionePassivaRepository.findById(id).get());
            model.addAttribute("titoloPagina", "Modifica fatturazione passiva");
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());
            model.addAttribute("listaStati", statoFPRepository.findAll());

            return "fatturazione_passiva_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
          }
    }
}