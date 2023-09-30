/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.FatturazioneAttiva;
import it.innotek.wehub.repository.ClienteRepository;
import it.innotek.wehub.repository.FatturazioneAttivaRepository;
import it.innotek.wehub.repository.StatoFARepository;
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
@RequestMapping("/fatturazione/attiva")
public class FatturazioneAttivaController {

    @Autowired
    private FatturazioneAttivaRepository fatturazioneAttivaRepository;
    @Autowired
    private ClienteRepository            clienteRepository;
    @Autowired
    private StatoFARepository            statoFARepository;

    private static final Logger logger = LoggerFactory.getLogger(FatturazioneAttivaController.class);

    @RequestMapping
    public String showFatturazioneAttivaList(Model model){
        try {
            List<FatturazioneAttiva> fattureAttive = fatturazioneAttivaRepository.findAll();

            for (FatturazioneAttiva fattura : fattureAttive) {
              Cliente cliente = clienteRepository.findById(fattura.getCliente().getId()).get();
              fattura.getCliente().setDenominazione(cliente.getDenominazione());
            }

            model.addAttribute("fatturazioneAttivaRicerca", new FatturazioneAttiva());
            model.addAttribute("listaFatAttive", fattureAttive);
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaStati", statoFARepository.findAll());

            return "fatturazione_attiva";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showRicercaFattAttivaList(
        Model model,
        FatturazioneAttiva fatturazione
    ){
        try {
            Integer                  idCliente   = fatturazione.getCliente() != null ? fatturazione.getCliente().getId() : null;
            Integer                  idStato     = fatturazione.getStato() != null ? fatturazione.getStato().getId() : null;
            List<FatturazioneAttiva> listFatture = fatturazioneAttivaRepository.ricercaByIdClienteAndIdStato(idCliente, idStato);

            for (FatturazioneAttiva fattura : listFatture) {
              Cliente cliente = clienteRepository.findById(fattura.getCliente().getId()).get();
              fattura.getCliente().setDenominazione(cliente.getDenominazione());
            }

            model.addAttribute("listaFatAttive", listFatture);
            model.addAttribute("fatturazioneAttivaRicerca", fatturazione);
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaStati", statoFARepository.findAll());

            return "fatturazione_attiva";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi")
    public String showNewFormFattAttiva(Model model){
        try {
            if (null != model.getAttribute("fatturazione")) {
              model.addAttribute("fatturazione", model.getAttribute("fatturazione"));
            } else {
              model.addAttribute("fatturazione", new FatturazioneAttiva());
            }

            model.addAttribute("titoloPagina", "Aggiungi una nuova fatturazione attiva");
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaStati", statoFARepository.findAll());

            return "fatturazione_attiva_form";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveFattAttiva(
        FatturazioneAttiva fatturazione,
        RedirectAttributes ra
    ){
        try {
            fatturazioneAttivaRepository.save(fatturazione);
            ra.addFlashAttribute("message", "La fattura è stata salvata con successo");
            return "redirect:/fatturazione/attiva";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/modifica/{id}")
    public String showEditFormFattAttiva(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            model.addAttribute("fatturazione", fatturazioneAttivaRepository.findById(id).get());
            model.addAttribute("titoloPagina", "Modifica fatturazione attiva");
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaStati", statoFARepository.findAll());

            return "fatturazione_attiva_form";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }
}