/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Attivita;
import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.Owner;
import it.innotek.wehub.entity.TipologiaAttivita;
import it.innotek.wehub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private ProvinceRepository          provinceRepository;
    @Autowired
    private ClienteRepository           clienteRepository;
    @Autowired
    private OwnerRepository             ownerRepository;
    @Autowired
    private TipologiaRepository         tipologiaRepository;
    @Autowired
    private TipologiaAttivitaRepository tipologiaAttivitaRepository;
    @Autowired
    private AttivitaRepository          attivitaRepository;

    private static final Logger logger = LoggerFactory.getLogger(AziendeController.class);
    
    @RequestMapping
    public String showAziendeList(Model model){

        try {
            model.addAttribute("titoloPagina", "Aziende");
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("clienteRicerca", new Cliente());
            model.addAttribute("listOwner", ownerRepository.findAll());

            return "aziende";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showRicercaAziendeList(
        Model model,
        Cliente cliente
    ){
        try {
            Integer       status        = cliente.getStatus() != null ? cliente.getStatus() : null;
            Integer       owner         = cliente.getOwner() != null ? cliente.getOwner().getId() : null;
            String        tipologia     = ( cliente.getTipologia() != null && !cliente.getTipologia().isEmpty() ) ? cliente.getTipologia() : null;
            String        denominazione = cliente.getDenominazione() != null ? cliente.getDenominazione() : null;
            List<Cliente> listaAziende  = clienteRepository.ricercaByStatusAndOwner_IdAndTipologiaAndDenominazione(status, owner, tipologia, denominazione);

            model.addAttribute("listaAziende", listaAziende);
            model.addAttribute("clienteRicerca", cliente);
            model.addAttribute("listOwner", ownerRepository.findAll());

            return "aziende";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(Model model){
        try {
            List<Owner> listOwner = ownerRepository.findAll();

            if (null != model.getAttribute("cliente")) {
                model.addAttribute("cliente", model.getAttribute("cliente"));
            } else {
                model.addAttribute("cliente", new Cliente());
            }

            model.addAttribute("titoloPagina", "Aggiungi un nuovo cliente");
            model.addAttribute("listaProvince", provinceRepository.findAll());
            model.addAttribute("listOwner", listOwner);

            return "aziende_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveAzienda(
        Cliente cliente,
        RedirectAttributes ra
    ){
        try {
            if (controllaDuplicati(cliente, ra)) {
                return "redirect:/aziende/aggiungi";
            }

            clienteRepository.save(cliente);
            ra.addFlashAttribute("message", "Il cliente e' stato salvato con successo");
            return "redirect:/aziende";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/attivita/salva/{idAzienda}/{idTipoAttivita}")
    public String salvaAttivita(
        @PathVariable("idAzienda") Integer idAzienda,
        @PathVariable("idTipoAttivita") Integer idTipoAttivita,
        Attivita attivita,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            TipologiaAttivita tipologiaAttivita = new TipologiaAttivita();
            tipologiaAttivita.setId(idTipoAttivita);
            attivita.setTipologia(tipologiaAttivita);
            attivita.setData(OffsetDateTime.now());

            if (( null != attivita.getKeyPeople() ) && ( null == attivita.getKeyPeople().getId() )) {
                attivita.setKeyPeople(null);
            }

            attivitaRepository.save(attivita);

            return "redirect:/aziende/dettaglio/" + idAzienda;
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
    ) {
        try {
            Cliente        clienteToShow      = clienteRepository.findById(id).get();
            Integer        needAperti         = clienteToShow.getNeeds() != null ? clienteToShow.getNeeds().size() : 0;
            Integer        needVinti          = clienteToShow.getNeeds() != null ? (int)clienteToShow.getNeeds().stream().filter(s -> s.getStato().getId() == 3).count() : 0;
            OffsetDateTime dataUltimaAttivita = null;

            if (null != clienteToShow.getAttivita() && !clienteToShow.getAttivita().isEmpty()) {
                dataUltimaAttivita = clienteToShow.getAttivita().stream().max(Comparator.comparing(Attivita::getData)).get().getData();
            }

            Attivita nuovaAttivita = new Attivita();
            nuovaAttivita.setCliente(clienteToShow);

            model.addAttribute("titoloPagina", "Visualizzazione " + clienteToShow.getDenominazione());
            model.addAttribute("needAperti", needAperti);
            model.addAttribute("needVinti", needVinti);
            model.addAttribute("dataUltimaAttivita", dataUltimaAttivita);
            model.addAttribute("dataUltimaAttivita", dataUltimaAttivita);
            model.addAttribute("cliente", clienteToShow);
            model.addAttribute("listaAttivita", clienteToShow.getAttivita());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaTipologie", tipologiaAttivitaRepository.findAll());
            model.addAttribute("nuovaAttivita", nuovaAttivita);
            model.addAttribute("ricercaAttivita", new Attivita());

            return "azienda_dettaglio";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/dettaglio/ricerca/{id}")
    public String showDettaglioRicerca(
        @PathVariable("id") Integer id,
        Model model,
        Attivita ricercaAttivita
    ) {
        try {
            Cliente clienteToShow = clienteRepository.findById(id).get();

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

            model.addAttribute("titoloPagina", "Visualizzazione " + clienteToShow.getDenominazione());
            model.addAttribute("needAperti", needAperti);
            model.addAttribute("needVinti", needVinti);
            model.addAttribute("dataUltimaAttivita", dataUltimaAttivita);
            model.addAttribute("dataUltimaAttivita", dataUltimaAttivita);
            model.addAttribute("listaAttivita", attivitaRepository.ricercaByTipologia_IdAndOwner_IdAndCliente_Id(idTipologia, idOwner, id));
            model.addAttribute("cliente", clienteToShow);
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaTipologie", tipologiaAttivitaRepository.findAll());
            model.addAttribute("nuovaAttivita", nuovaAttivita);
            model.addAttribute("ricercaAttivita", ricercaAttivita);

            return "azienda_dettaglio";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/modifica/{id}")
    public String showEditForm(
        @PathVariable("id") Integer id,
        Model model ,
        RedirectAttributes ra
    ) {
        try {
            model.addAttribute("cliente", clienteRepository.findById(id).get());
            model.addAttribute("titoloPagina", "Modifica azienda");
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaProvince", provinceRepository.findAll());
            model.addAttribute("listOwner", ownerRepository.findAll());

            return "aziende_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/{id}")
    public String deleteCliente(
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            clienteRepository.deleteById(id);
            ra.addFlashAttribute("message", "L'azienda è stata cancellata con successo");

            return "redirect:/aziende";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    private boolean controllaDuplicati(
        Cliente cliente,
        RedirectAttributes ra
    ){
        boolean           toReturn       = false;
        Optional<Cliente> clienteToCheck = cliente.getId() != null ? clienteRepository.findById(cliente.getId()) : Optional.empty();

        List<Cliente> clientiByDenominazione = clienteRepository.findByDenominazione(cliente.getDenominazione());
        List<Cliente> clientiByEmail = clienteRepository.findByEmail(cliente.getEmail());

        if ((null != clientiByDenominazione && !clientiByDenominazione.isEmpty()) && clienteToCheck.isEmpty()) {
          ra.addFlashAttribute("message", "Denominazione gia' associata ad un altro cliente" );
          toReturn = true;
        }

        if ((null != clientiByEmail && !clientiByEmail.isEmpty()) && clienteToCheck.isEmpty()) {
          ra.addFlashAttribute("message", "Email gia' associata ad un altro cliente" );
          toReturn = true;
        }

        return toReturn;
    }
}