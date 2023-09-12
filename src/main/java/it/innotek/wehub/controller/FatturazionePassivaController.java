/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.FatturazionePassiva;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.*;
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
  private FatturazioneAttivaService serviceFatturazioneAttiva;

  @Autowired
  private FatturazionePassivaService serviceFatturazionePassiva;

  @Autowired
  private ClienteService serviceCliente;

  @Autowired
  private FornitoreService serviceFornitore;

  @Autowired
  private StatoFAService serviceStatoFA;

  @Autowired
  private StatoFPService serviceStatoFP;

  @RequestMapping
  public String showFatturazionePassivaList(Model model){

    model.addAttribute("fatturazionePassivaRicerca", new FatturazionePassiva());
    model.addAttribute("listaFatPassive",            serviceFatturazionePassiva.listAll());
    model.addAttribute("listaFornitori",             serviceFornitore.listAll());
    model.addAttribute("listaStati",                 serviceStatoFP.listAll());

    return "fatturazione_passiva";
  }

  @RequestMapping("/ricerca")
  public String showRicercaFattPassivaList(
      Model model,
      FatturazionePassiva fatturazione
  ){
    Integer                   idFornitore = fatturazione.getFornitore() != null ? fatturazione.getFornitore().getId() : null;
    Integer                   idStato     = fatturazione.getStato() != null ? fatturazione.getStato().getId() : null;
    List<FatturazionePassiva> listFatture = serviceFatturazionePassiva.listRicerca(idFornitore,idStato);

    model.addAttribute("listaFatPassive",            listFatture);
    model.addAttribute("fatturazionePassivaRicerca", fatturazione);
    model.addAttribute("listaFornitori",             serviceFornitore.listAll());
    model.addAttribute("listaStati",                 serviceStatoFP.listAll());

    return "fatturazione_passiva";
  }

  @RequestMapping("/aggiungi")
  public String showNewFormFattPassiva(Model model){

    if (model.getAttribute("fatturazione") != null) {
        model.addAttribute("fatturazione", model.getAttribute("fatturazione"));
    } else {
        model.addAttribute("fatturazione", new FatturazionePassiva());
    }

    model.addAttribute("titoloPagina",   "Aggiungi una nuova fatturazione passiva");
    model.addAttribute("listaFornitori", serviceFornitore.listAll());
    model.addAttribute("listaStati",     serviceStatoFP.listAll());

    return "fatturazione_passiva_form";
  }

  @RequestMapping("/salva")
  public String saveFattPassiva(
      FatturazionePassiva fatturazione,
      RedirectAttributes ra
  ){
    serviceFatturazionePassiva.save(fatturazione);
    ra.addFlashAttribute("message", "La fattura è stata salvata con successo" );
    return "redirect:/fatturazione/passiva";
  }

  @RequestMapping("/modifica/{id}")
  public String showEditFormFattPass(
      @PathVariable("id") Integer id,
      Model model,
      RedirectAttributes ra
  ){
    try {
      model.addAttribute("fatturazione",   serviceFatturazionePassiva.get(id));
      model.addAttribute("titoloPagina",   "Modifica fatturazione passiva");
      model.addAttribute("listaFornitori", serviceFornitore.listAll());
      model.addAttribute("listaStati",     serviceStatoFP.listAll());

    } catch (ElementoNonTrovatoException e) {
      ra.addFlashAttribute("message", e.getMessage() );
      return "redirect:/fatturazione/passiva";
    }
    return "fatturazione_passiva_form";
  }
}