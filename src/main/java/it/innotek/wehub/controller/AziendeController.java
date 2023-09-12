/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Attivita;
import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.Owner;
import it.innotek.wehub.entity.TipologiaAttivita;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/aziende")
public class AziendeController {

  @Autowired
  private ProvinceService          provinceService;
  @Autowired
  private ClienteService           clienteService;
  @Autowired
  private OwnerService             ownerService;
  @Autowired
  private TipologiaService         serviceTipologia;
  @Autowired
  private TipologiaAttivitaService serviceTipologiaAttivita;
  @Autowired
  private AttivitaService          serviceAttivita;

  @RequestMapping
  public String showAziendeList(Model model){

    model.addAttribute("titoloPagina",   "Aziende");
    model.addAttribute("listaAziende",   clienteService.listAll());
    model.addAttribute("clienteRicerca", new Cliente());
    model.addAttribute("listOwner",      ownerService.listAll());

    return "aziende";
  }

  @RequestMapping("/ricerca")
  public String showRicercaAziendeList(
      Model model,
      Cliente cliente
  ){
    Integer       status        = cliente.getStatus() != null ? cliente.getStatus() : null;
    Integer       owner         = cliente.getOwner() != null ? cliente.getOwner().getId() : null;
    String        tipologia     = ( cliente.getTipologia() != null && !cliente.getTipologia().isEmpty() ) ? cliente.getTipologia() : null;
    String        denominazione = cliente.getDenominazione() != null ? cliente.getDenominazione() : null;
    List<Cliente> listaAziende  = clienteService.listRicercaAziende(
        status,
        owner,
        tipologia,
        denominazione
    );

    model.addAttribute("listaAziende",   listaAziende);
    model.addAttribute("clienteRicerca", cliente);
    model.addAttribute("listOwner",      ownerService.listAll());

    return "aziende";
  }

  @RequestMapping("/aggiungi")
  public String showNewForm(Model model){
    List<Owner> listOwner = ownerService.listAll();

    if (null != model.getAttribute("cliente")) {
      model.addAttribute("cliente", model.getAttribute("cliente"));
    } else {
      model.addAttribute("cliente", new Cliente());
    }

    model.addAttribute("titoloPagina",  "Aggiungi un nuovo cliente");
    model.addAttribute("listaProvince", provinceService.listAll());
    model.addAttribute("listOwner",     listOwner);

    return "aziende_form";
  }

  @RequestMapping("/salva")
  public String saveAzienda(
      Cliente cliente,
      RedirectAttributes ra
  ){
    if (controllaDuplicati(cliente, ra)) {
      return "redirect:/aziende/aggiungi";
    }

    clienteService.save(cliente);
    ra.addFlashAttribute("message", "Il cliente e' stato salvato con successo" );
    return "redirect:/aziende";
  }

  @RequestMapping("/attivita/salva/{idAzienda}/{idTipoAttivita}")
  public String salvaAttivita(
      @PathVariable("idAzienda") Integer idAzienda,
      @PathVariable("idTipoAttivita") Integer idTipoAttivita,
      Attivita attivita,
      Model model,
      RedirectAttributes ra
  ) {
      TipologiaAttivita tipologiaAttivita = new TipologiaAttivita();
      tipologiaAttivita.setId(idTipoAttivita);
      attivita.setTipologia(tipologiaAttivita);
      attivita.setData(OffsetDateTime.now());

      if ((null != attivita.getKeyPeople()) && (null == attivita.getKeyPeople().getId())) {
        attivita.setKeyPeople(null);
      }

      serviceAttivita.save(attivita);

      return "redirect:/aziende/dettaglio/"+idAzienda;
  }


  @RequestMapping("/dettaglio/{id}")
  public String showForm(
      @PathVariable("id") Integer id,
      Model model,
      RedirectAttributes ra
  ) {
      try {
        Cliente        clienteToShow      = clienteService.get(id);
        Integer        needAperti         = clienteToShow.getNeeds() != null ? clienteToShow.getNeeds().size() : 0;
        Integer        needVinti          = clienteToShow.getNeeds() != null ? (int)clienteToShow.getNeeds().stream().filter(s -> s.getStato().getId() == 3).count() : 0;
        OffsetDateTime dataUltimaAttivita = null;

        if (null != clienteToShow.getAttivita() && !clienteToShow.getAttivita().isEmpty()) {
          dataUltimaAttivita = clienteToShow.getAttivita().stream().max(Comparator.comparing(Attivita::getData)).get().getData();
        }

        Attivita nuovaAttivita = new Attivita();
        nuovaAttivita.setCliente(clienteToShow);

        model.addAttribute("titoloPagina",       "Visualizzazione " + clienteToShow.getDenominazione());
        model.addAttribute("needAperti",         needAperti);
        model.addAttribute("needVinti",          needVinti);
        model.addAttribute("dataUltimaAttivita", dataUltimaAttivita);
        model.addAttribute("dataUltimaAttivita", dataUltimaAttivita);
        model.addAttribute("cliente",            clienteToShow);
        model.addAttribute("listaAttivita",      clienteToShow.getAttivita());
        model.addAttribute("listaOwner",         ownerService.listAll());
        model.addAttribute("listaTipologie",     serviceTipologiaAttivita.listAll());
        model.addAttribute("nuovaAttivita",      nuovaAttivita);
        model.addAttribute("ricercaAttivita",    new Attivita());
      } catch (ElementoNonTrovatoException e) {
        ra.addFlashAttribute("message", e.getMessage());
        return "redirect:/aziende";
      }

      return "azienda_dettaglio";

  }

