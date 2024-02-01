/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.FatturazionePassiva;
import it.innotek.wehub.entity.Tesoreria;
import it.innotek.wehub.repository.ClienteRepository;
import it.innotek.wehub.repository.FatturazionePassivaRepository;
import it.innotek.wehub.repository.TesoreriaRepository;
import it.innotek.wehub.util.UtilLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.time.LocalDate;

@RestController
@RequestMapping("/tesoreria")
public class TesoreriaController {

    @Autowired
    private TesoreriaRepository           tesoreriaRepository;
    @Autowired
    private ClienteRepository             clienteRepository;
    @Autowired
    private FatturazionePassivaRepository fatturazionePassivaRepository;

    private static final Logger logger = LoggerFactory.getLogger(TesoreriaController.class);

    @RequestMapping
    public String showTesoreriaList(Model model){
        logger.debug("tesoreria");

        try {
            String    meseCorrente   = UtilLib.meseItaliano(LocalDate.now().getMonth().name());
            int       annoCorrente   = LocalDate.now().getYear();
            Tesoreria tesoreria      = tesoreriaRepository.findByMeseAndAnno(meseCorrente, annoCorrente);
            Tesoreria nuovaTesoreria = new Tesoreria();

            if (null == tesoreria) {

                nuovaTesoreria.setMese(meseCorrente);
                nuovaTesoreria.setAnno(annoCorrente);

                tesoreriaRepository.save(nuovaTesoreria);

                tesoreria = tesoreriaRepository.findByMeseAndAnno(meseCorrente, annoCorrente);
            }

            //tesoreria.setClienti(clienteRepository.findAll());

            LocalDate localDate = LocalDate.of(annoCorrente, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());

            tesoreria.setFatture(fatturazionePassivaRepository.findByScadenzaLessThan(localDate));

            model.addAttribute("tesoreria", tesoreria);
            model.addAttribute("tesoreriaRicerca", new Tesoreria());

            return "tesoreria";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showTesoreriaRicerca(
        Model model,
        Tesoreria tesoreriaRicerca
    ) {
        logger.debug("tesoreria ricerca");

        try {
            String    meseRicerca = tesoreriaRicerca.getMese();
            int       annoRicerca = ( null == tesoreriaRicerca.getAnno() ) ? LocalDate.now().getYear() : tesoreriaRicerca.getAnno();
            Tesoreria tesoreria   = tesoreriaRepository.findByMeseAndAnno(meseRicerca, annoRicerca);

            if (null == tesoreria) {
                Tesoreria nuovaTesoreria = new Tesoreria();
                nuovaTesoreria.setMese(meseRicerca);
                nuovaTesoreria.setId(tesoreriaRepository.findMaxId() + 1);
                nuovaTesoreria.setAnno(annoRicerca);

                tesoreriaRepository.save(nuovaTesoreria);

                tesoreria = tesoreriaRepository.findByMeseAndAnno(meseRicerca, annoRicerca);
            }

            //tesoreria.setClienti(clienteRepository.findAll());

            LocalDate localDate = LocalDate.of(annoRicerca, numeroMese(meseRicerca), 28);

            tesoreria.setFatture(fatturazionePassivaRepository.findByScadenzaLessThan(localDate));

            model.addAttribute("tesoreria", tesoreria);
            model.addAttribute("tesoreriaRicerca", tesoreriaRicerca);

            return "tesoreria";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveTesoreria(
        RedirectAttributes ra,
        Tesoreria tesoreria,
        Model model
    ) {
        logger.debug("tesoreria salva");

        try {
            Double totaleSpese   = 0D;
            Double totaleIncassi = 0D;

            /*for (Cliente cliente : tesoreria.getClienti()) {

                if (( null != cliente.getGuadagno() ) && !cliente.getGuadagno().isEmpty()) {

                    Cliente clienteUpdate = clienteRepository.findById(cliente.getId()).get();
                    clienteUpdate.setGuadagno(cliente.getGuadagno());

                    totaleIncassi += Double.parseDouble(cliente.getGuadagno());

                    clienteRepository.save(clienteUpdate);
                }
            }*/

            for (FatturazionePassiva fattura : tesoreria.getFatture()) {
                FatturazionePassiva fatturaUpdate = fatturazionePassivaRepository.findById(fattura.getId()).get();

                fatturaUpdate.setImporto(fattura.getImporto());

                totaleSpese += Double.parseDouble(fattura.getImporto());

                fatturazionePassivaRepository.save(fatturaUpdate);
            }

            totaleSpese += Double.parseDouble(( tesoreria.getF24() != null && !tesoreria.getF24().isEmpty() ) ? tesoreria.getF24() : "0");
            totaleSpese += Double.parseDouble(( tesoreria.getIva() != null && !tesoreria.getIva().isEmpty() ) ? tesoreria.getIva() : "0");
            totaleSpese += Double.parseDouble(( tesoreria.getStipendi() != null && !tesoreria.getStipendi().isEmpty() ) ? tesoreria.getStipendi() : "0");

            tesoreria.setTotaleSpese(new DecimalFormat("#.##").format(totaleSpese));
            tesoreria.setTotaleIncassi(new DecimalFormat("#.##").format(totaleIncassi));

            tesoreriaRepository.save(tesoreria);

            ra.addFlashAttribute("message", "La tesoreria e' aggiornata con successo");

            return "redirect:/tesoreria";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    public int numeroMese(String mese){
        logger.debug("tesoreria numeroMese");

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