/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.EmailSenderService;
import it.innotek.wehub.entity.KeyPeople;
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
@RequestMapping("/keypeople")
public class KeyPeopleController {

  @Autowired private KeyPeopleService keyPeopleService;
  @Autowired private NeedService serviceNeed;
  @Autowired private ClienteService serviceCliente;
  @Autowired private OwnerService serviceOwner;
  @Autowired private ProgettoService serviceProgetto;
  @Autowired private TipologiaService serviceTipologia;
  @Autowired private LivelloService serviceLivelli;
  @Autowired private ProvinceService serviceProvince;
  @Autowired private EmailSenderService serviceEmailSender;

  @RequestMapping
  public String showKeyPeopleList(Model model){

    model.addAttribute("titoloPagina",     "KeyPeople");
    model.addAttribute("listKeyPeople",    keyPeopleService.listAll());
    model.addAttribute("keyPeopleRicerca", new KeyPeople());
    model.addAttribute("listOwner",        serviceOwner.listAll());
    model.addAttribute("listaAziende",     serviceCliente.listAll());

    return "key_people";
  }

  @RequestMapping("/ricerca")
  public String showRicercaKeypeople(
      Model model,
      KeyPeople keyPeople
  ){
    String          status        = ((null != keyPeople.getStatus()) && !keyPeople.getStatus().isEmpty()) ? keyPeople.getStatus() : null;
    Integer         owner         = (null != keyPeople.getOwner()) ? keyPeople.getOwner().getId() : null;
    Integer         azienda       = (null != keyPeople.getAzienda()) ? keyPeople.getAzienda().getId() : null;
    List<KeyPeople> listKeyPeople = keyPeopleService.listRicercaKeyPeople(status,owner,azienda);

    model.addAttribute("titoloPagina",     "KeyPeople");
    model.addAttribute("listKeyPeople",    listKeyPeople);
    model.addAttribute("keyPeopleRicerca", keyPeople);
    model.addAttribute("listOwner",        serviceOwner.listAll());
    model.addAttribute("listaAziende",     serviceCliente.listAll());

    return "key_people";
  }

  @RequestMapping("/aggiungi")
  public String showNewForm(Model model){

    if (model.getAttribute("keyPeople") != null) {
      model.addAttribute("keyPeople", model.getAttribute("keyPeople"));
    } else {
      model.addAttribute("keyPeople", new KeyPeople());
    }

    model.addAttribute("titoloPagina",           "Aggiungi un nuovo contatto");
    model.addAttribute("listaTipologie",         serviceTipologia.listAll());
    model.addAttribute("listaProvince",          serviceProvince.listAll());
    model.addAttribute("listaLivelliScolastici", serviceLivelli.listAll());
    model.addAttribute("listOwner",              serviceOwner.listAll());
    model.addAttribute("listaAziende",           serviceCliente.listAll());

    return "keypeople_form";
  }

  @RequestMapping("/salva")
  public String saveKeyPeople(
      KeyPeople keyPeople,
      RedirectAttributes ra
  ){
    if ((null == keyPeople.getId()) && controllaDenominazioneDuplicata(keyPeople.getNome()) == 1){

      ra.addFlashAttribute("message", "Nome gia' associato ad un altro contatto");
      return "redirect:/keypeople/aggiungi";
    }

    keyPeopleService.save(keyPeople);
    ra.addFlashAttribute("message", "Il contatto e' stato salvato con successo");
    return "redirect:/keypeople";
  }

  @RequestMapping("/dettaglio/{id}")
  public String showForm(
      @PathVariable("id") Integer id,
      Model model,
      RedirectAttributes ra
  ){
    try {

      KeyPeople keyPeopleToShow = keyPeopleService.get(id);
      model.addAttribute("titoloPagina","Visualizzazione Keypeople: " + keyPeopleToShow.getNome());
      model.addAttribute("keyPeople", keyPeopleToShow);

    } catch (ElementoNonTrovatoException e) {
      ra.addFlashAttribute("message", e.getMessage());
      return "redirect:/keypeople";
    }

    return "keypeople_dettaglio";
  }

  @RequestMapping("/elimina/{id}")
  public String deleteKeypeople(
      @PathVariable("id") Integer id,
      RedirectAttributes ra
  ){
    try {

      keyPeopleService.delete(id);
      ra.addFlashAttribute("message", "L'azienda è stata cancellata con successo");

    } catch (ElementoNonTrovatoException e) {
      ra.addFlashAttribute("message", e.getMessage() );
    }

    return "redirect:/keypeople";
  }

  @RequestMapping("/modifica/{id}")
  public String showEditForm(
      @PathVariable("id") Integer id,
      Model model,
      RedirectAttributes ra
  ){
    try {
      model.addAttribute("keyPeople",              keyPeopleService.get(id));
      model.addAttribute("titoloPagina",           "Modifica contatto");
      model.addAttribute("listaTipologie",         serviceTipologia.listAll());
      model.addAttribute("listaProvince",          serviceProvince.listAll());
      model.addAttribute("listaLivelliScolastici", serviceLivelli.listAll());
      model.addAttribute("listOwner",              serviceOwner.listAll());
      model.addAttribute("listaAziende",           serviceCliente.listAll());

    } catch (ElementoNonTrovatoException e) {
      ra.addFlashAttribute("message", e.getMessage());
      return "redirect:/keypeople";
    }
    return "keypeople_form";
  }

  public Integer controllaDenominazioneDuplicata(String nome){
    return keyPeopleService.controllaNome(nome);
  }
}