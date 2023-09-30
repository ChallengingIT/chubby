/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.*;
import it.innotek.wehub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/need")
public class NeedController {

    @Autowired
    private CandidatoRepository    candidatoRepository;
    @Autowired
    private ClienteRepository      clienteRepository;
    @Autowired
    private TipologiaNRepository   tipologiaNRepository;
    @Autowired
    private StatoNRepository       statoNRepository;
    @Autowired
    private OwnerRepository        ownerRepository;
    @Autowired
    private LivelloRepository      livelloRepository;
    @Autowired
    private SkillRepository        skillRepository;
    @Autowired
    private NeedRepository         needRepository;
    @Autowired
    private TipologiaRepository    tipologiaRepository;
    @Autowired
    private StatoCRepository       statoCRepository;
    @Autowired
    private TipoRepository         tipoRepository;
    @Autowired
    private AssociazioniRepository associazioniRepository;

    private static final Logger logger = LoggerFactory.getLogger(NeedController.class);

    @RequestMapping
    public String showNeedList(Model model){
        try {
            List<Need> listNeed = needRepository.findAll();

            for (Need need : listNeed) {
                if (need.getCliente() != null) {
                    Optional<Cliente> cliente = clienteRepository.findById(need.getCliente().getId());
                    cliente.ifPresent(need::setCliente);
                }
            }

            model.addAttribute("listNeed", listNeed);
            model.addAttribute("needRicerca", new Need());
            model.addAttribute("needStato", new Need());
            model.addAttribute("listaTipologieN", tipologiaNRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaStatiN", statoNRepository.findAll());

            return "lista_need";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showRicercaList(
        Model model,
        Need need
    ) {
        try {
            Integer    idCliente   = ( null != need.getCliente() ) ? need.getCliente().getId() : null;
            Integer    idStato     = ( null != need.getStato() ) ? need.getStato().getId() : null;
            Integer    idTipologia = ( null != need.getTipologia() ) ? need.getTipologia().getId() : null;
            Integer    idOwner     = ( null != need.getOwner() ) ? need.getOwner().getId() : null;
            String     settimana   = ( ( null != need.getWeek() ) && !need.getWeek().isEmpty() ) ? need.getWeek() : null;
            List<Need> listNeed    = needRepository.ricerca(idCliente, idStato, need.getPriorita(), idTipologia, settimana, idOwner);

            for (Need needFor : listNeed) {
                Cliente cliente = clienteRepository.findById(needFor.getCliente().getId()).get();
                needFor.setCliente(cliente);
            }

            model.addAttribute("listNeed", listNeed);
            model.addAttribute("needRicerca", need);
            model.addAttribute("needStato", new Need());
            model.addAttribute("listaTipologieN", tipologiaNRepository.findAll());
            model.addAttribute("listaAziende", clienteRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStatiN", statoNRepository.findAll());

            return "lista_need";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/{idCliente}")
    public String showNeedIdList(
        @PathVariable("idCliente") Integer idCliente,
        Model model
    ) {
        try {
            Cliente    cliente  = clienteRepository.findById(idCliente).get();
            List<Need> listNeed = needRepository.findByCliente_Id(idCliente);

            model.addAttribute("listNeed", listNeed);
            model.addAttribute("cliente", cliente);
            model.addAttribute("needRicerca", new Need());
            model.addAttribute("listaTipologieN", tipologiaNRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStatiN", statoNRepository.findAll());

            return "lista_need_cliente";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca/{idCliente}")
    public String showRicercaList(
        @PathVariable("idCliente") Integer idCliente,
        Model model,
        Need need
    ) {
        try {
            Cliente    cliente     = clienteRepository.findById(idCliente).get();
            Integer    idStato     = ( null != need.getStato() ) ? need.getStato().getId() : null;
            Integer    idTipologia = ( null != need.getTipologia() ) ? need.getTipologia().getId() : null;
            String     settimana   = ( ( null != need.getWeek() ) && !need.getWeek().isEmpty() ) ? need.getWeek() : null;
            Integer    idOwner     = ( null != need.getOwner() ) ? need.getOwner().getId() : null;
            List<Need> listNeed    = needRepository.ricerca(idCliente, idStato, need.getPriorita(), idTipologia, settimana, idOwner);

            model.addAttribute("cliente", cliente);
            model.addAttribute("listNeed", listNeed);
            model.addAttribute("needRicerca", need);
            model.addAttribute("listaTipologieN", tipologiaNRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStatiN", statoNRepository.findAll());

            return "lista_need_cliente";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi/{idCliente}")
    public String showNewForm(
        @PathVariable("idCliente") Integer idCliente,
        Model model
    ){
        try {
            model.addAttribute("need", new Need());
            model.addAttribute("idCliente", idCliente);
            model.addAttribute("titoloPagina", "Aggiungi nuovo need");
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaTipologieN", tipologiaNRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStatiN", statoNRepository.findAll());

            return "need_cliente_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(Model model){
        try {
            model.addAttribute("need", new Need());
            model.addAttribute("titoloPagina", "Aggiungi nuovo need");
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaTipologieN", tipologiaNRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStatiN", statoNRepository.findAll());
            model.addAttribute("listaAziende", clienteRepository.findAll());

            return "need_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveNeed(
        Need need,
        RedirectAttributes ra
    ) {
        try {
            Cliente                         cliente      = new Cliente();
            List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByNeed_Id(need.getId());
            List<Candidato>                 candidati    = candidatoRepository.findByNeed_Id(need.getId());

            cliente.setId(need.getCliente().getId());

            need.setCliente(cliente);
            need.setAssociazioni(associazioni);
            need.setCandidati(candidati);

            needRepository.save(need);
            ra.addFlashAttribute("message", "Il need è stato salvato con successo");
            return "redirect:/need";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva/stato/{idNeed}")
    public String saveStatoNeed(
        @PathVariable("idNeed") Integer idNeed,
        Need needStato,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            Need                            need         = needRepository.findById(idNeed).get();
            Cliente                         cliente      = new Cliente();
            List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByNeed_Id(need.getId());
            List<Candidato>                 candidati    = candidatoRepository.findByNeed_Id(need.getId());

            need.setStato(needStato.getStato());

            cliente.setId(need.getCliente().getId());

            need.setCliente(cliente);
            need.setAssociazioni(associazioni);
            need.setCandidati(candidati);

            needRepository.save(need);
            ra.addFlashAttribute("message", "Il need è stato salvato con successo");
            return "redirect:/need";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva/{idCliente}")
    public String saveNeed(
        @PathVariable("idCliente") Integer idCliente,
        Need need,
        RedirectAttributes ra
    ) {
        try {
            Cliente                         cliente      = new Cliente();
            List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByNeed_Id(need.getId());
            List<Candidato>                 candidati    = candidatoRepository.findByNeed_Id(need.getId());

            cliente.setId(idCliente);
            need.setCliente(cliente);
            need.setAssociazioni(associazioni);
            need.setCandidati(candidati);

            needRepository.save(need);
            ra.addFlashAttribute("message", "Il need è stato salvato con successo");
            return "redirect:/need/" + idCliente;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/modifica/{idCliente}/{id}")
    public String showEditForm(
        @PathVariable("idCliente") Integer idCliente,
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Need need = needRepository.findById(id).get();

            model.addAttribute("need", need);
            model.addAttribute("idCliente", idCliente);
            model.addAttribute("titoloPagina", "Modifica need");
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaTipologieN", tipologiaNRepository.findAll());
            model.addAttribute("listaOwner", ownerRepository.findAll());
            model.addAttribute("listaStatiN", statoNRepository.findAll());

            return "need_cliente_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/{idCliente}/{id}")
    public String deleteNeed(
        @PathVariable("idCliente") Integer idCliente,
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            Need need = needRepository.findById(id).get();

            for (AssociazioneCandidatoNeed associazione : need.getAssociazioni()) {
                associazioniRepository.deleteById(associazione.getId());
            }

            needRepository.deleteById(id);
            ra.addFlashAttribute("message", "Il need è stato cancellato con successo");

            return "redirect:/need/" + idCliente;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/match/{idNeed}")
    public String showMatchForm(
        @PathVariable("idNeed") Integer idNeed,
        Model model
    ) {
        try {
            model.addAttribute("need", needRepository.findById(idNeed).get());
            model.addAttribute("titoloPagina", "Match del need");
            model.addAttribute("listCandidatiNonAssociati", candidatoRepository.findCandidatiNonAssociati(idNeed));
            model.addAttribute("listCandidatiAssociati", candidatoRepository.findCandidatiAssociati(idNeed));
            model.addAttribute("listAssociazioniNeed", associazioniRepository.findByNeed_Id(idNeed));
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("candidatoRicerca", new Candidato());

            return "liste_match_need";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca/match/{idNeed}")
    public String showRicercaMatchForm(
        @PathVariable("idNeed") Integer idNeed,
        Model model,
        Candidato candidato
    ) {
        try {
            Integer idTipologia         = ( null != candidato.getTipologia() ) ? candidato.getTipologia().getId() : null;
            Integer idTipo              = ( null != candidato.getTipo() ) ? candidato.getTipo().getId() : null;
            String  nome                = ( ( null != candidato.getNome() ) && !candidato.getNome().isEmpty() ) ? candidato.getNome() : null;
            String  cognome             = ( ( null != candidato.getCognome() ) && !candidato.getCognome().isEmpty() ) ? candidato.getCognome() : null;
            Double  anniEsperienzaRuolo = ( null != candidato.getAnniEsperienzaRuolo() ) ? candidato.getAnniEsperienzaRuolo() : null;
            Integer anniMinimi          = null;
            Integer anniMassimi         = null;

            if (null != anniEsperienzaRuolo) {
                if (anniEsperienzaRuolo == 0) {
                    anniMassimi = 1;
                } else if (anniEsperienzaRuolo == 1) {
                    anniMinimi  = 1;
                    anniMassimi = 2;
                } else if (anniEsperienzaRuolo == 2) {
                    anniMinimi  = 2;
                    anniMassimi = 5;
                } else {
                    anniMinimi = 5;
                }
            }

            model.addAttribute("listCandidatiNonAssociati", candidatoRepository.ricercaCandidatiNonAssociati(idNeed, nome, cognome, idTipologia, idTipo, anniMinimi, anniMassimi));

            model.addAttribute("need", needRepository.findById(idNeed).get());
            model.addAttribute("titoloPagina", "Match del need");
            model.addAttribute("candidatoRicerca", candidato);
            model.addAttribute("listAssociazioniNeed", associazioniRepository.findByNeed_Id(idNeed));
            model.addAttribute("listCandidatiAssociati", candidatoRepository.findCandidatiAssociati(idNeed));
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            return "liste_match_need";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/associa/staffing/{idNeed}/{idCandidato}")
    public String showAssociaCandidatiForm(
        @PathVariable("idNeed") Integer idNeed,
        @PathVariable("idCandidato") Integer idCandidato,
        Model model,
        RedirectAttributes ra
    ){
        try {
            AssociazioneCandidatoNeed associazione = new AssociazioneCandidatoNeed();
            Candidato                 candidato    = candidatoRepository.findById(idCandidato).get();
            long                      millis       = System.currentTimeMillis();
            Need                      need         = needRepository.findById(idNeed).get();
            StatoA                    statoa       = new StatoA();

            statoa.setId(1);
            statoa.setDescrizione("Pool");
            associazione.setCandidato(candidato);
            associazione.setNeed(need);
            associazione.setStato(statoa);
            associazione.setDataModifica(new Date(millis));
            associazione.setOwner(need.getOwner());

            associazioniRepository.save(associazione);

            candidato.getNeeds().add(need);
            candidatoRepository.save(candidato);

            model.addAttribute("need", need);
            model.addAttribute("titoloPagina", "Match del need");
            model.addAttribute("listCandidatiNonAssociati", candidatoRepository.findCandidatiNonAssociati(idNeed));
            model.addAttribute("listAssociazioniNeed", associazioniRepository.findByNeed_Id(idNeed));
            model.addAttribute("listCandidatiAssociati", candidatoRepository.findCandidatiAssociati(idNeed));
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("candidatoRicerca", new Candidato());

            return "redirect:/need/match/" + idNeed;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/rimuovi/staffing/{idNeed}/{idCandidato}")
    public String showRimuoviCandidatiForm(
        @PathVariable("idNeed") Integer idNeed,
        @PathVariable("idCandidato") Integer idCandidato,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Need      need      = needRepository.findById(idNeed).get();
            Candidato candidato = candidatoRepository.findById(idCandidato).get();

            candidato.getNeeds().remove(need);
            need.getCandidati().remove(candidato);

            candidatoRepository.save(candidato);
            needRepository.save(need);

            model.addAttribute("need", need);
            model.addAttribute("titoloPagina", "Match del need");
            model.addAttribute("listCandidatiNonAssociati", candidatoRepository.findCandidatiNonAssociati(idNeed));
            model.addAttribute("listCandidatiAssociati", candidatoRepository.findCandidatiAssociati(idNeed));
            model.addAttribute("listAssociazioniNeed", associazioniRepository.findByNeed_Id(idNeed));
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("candidatoRicerca", new Candidato());

            return "liste_match_need";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/associazioni/{idNeed}")
    public String showNeedAssociazioniList(
        @PathVariable("idNeed") Integer idNeed,
        Model model
    ) {
        try {
            Need                            need             = needRepository.findById(idNeed).get();
            List<AssociazioneCandidatoNeed> listAssociazioni = associazioniRepository.findByNeed_Id(idNeed);

            model.addAttribute("listAssociazioni", listAssociazioni);
            model.addAttribute("need", need);

            return "lista_associazioni_need";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }
}