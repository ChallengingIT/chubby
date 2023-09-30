/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.EmailSenderService;
import it.innotek.wehub.entity.KeyPeople;
import it.innotek.wehub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/keypeople")
public class KeyPeopleController {

    @Autowired
    private KeyPeopleRepository keyPeopleRepository;
    @Autowired
    private ClienteRepository   clienteRepository;
    @Autowired
    private OwnerRepository     ownerRepository;
    @Autowired
    private TipologiaRepository tipologiaRepository;
    @Autowired
    private LivelloRepository   livelloRepository;
    @Autowired
    private ProvinceRepository  provinceRepository;
    @Autowired
    private EmailSenderService  serviceEmailSender;

    private static final Logger logger = LoggerFactory.getLogger(KeyPeopleController.class);

    @RequestMapping
    public String showKeyPeopleList(Model model){
        try {
            model.addAttribute("titoloPagina", "KeyPeople");
            model.addAttribute("listKeyPeople", keyPeopleRepository.findAll());
            model.addAttribute("keyPeopleRicerca", new KeyPeople());
            model.addAttribute("listOwner", ownerRepository.findAll());
            model.addAttribute("listaAziende", clienteRepository.findAll());

            return "key_people";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showRicercaKeypeople(
        Model model,
        KeyPeople keyPeople
    ){
        try {
            String          status        = ( ( null != keyPeople.getStatus() ) && !keyPeople.getStatus().isEmpty() ) ? keyPeople.getStatus() : null;
            Integer         owner         = ( null != keyPeople.getOwner() ) ? keyPeople.getOwner().getId() : null;
            Integer         azienda       = ( null != keyPeople.getAzienda() ) ? keyPeople.getAzienda().getId() : null;
            List<KeyPeople> listKeyPeople = keyPeopleRepository.ricercaByStatusAndIdOwnerAndIdAzienda(status, owner, azienda);

            model.addAttribute("titoloPagina", "KeyPeople");
            model.addAttribute("listKeyPeople", listKeyPeople);
            model.addAttribute("keyPeopleRicerca", keyPeople);
            model.addAttribute("listOwner", ownerRepository.findAll());
            model.addAttribute("listaAziende", clienteRepository.findAll());

            return "key_people";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(Model model){
        try {
            if (model.getAttribute("keyPeople") != null) {
                model.addAttribute("keyPeople", model.getAttribute("keyPeople"));
            } else {
                model.addAttribute("keyPeople", new KeyPeople());
            }

            model.addAttribute("titoloPagina", "Aggiungi un nuovo contatto");
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaProvince", provinceRepository.findAll());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listOwner", ownerRepository.findAll());
            model.addAttribute("listaAziende", clienteRepository.findAll());

            return "keypeople_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveKeyPeople(
        KeyPeople keyPeople,
        RedirectAttributes ra
    ){
        try {
            if (( null == keyPeople.getId() ) && controllaDenominazioneDuplicata(keyPeople.getNome())) {

                ra.addFlashAttribute("message", "Nome gia' associato ad un altro contatto");
                return "redirect:/keypeople/aggiungi";
            }

            keyPeopleRepository.save(keyPeople);
            ra.addFlashAttribute("message", "Il contatto e' stato salvato con successo");
            return "redirect:/keypeople";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/dettaglio/{id}")
    public String showForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            KeyPeople keyPeopleToShow = keyPeopleRepository.findById(id).get();

            model.addAttribute("titoloPagina", "Visualizzazione Keypeople: " + keyPeopleToShow.getNome());
            model.addAttribute("keyPeople", keyPeopleToShow);

            return "keypeople_dettaglio";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/{id}")
    public String deleteKeypeople(
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            keyPeopleRepository.deleteById(id);
            ra.addFlashAttribute("message", "L'azienda è stata cancellata con successo");

            return "redirect:/keypeople";
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
            model.addAttribute("keyPeople", keyPeopleRepository.findById(id).get());
            model.addAttribute("titoloPagina", "Modifica contatto");
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaProvince", provinceRepository.findAll());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listOwner", ownerRepository.findAll());
            model.addAttribute("listaAziende", clienteRepository.findAll());

            return "keypeople_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    public boolean controllaDenominazioneDuplicata(String nome){
        Optional<KeyPeople> keyPeople =  keyPeopleRepository.findByNome(nome);
        return keyPeople.isPresent();
    }
}