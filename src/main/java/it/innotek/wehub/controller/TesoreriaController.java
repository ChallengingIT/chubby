/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.FatturazionePassiva;
import it.innotek.wehub.entity.Tesoreria;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.TesoreriaRepository;
import it.innotek.wehub.service.ClienteService;
import it.innotek.wehub.service.FatturazionePassivaService;
import it.innotek.wehub.service.TesoreriaService;
import it.innotek.wehub.util.UtilLib;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.time.LocalDate;

@Controller
@Log
@RequestMapping("/tesoreria")
public class TesoreriaController {

    @Autowired
    private TesoreriaRepository        tesoreriaRepository;
    @Autowired
    private ClienteService             serviceCliente;
    @Autowired
    private TesoreriaService           serviceTesoreria;
    @Autowired
    private FatturazionePassivaService serviceFattPass;

    @RequestMapping
    public String showTesoreriaList(Model model){

        String    meseCorrente   = UtilLib.meseItaliano(LocalDate.now().getMonth().name());
        int       annoCorrente   = LocalDate.now().getYear();
        Tesoreria tesoreria      = serviceTesoreria.getSelected(meseCorrente, annoCorrente);
        Tesoreria nuovaTesoreria = new Tesoreria();

        if (null == tesoreria) {

            nuovaTesoreria.setMese(meseCorrente);
            nuovaTesoreria.setAnno(annoCorrente);

            serviceTesoreria.save(nuovaTesoreria);

            tesoreria = serviceTesoreria.getSelected(meseCorrente, annoCorrente);
        }

        tesoreria.setClienti(serviceCliente.listAll());

        LocalDate localDate = LocalDate.of(annoCorrente, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());

        tesoreria.setFatture(serviceFattPass.getFattureDaPagare(localDate));

        model.addAttribute("tesoreria",        tesoreria);
        model.addAttribute("tesoreriaRicerca", new Tesoreria());

        return "tesoreria";
    }

    @RequestMapping("/ricerca")
    public String showTesoreriaRicerca(
        Model model,
        Tesoreria tesoreriaRicerca
    ) throws ElementoNonTrovatoException {

        String    meseRicerca = tesoreriaRicerca.getMese();
        int       annoRicerca = (null == tesoreriaRicerca.getAnno()) ? LocalDate.now().getYear() : tesoreriaRicerca.getAnno();
        Tesoreria tesoreria   = serviceTesoreria.getSelected(meseRicerca, annoRicerca);

        if (null == tesoreria) {
            Tesoreria nuovaTesoreria = new Tesoreria();
            nuovaTesoreria.setMese(meseRicerca);
            nuovaTesoreria.setId(serviceTesoreria.getMaxId()+1);
            nuovaTesoreria.setAnno(annoRicerca);

            serviceTesoreria.save(nuovaTesoreria);

            tesoreria = serviceTesoreria.getSelected(meseRicerca, annoRicerca);
        }

        tesoreria.setClienti(serviceCliente.listAll());

        LocalDate localDate = LocalDate.of(annoRicerca,numeroMese(meseRicerca), 28);

        tesoreria.setFatture(serviceFattPass.getFattureDaPagare(localDate));

        model.addAttribute("tesoreria",        tesoreria);
        model.addAttribute("tesoreriaRicerca", tesoreriaRicerca);

        return "tesoreria";
    }

    @RequestMapping("/salva")
    public String saveTesoreria(
        RedirectAttributes ra,
        Tesoreria tesoreria,
        Model model
    ) throws ElementoNonTrovatoException {

        Double totaleSpese   = 0D;
        Double totaleIncassi = 0D;

        for (Cliente cliente : tesoreria.getClienti()) {

            if ((null != cliente.getGuadagno()) && !cliente.getGuadagno().isEmpty()) {

                Cliente clienteUpdate = serviceCliente.get(cliente.getId());
                clienteUpdate.setGuadagno(cliente.getGuadagno());

                totaleIncassi += Double.parseDouble(cliente.getGuadagno());

                serviceCliente.save(clienteUpdate);
            }
        }

        for (FatturazionePassiva fattura : tesoreria.getFatture()) {
            FatturazionePassiva fatturaUpdate = serviceFattPass.get(fattura.getId());

            fatturaUpdate.setImporto(fattura.getImporto());

            totaleSpese += Double.parseDouble(fattura.getImporto());

            serviceFattPass.save(fatturaUpdate);
        }

        totaleSpese += Double.parseDouble((tesoreria.getF24()      != null && !tesoreria.getF24().isEmpty())      ? tesoreria.getF24()      : "0");
        totaleSpese += Double.parseDouble((tesoreria.getIva()      != null && !tesoreria.getIva().isEmpty())      ? tesoreria.getIva()      : "0");
        totaleSpese += Double.parseDouble((tesoreria.getStipendi() != null && !tesoreria.getStipendi().isEmpty()) ? tesoreria.getStipendi() : "0");

        tesoreria.setTotaleSpese(new DecimalFormat("#.##").format(totaleSpese));
        tesoreria.setTotaleIncassi(new DecimalFormat("#.##").format(totaleIncassi));

        tesoreriaRepository.save(tesoreria);

        ra.addFlashAttribute("message", "La tesoreria e' aggiornata con successo");

        return "redirect:/tesoreria";
    }

    public int numeroMese(String mese){
        return switch (mese.toLowerCase()) {
            case "febbraio" -> 2;
            case "marzo" -> 3;
            case "aprile" -> 4;
            case "maggio" -> 5;
            case "giugno" -> 6;
            case "luglio" -> 7;
            case "agosto" -> 8;
            case "settembre" -> 9;
            case "ottobre" -> 10;
            case "novembre" -> 11;
            case "dicembre" -> 12;
            default -> 1;
        };
    }
}