  @RequestMapping("/dettaglio/ricerca/{id}")
  public String showDettaglioRicerca(
      @PathVariable("id") Integer id,
      Model model,
      Attivita ricercaAttivita
  ) {
    try{
      Cliente clienteToShow = clienteService.get(id);

      Integer        needAperti         = clienteToShow.getNeeds() != null ? clienteToShow.getNeeds().size() : 0;
      Integer        needVinti          = clienteToShow.getNeeds() != null ? (int)clienteToShow.getNeeds().stream().filter(s -> s.getStato().getId() == 3).count() : 0;
      OffsetDateTime dataUltimaAttivita = null;

      if (null != clienteToShow.getAttivita() && !clienteToShow.getAttivita().isEmpty()) {
        dataUltimaAttivita = clienteToShow.getAttivita().stream().max(Comparator.comparing(Attivita::getData)).get().getData();
      }

      Attivita nuovaAttivita = new Attivita();
      Integer  idTipologia   = ricercaAttivita.getTipologia() != null ? ricercaAttivita.getTipologia().getId() : null;
      Integer  idOwner       = ricercaAttivita.getOwner() != null ? ricercaAttivita.getOwner().getId() : null;

      nuovaAttivita.setCliente(clienteToShow);

      model.addAttribute("titoloPagina",       "Visualizzazione " + clienteToShow.getDenominazione());
      model.addAttribute("needAperti",         needAperti);
      model.addAttribute("needVinti",          needVinti);
      model.addAttribute("dataUltimaAttivita", dataUltimaAttivita);
      model.addAttribute("dataUltimaAttivita", dataUltimaAttivita);
      model.addAttribute("listaAttivita",      serviceAttivita.ricerca(idTipologia, idOwner));
      model.addAttribute("cliente",            clienteToShow);
      model.addAttribute("listaOwner",         ownerService.listAll());
      model.addAttribute("listaTipologie",     serviceTipologiaAttivita.listAll());
      model.addAttribute("nuovaAttivita",      nuovaAttivita);
      model.addAttribute("ricercaAttivita",    ricercaAttivita);

    } catch (ElementoNonTrovatoException e) {
      return "/aziende/dettaglio/"+ id;
    }

    return "azienda_dettaglio";
  }

  @RequestMapping("/modifica/{id}")
  public String showEditForm(
      @PathVariable("id") Integer id,
      Model model ,
      RedirectAttributes ra
  ) {
    try {

      model.addAttribute("cliente",        clienteService.get(id));
      model.addAttribute("titoloPagina",  "Modifica azienda");
      model.addAttribute("listaTipologie", serviceTipologia.listAll());
      model.addAttribute("listaProvince",  provinceService.listAll());
      model.addAttribute("listOwner",      ownerService.listAll());

    } catch (ElementoNonTrovatoException e) {
      ra.addFlashAttribute("message", e.getMessage() );
      return "redirect:/aziende";
    }
    return "aziende_form";
  }

  @RequestMapping("/elimina/{id}")
  public String deleteCliente(
      @PathVariable("id") Integer id,
      RedirectAttributes ra
  ){
    try {

      clienteService.delete(id);
      ra.addFlashAttribute("message", "L'azienda è stata cancellata con successo");

    } catch (ElementoNonTrovatoException e) {
      ra.addFlashAttribute("message", e.getMessage());
    }
    return "redirect:/aziende";
  }

  private boolean controllaDuplicati(
      Cliente cliente,
      RedirectAttributes ra
  ){
    boolean           toReturn       = false;
    Optional<Cliente> clienteToCheck = cliente.getId() != null ? clienteService.getById(cliente.getId()) : Optional.empty();

    if (clienteService.controllaDenominazione(cliente.getDenominazione()) == 1 &&
      ((null == clienteToCheck) || !clienteToCheck.isPresent())
    ) {
      ra.addFlashAttribute("message", "Denominazione gia' associata ad un altro cliente" );
      toReturn = true;
    }

    if (clienteService.controllaEmail(cliente.getEmail()) == 1 &&
      ((null == clienteToCheck) || !clienteToCheck.isPresent())
    ) {
      ra.addFlashAttribute("message", "Email gia' associata ad un altro cliente" );
      toReturn = true;
    }
    return toReturn;
  }
}