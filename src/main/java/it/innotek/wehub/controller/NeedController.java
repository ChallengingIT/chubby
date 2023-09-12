/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.*;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.*;
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
    private CandidatoService    serviceCandidato;
    @Autowired
    private ClienteService      serviceCliente;
    @Autowired
    private TipologiaNService   serviceTipologiaN;
    @Autowired
    private StatoNService       serviceStatoN;
    @Autowired
    private OwnerService        serviceOwner;
    @Autowired
    private LivelloService      serviceLivelli;
    @Autowired
    private SkillService        serviceSkill;
    @Autowired
    private NeedService         serviceNeed;
    @Autowired
    private TipologiaService    serviceTipologia;
    @Autowired
    private StatoCService       serviceStatoC;
    @Autowired
    private TipoService         serviceTipo;
    @Autowired
    private AssociazioniService serviceAssociazione;

    @RequestMapping
    public String showNeedList(Model model){

        List<Need> listNeed = serviceNeed.listAll();

        for (Need need : listNeed) {
            if (need.getCliente() != null) {
                Optional<Cliente> cliente = serviceCliente.getById(need.getCliente().getId());
                cliente.ifPresent(need::setCliente);
            }
        }

        model.addAttribute("listNeed",        listNeed);
        model.addAttribute("needRicerca",     new Need());
        model.addAttribute("listaTipologieN", serviceTipologiaN.listAll());
        model.addAttribute("listaOwner",      serviceOwner.listAll());
        model.addAttribute("listaAziende",    serviceCliente.listAll());
        model.addAttribute("listaStatiN",     serviceStatoN.listAll());

        return "lista_need";
    }

    @RequestMapping("/ricerca")
    public String showRicercaList(
        Model model,
        Need need
    ) throws ElementoNonTrovatoException {

        Integer    idCliente   = (null != need.getCliente())                             ? need.getCliente().getId()   : null;
        Integer    idStato     = (null != need.getStato())                               ? need.getStato().getId()     : null;
        Integer    idTipologia = (null != need.getTipologia())                           ? need.getTipologia().getId() : null;
        Integer    idOwner     = (null != need.getOwner())                               ? need.getOwner().getId()     : null;
        String     settimana   = ((null != need.getWeek()) && !need.getWeek().isEmpty()) ? need.getWeek()              : null;
        List<Need> listNeed    = serviceNeed.listRicerca(idCliente, idStato, need.getPriorita(), idTipologia, settimana, idOwner);

        for(Need needFor : listNeed){
            Cliente cliente = serviceCliente.get(needFor.getCliente().getId());
            needFor.setCliente(cliente);
        }

        model.addAttribute("listNeed",        listNeed);
        model.addAttribute("needRicerca",     need);
        model.addAttribute("listaTipologieN", serviceTipologiaN.listAll());
        model.addAttribute("listaAziende",    serviceCliente.listAll());
        model.addAttribute("listaOwner",      serviceOwner.listAll());
        model.addAttribute("listaStatiN",     serviceStatoN.listAll());

        return "lista_need";
    }

    @RequestMapping("/{idCliente}")
    public String showNeedIdList(
        @PathVariable("idCliente") Integer idCliente,
        Model model
    ) throws ElementoNonTrovatoException {

        Cliente    cliente  = serviceCliente.get(idCliente);
        List<Need> listNeed = serviceNeed.listAllByCLiente(idCliente);

        model.addAttribute("listNeed",        listNeed);
        model.addAttribute("cliente",         cliente);
        model.addAttribute("needRicerca",     new Need());
        model.addAttribute("listaTipologieN", serviceTipologiaN.listAll());
        model.addAttribute("listaOwner",      serviceOwner.listAll());
        model.addAttribute("listaStatiN",     serviceStatoN.listAll());

        return "lista_need_cliente";
    }

    @RequestMapping("/ricerca/{idCliente}")
    public String showRicercaList(
        @PathVariable("idCliente") Integer idCliente,
        Model model,
        Need need
    ) throws ElementoNonTrovatoException {

        Cliente    cliente     = serviceCliente.get(idCliente);
        Integer    idStato     = (null != need.getStato())                               ? need.getStato().getId()     : null;
        Integer    idTipologia = (null != need.getTipologia())                           ? need.getTipologia().getId() : null;
        String     settimana   = ((null != need.getWeek()) && !need.getWeek().isEmpty()) ? need.getWeek()              : null;
        Integer    idOwner     = (null != need.getOwner())                               ? need.getOwner().getId()     : null;
        List<Need> listNeed    = serviceNeed.listRicerca(idCliente,idStato, need.getPriorita(), idTipologia, settimana, idOwner);

        model.addAttribute("cliente",         cliente);
        model.addAttribute("listNeed",        listNeed);
        model.addAttribute("needRicerca",     need);
        model.addAttribute("listaTipologieN", serviceTipologiaN.listAll());
        model.addAttribute("listaOwner",      serviceOwner.listAll());
        model.addAttribute("listaStatiN",     serviceStatoN.listAll());

        return "lista_need_cliente";
    }


    @RequestMapping("/aggiungi/{idCliente}")
    public String showNewForm(
        @PathVariable("idCliente") Integer idCliente,
        Model model
    ){
        model.addAttribute("need",                   new Need());
        model.addAttribute("idCliente",              idCliente);
        model.addAttribute("titoloPagina",           "Aggiungi un nuovo need");
        model.addAttribute("listaLivelliScolastici", serviceLivelli.listAll());
        model.addAttribute("listaSkillOrdinata",     serviceSkill.listAll());
        model.addAttribute("listaTipologieN",        serviceTipologiaN.listAll());
        model.addAttribute("listaOwner",             serviceOwner.listAll());
        model.addAttribute("listaStatiN",            serviceStatoN.listAll());

        return "need_cliente_form";
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(Model model){

        model.addAttribute("need",                   new Need());
        model.addAttribute("titoloPagina",           "Aggiungi un nuovo need");
        model.addAttribute("listaLivelliScolastici", serviceLivelli.listAll());
        model.addAttribute("listaSkillOrdinata",     serviceSkill.listAll());
        model.addAttribute("listaTipologieN",        serviceTipologiaN.listAll());
        model.addAttribute("listaOwner",             serviceOwner.listAll());
        model.addAttribute("listaStatiN",            serviceStatoN.listAll());
        model.addAttribute("listaAziende",           serviceCliente.listAll());

        return "need_form";
    }

    @RequestMapping("/salva")
    public String saveNeed(
        Need need,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Cliente                         cliente            = new Cliente();
        List<AssociazioneCandidatoNeed> associazioni       = serviceAssociazione.listAllByNeed(need.getId());
        List<Candidato>                 candidati          = serviceCandidato.listAllByNeed(need.getId());
        String                          descrizioneLivello = recuperaDescrizioneLivello(need.getLivelloScolastico().getId());

        cliente.setId(need.getCliente().getId());

        need.getLivelloScolastico().setDescrizione(descrizioneLivello);
        need.setCliente(cliente);
        need.setAssociazioni(associazioni);
        need.setCandidati(candidati);

        serviceNeed.save(need);
        ra.addFlashAttribute("message", "Il need è stato salvato con successo");
        return "redirect:/need";
    }

    @RequestMapping("/salva/{idCliente}")
    public String saveNeed(
        @PathVariable("idCliente") Integer idCliente,
        Need need,
        RedirectAttributes ra
    ) throws ElementoNonTrovatoException {

        Cliente                         cliente            = new Cliente();
        List<AssociazioneCandidatoNeed> associazioni       = serviceAssociazione.listAllByNeed(need.getId());
        List<Candidato>                 candidati          = serviceCandidato.listAllByNeed(need.getId());
        String                          descrizioneLivello = recuperaDescrizioneLivello(need.getLivelloScolastico().getId());

        cliente.setId(idCliente);
        need.getLivelloScolastico().setDescrizione(descrizioneLivello);
        need.setCliente(cliente);
        need.setAssociazioni(associazioni);
        need.setCandidati(candidati);

        serviceNeed.save(need);
        ra.addFlashAttribute("message", "Il need è stato salvato con successo");
        return "redirect:/need/"+idCliente;
    }

    @RequestMapping("/modifica/{idCliente}/{id}")
    public String showEditForm(
        @PathVariable("idCliente") Integer idCliente,
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Need need = serviceNeed.get(id);

            model.addAttribute("need",                   need);
            model.addAttribute("idCliente",              idCliente);
            model.addAttribute("titoloPagina",           "Modifica need");
            model.addAttribute("listaLivelliScolastici", serviceLivelli.listAll());
            model.addAttribute("listaSkillOrdinata",     serviceSkill.listAll());
            model.addAttribute("listaTipologieN",        serviceTipologiaN.listAll());
            model.addAttribute("listaOwner",             serviceOwner.listAll());
            model.addAttribute("listaStatiN",            serviceStatoN.listAll());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/need/"+idCliente;
        }
        return "need_cliente_form";
    }

    @RequestMapping("/elimina/{idCliente}/{id}")
    public String deleteNeed(@PathVariable("idCliente") Integer idCliente, @PathVariable("id") Integer id, RedirectAttributes ra){

        try {
            Need need = serviceNeed.get(id);

            for (AssociazioneCandidatoNeed associazione: need.getAssociazioni()) {
                serviceAssociazione.delete(associazione.getId());
            }

            serviceNeed.delete(id);
            ra.addFlashAttribute("message", "Il need è stato cancellato con successo");

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/need/"+idCliente;
    }

    @RequestMapping("/match/{idNeed}")
    public String showMatchForm(
        @PathVariable("idNeed") Integer idNeed,
        Model model
    ) throws ElementoNonTrovatoException {

        model.addAttribute("need",                      serviceNeed.get(idNeed));
        model.addAttribute("titoloPagina",              "Match del need");
        model.addAttribute("listCandidatiNonAssociati", serviceCandidato.getCandidatiNonAssociati(idNeed));
        model.addAttribute("listCandidatiAssociati",    serviceCandidato.getCandidatiAssociati(idNeed));
        model.addAttribute("listAssociazioniNeed",      serviceAssociazione.listAllByNeed(idNeed));
        model.addAttribute("listaTipologie",            serviceTipologia.listAll());
        model.addAttribute("listaTipi",                 serviceTipo.listAll());
        model.addAttribute("listaStatiC",               serviceStatoC.listAllOrdered());
        model.addAttribute("candidatoRicerca",          new Candidato());

        return "liste_match_need";
    }

    @RequestMapping("/ricerca/match/{idNeed}")
    public String showRicercaMatchForm(
        @PathVariable("idNeed") Integer idNeed,
        Model model,
        Candidato candidato
    ) throws ElementoNonTrovatoException {

        Integer idTipologia         = (null != candidato.getTipologia())                                      ? candidato.getTipologia().getId()   : null;
        Integer idTipo              = (null != candidato.getTipo())                                           ? candidato.getTipo().getId()        : null;
        String  nome                = ((null != candidato.getNome()) && !candidato.getNome().isEmpty())       ? candidato.getNome()                : null;
        String  cognome             = ((null != candidato.getCognome()) && !candidato.getCognome().isEmpty()) ? candidato.getCognome()             : null;
        Double  anniEsperienzaRuolo = (null != candidato.getAnniEsperienzaRuolo())                            ? candidato.getAnniEsperienzaRuolo() : null;
        Integer anniMinimi          = null;
        Integer anniMassimi         = null;

        if (null != anniEsperienzaRuolo) {
            if (anniEsperienzaRuolo == 0) {
                anniMassimi = 1;
            } else if (anniEsperienzaRuolo == 1) {
                anniMinimi = 1;
                anniMassimi = 2;
            } else if (anniEsperienzaRuolo == 2) {
                anniMinimi  = 2;
                anniMassimi = 5;
            } else {
                anniMinimi  = 5;
            }
        }

        model.addAttribute(
            "listCandidatiNonAssociati",
            serviceCandidato.getRicercaCandidatiNonAssociati(
                idNeed,
                nome,
                cognome,
                idTipologia,
                idTipo,
                anniMinimi,
                anniMassimi
            )
        );

        model.addAttribute("need",                   serviceNeed.get(idNeed));
        model.addAttribute("titoloPagina",           "Match del need");
        model.addAttribute("candidatoRicerca",       candidato);
        model.addAttribute("listAssociazioniNeed",   serviceAssociazione.listAllByNeed(idNeed));
        model.addAttribute("listCandidatiAssociati", serviceCandidato.getCandidatiAssociati(idNeed));
        model.addAttribute("listaTipologie",         serviceTipologia.listAll());
        model.addAttribute("listaTipi",              serviceTipo.listAll());
        model.addAttribute("listaStatiC",            serviceStatoC.listAllOrdered());
        return "liste_match_need";
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
            Candidato                 candidato    = serviceCandidato.get(idCandidato);
            long                      millis       = System.currentTimeMillis();
            Need                      need         = serviceNeed.get(idNeed);
            StatoA                    statoa       = new StatoA();

            statoa.setId(1);
            statoa.setDescrizione("Pool");
            associazione.setCandidato(candidato);
            associazione.setNeed(need);
            associazione.setStato(statoa);
            associazione.setDataModifica(new Date(millis));

            serviceAssociazione.save(associazione);

            candidato.getNeeds().add(need);
            serviceCandidato.save(candidato);

            model.addAttribute("need",                      need);
            model.addAttribute("titoloPagina",              "Match del need");
            model.addAttribute("listCandidatiNonAssociati", serviceCandidato.getCandidatiNonAssociati(idNeed));
            model.addAttribute("listAssociazioniNeed",      serviceAssociazione.listAllByNeed(idNeed));
            model.addAttribute("listCandidatiAssociati",    serviceCandidato.getCandidatiAssociati(idNeed));
            model.addAttribute("listaTipologie",            serviceTipologia.listAll());
            model.addAttribute("listaTipi",                 serviceTipo.listAll());
            model.addAttribute("listaStatiC",               serviceStatoC.listAllOrdered());
            model.addAttribute("candidatoRicerca",          new Candidato());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/need/match/"+idNeed;
        }
        return "redirect:/need/match/"+idNeed;
    }

    @RequestMapping("/rimuovi/staffing/{idNeed}/{idCandidato}")
    public String showRimuoviCandidatiForm(
        @PathVariable("idNeed") Integer idNeed,
        @PathVariable("idCandidato") Integer idCandidato,
        Model model,
        RedirectAttributes ra
    ){
        try {
            Need      need      = serviceNeed.get(idNeed);
            Candidato candidato = serviceCandidato.get(idCandidato);

            candidato.getNeeds().remove(need);
            need.getCandidati().remove(candidato);

            serviceCandidato.save(candidato);
            serviceNeed.save(need);

            model.addAttribute("need",                      need);
            model.addAttribute("titoloPagina",              "Match del need");
            model.addAttribute("listCandidatiNonAssociati", serviceCandidato.getCandidatiNonAssociati(idNeed));
            model.addAttribute("listCandidatiAssociati",    serviceCandidato.getCandidatiAssociati(idNeed));
            model.addAttribute("listAssociazioniNeed",      serviceAssociazione.listAllByNeed(idNeed));
            model.addAttribute("listaTipologie",            serviceTipologia.listAll());
            model.addAttribute("listaTipi",                 serviceTipo.listAll());
            model.addAttribute("listaStatiC",               serviceStatoC.listAllOrdered());
            model.addAttribute("candidatoRicerca",          new Candidato());

        } catch (ElementoNonTrovatoException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/need/match/"+idNeed;
        }
        return "liste_match_need";
    }

    public String recuperaDescrizioneLivello(Integer id) throws ElementoNonTrovatoException {
        return serviceLivelli.get(id).getDescrizione();
    }

    @RequestMapping("/associazioni/{idNeed}")
    public String showNeedAssociazioniList(
        @PathVariable("idNeed") Integer idNeed,
        Model model
    ) throws ElementoNonTrovatoException {

        Need                            need             = serviceNeed.get(idNeed);
        List<AssociazioneCandidatoNeed> listAssociazioni = serviceAssociazione.listAllByNeed(idNeed);

        model.addAttribute("listAssociazioni", listAssociazioni);
        model.addAttribute("need",             need);

        return "lista_associazioni_need";
    }
}