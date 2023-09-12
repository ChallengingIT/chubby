/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.FatturazioneAttiva;
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
@RequestMapping("/fatturazione/attiva")
public class FatturazioneAttivaController {

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
  public String showFatturazioneAttivaList(Model model){
    List<FatturazioneAttiva> fattureAttive = serviceFatturazioneAttiva.listAll();

    for (FatturazioneAttiva fattura : fattureAttive) {
        Cliente cliente = serviceCliente.getById(fattura.getCliente().getId()).get();
        fattura.getCliente().setDenominazione(cliente.getDenominazione());
    }

    model.addAttribute("fatturazioneAttivaRicerca", new FatturazioneAttiva());
    model.addAttribute("listaFatAttive",            fattureAttive);
    model.addAttribute("listaAziende",              serviceCliente.listAll());
    model.addAttribute("listaStati",                serviceStatoFA.listAll());

    return "fatturazione_attiva";
  }

  @RequestMapping("/ricerca")
  public String showRicercaFattAttivaList(
      Model model,
      FatturazioneAttiva fatturazione
  ){
    Integer                  idCliente   = fatturazione.getCliente() != null ? fatturazione.getCliente().getId() : null;
    Integer                  idStato     = fatturazione.getStato() != null ? fatturazione.getStato().getId() : null;
    List<FatturazioneAttiva> listFatture = serviceFatturazioneAttiva.listRicerca(idCliente,idStato);

    for (FatturazioneAttiva fattura : listFatture) {
      Cliente cliente = serviceCliente.getById(fattura.getCliente().getId()).get();
      fattura.getCliente().setDenominazione(cliente.getDenominazione());
    }

    model.addAttribute("listaFatAttive",            listFatture);
    model.addAttribute("fatturazioneAttivaRicerca", fatturazione);
    model.addAttribute("listaAziende",              serviceCliente.listAll());
    model.addAttribute("listaStati",                serviceStatoFA.listAll());

    return "fatturazione_attiva";
  }

  @RequestMapping("/aggiungi")
  public String showNewFormFattAttiva(Model model){

    if (null != model.getAttribute("fatturazione")) {
      model.addAttribute("fatturazione", model.getAttribute("fatturazione"));
    } else {
      model.addAttribute("fatturazione", new FatturazioneAttiva());
    }

    model.addAttribute("titoloPagina", "Aggiungi una nuova fatturazione attiva");
    model.addAttribute("listaAziende", serviceCliente.listAll());
    model.addAttribute("listaStati",   serviceStatoFA.listAll());

    return "fatturazione_attiva_form";
  }

  @RequestMapping("/salva")
  public String saveFattAttiva(
      FatturazioneAttiva fatturazione,
      RedirectAttributes ra
  ){
    serviceFatturazioneAttiva.save(fatturazione);
    ra.addFlashAttribute("message", "La fattura è stata salvata con successo");
    return "redirect:/fatturazione/attiva";
  }

  @RequestMapping("/modifica/{id}")
  public String showEditFormFattAttiva(
      @PathVariable("id") Integer id,
      Model model,
      RedirectAttributes ra
  ){
    try {
      model.addAttribute("fatturazione", serviceFatturazioneAttiva.get(id));
      model.addAttribute("titoloPagina", "Modifica fatturazione attiva");
      model.addAttribute("listaAziende", serviceCliente.listAll());
      model.addAttribute("listaStati",   serviceStatoFA.listAll());

    } catch (ElementoNonTrovatoException e) {
      ra.addFlashAttribute("message", e.getMessage() );
      return "redirect:/fatturazione/attiva";
    }
    return "fatturazione_attiva_form";
  }
}