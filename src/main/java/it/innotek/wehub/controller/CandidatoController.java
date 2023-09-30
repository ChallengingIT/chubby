/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.AssociazioneCandidatoNeed;
import it.innotek.wehub.entity.Candidato;
import it.innotek.wehub.entity.File;
import it.innotek.wehub.entity.TipologiaF;
import it.innotek.wehub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/staffing")
public class CandidatoController {

    @Autowired
    private CandidatoRepository     candidatoRepository;
    @Autowired
    private TipologiaRepository     tipologiaRepository;
    @Autowired
    private FacoltaRepository       facoltaRepository;
    @Autowired
    private StatoCRepository        statoCRepository;
    @Autowired
    private LivelloRepository       livelloRepository;
    @Autowired
    private SkillRepository         skillRepository;
    @Autowired
    private FileRepository          fileRepository;
    @Autowired
    private FileCandidatoRepository fileCandidatoRepository;
    @Autowired
    private OwnerRepository         ownerRepository;
    @Autowired
    private TipoRepository          tipoRepository;
    @Autowired
    private FornitoreRepository     fornitoreRepository;
    @Autowired
    private AssociazioniRepository  associazioniRepository;

    private static final Logger logger = LoggerFactory.getLogger(CandidatoController.class);

    @RequestMapping
    public String showCandidatiList(Model model){
        try {
            Pageable        limit         = PageRequest.of(0, 60);
            List<Candidato> listCandidati = candidatoRepository.findAll(limit).toList();
            List<Integer>   listId        = new ArrayList<>();

            for (Candidato cand : listCandidati) {
                listId.add(cand.getId());
            }

            fileCandidatoRepository.elimina_file_vecchi(listId.toString(), 1);
            fileCandidatoRepository.elimina_file_vecchi(listId.toString(), 2);

            listCandidati = candidatoRepository.findAll(limit).toList();

            model.addAttribute("listCandidati", listCandidati);
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("candidatoRicerca", new Candidato());
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());
            return "candidati";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ricerca")
    public String showRicercaCandidatiList(
        Model model,
        Candidato candidato
    ){
        try {
            Integer idTipologia = candidato.getTipologia() != null ? candidato.getTipologia().getId() : null;
            Integer idStato     = candidato.getStato() != null ? candidato.getStato().getId() : null;
            Integer idTipo      = candidato.getTipo() != null ? candidato.getTipo().getId() : null;
            String  nome        = ( candidato.getNome() != null && !candidato.getNome().isEmpty() ) ? candidato.getNome() : null;
            String  cognome     = ( candidato.getCognome() != null && !candidato.getCognome().isEmpty() ) ? candidato.getCognome() : null;
            String  email       = ( candidato.getEmail() != null && !candidato.getEmail().isEmpty() ) ? candidato.getEmail() : null;

            List<Candidato> listCandidati = candidatoRepository.ricercaByNomeAndCognomeAndEmailAndTipologia_IdAndStato_IdAndTipo_Id(nome, cognome, email, idTipologia, idStato, idTipo);
            List<Integer>   listId        = new ArrayList<>();

            for (Candidato cand : listCandidati) {
                listId.add(cand.getId());
            }

            fileCandidatoRepository.elimina_file_vecchi(listId.toString(), 1);
            fileCandidatoRepository.elimina_file_vecchi(listId.toString(), 2);

            listCandidati = candidatoRepository.ricercaByNomeAndCognomeAndEmailAndTipologia_IdAndStato_IdAndTipo_Id(nome, cognome, email, idTipologia, idStato, idTipo);

            model.addAttribute("listCandidati", listCandidati);
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("candidatoRicerca", candidato);
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());

            return "candidati";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/aggiungi")
    public String showNewForm(
        Model model,
        RedirectAttributes ra
    ){
        try {
            if (null != model.getAttribute("candidato")) {
                model.addAttribute("candidato", model.getAttribute("candidato"));
            } else {
                model.addAttribute("candidato", new Candidato());
            }

            model.addAttribute("titoloPagina", "Aggiungi candidato");
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());
            model.addAttribute("listOwner", ownerRepository.findAll());

            return "candidato_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/salva")
    public String saveCandidato(
        @RequestParam("file") MultipartFile file,
        @RequestParam("cf") MultipartFile cf,
        Candidato candidato,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            if (controllaMailDuplicata(candidato.getEmail())) {

                ra.addFlashAttribute("message", "Email già associata ad un altro profilo");

                if (candidato.getId() == null) {
                    ra.addFlashAttribute(candidato);
                    return "redirect:/staffing/aggiungi";
                }
            }

            String descrizioneTipologia = recuperaDescrizioneTipologia(candidato.getTipologia().getId());
            String descrizioneLivello   = recuperaDescrizioneLivello(candidato.getLivelloScolastico().getId());

            candidato.getTipologia().setDescrizione(descrizioneTipologia);
            candidato.getLivelloScolastico().setDescrizione(descrizioneLivello);

            if (null == candidato.getRating()) {
                candidato.setRating(0.0);
            }

            if (null == candidato.getFornitore()) {
                candidato.setFornitore(null);
            }

            if (null != candidato.getFacolta()) {
                if (null == candidato.getFacolta().getId()) {
                    candidato.setFacolta(null);
                }
            }

            candidatoRepository.save(candidato);

            boolean modifica = false;
            Integer idCandidato;

            if (null != candidato.getId()) {
                modifica = true;
            }

            if (modifica) {
                idCandidato = candidato.getId();
            } else {
                idCandidato = candidatoRepository.findMaxId().intValue();
            }

            if (( null != file.getOriginalFilename() ) && !file.getOriginalFilename().isEmpty()) {

                uploadFileVoid(file, idCandidato, 1, ra);
            }
            if (( null != cf.getOriginalFilename() ) && !cf.getOriginalFilename().isEmpty()) {

                uploadFileVoid(cf, idCandidato, 2, ra);
            }

            ra.addFlashAttribute("message", "Il candidato è stato salvato con successo");
            return "redirect:/intervista/" + idCandidato;

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/visualizza/{id}")
    public String showForm(
        @PathVariable("id") Integer id,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            Candidato candidato = candidatoRepository.findById(id).get();

            model.addAttribute("candidato", candidato);
            model.addAttribute("titoloPagina", "Modifica staffing");
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());

            return "candidato_visualizza_form";
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
    ) {
        try {
            Candidato candidato = candidatoRepository.findById(id).get();

            model.addAttribute("candidato", candidato);
            model.addAttribute("titoloPagina", "Modifica staffing");
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());
            model.addAttribute("listOwner", ownerRepository.findAll());
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());

            return "candidato_form";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/modifica/noskills/{id}")
    public String showEditFormNoSkills(
        @PathVariable("id") Integer id,
        Candidato candidatoInput,
        Model model,
        RedirectAttributes ra
    ) {
        try {
            Candidato candidato = candidatoRepository.findById(id).get();

            candidato.setSkills(null);

            model.addAttribute("candidato", candidato);
            model.addAttribute("titoloPagina", "Modifica staffing");
            model.addAttribute("listaTipologie", tipologiaRepository.findAll());
            model.addAttribute("listaTipi", tipoRepository.findAll());
            model.addAttribute("listaStatiC", statoCRepository.findAllByOrderByIdAsc());
            model.addAttribute("listaLivelliScolastici", livelloRepository.findAll());
            model.addAttribute("listaSkillOrdinata", skillRepository.findAll());
            model.addAttribute("listaFacolta", facoltaRepository.findAll());
            model.addAttribute("listOwner", ownerRepository.findAll());
            model.addAttribute("listaFornitori", fornitoreRepository.findAll());

            return "candidato_form";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/elimina/{id}")
    public String deleteCandidato(
        @PathVariable("id") Integer id,
        RedirectAttributes ra
    ){
        try {
            Candidato candidato = candidatoRepository.findById(id).get();

            for (AssociazioneCandidatoNeed associazione : candidato.getAssociazioni()) {
                associazioniRepository.deleteById(associazione.getId());
            }

            candidatoRepository.deleteById(id);
            ra.addFlashAttribute("message", "Il candidato è stato cancellato con successo");

            return "redirect:/staffing";

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }

    @RequestMapping("/ottieni/{id}")
    public void getCandidato(
        @PathVariable("id") Integer id,
        Model model
    ) {
        try {
            model.addAttribute("candidatoToUse", candidatoRepository.findById(id).get());
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public void uploadFileVoid(
        MultipartFile file,
        Integer idCandidato,
        Integer tipoFile,
        RedirectAttributes ra
    ) {
        try {
            String         descrizione          = file.getOriginalFilename();
            byte[]         arrayByte            = file.getBytes();
            String         tipo                 = file.getContentType();
            File           fileOggettoCandidato = new File();
            java.util.Date date                 = new java.util.Date();
            java.sql.Date  sqlDate              = new Date(date.getTime());
            TipologiaF     tipologia            = new TipologiaF();
            Candidato      candidato            = new Candidato();

            fileOggettoCandidato.setData(arrayByte);
            fileOggettoCandidato.setDataInserimento(new java.sql.Date(sqlDate.getTime()));
            fileOggettoCandidato.setDescrizione(descrizione);
            fileOggettoCandidato.setTipo(tipo);

            tipologia.setId(tipoFile);
            fileOggettoCandidato.setTipologia(tipologia);

            candidato.setId(idCandidato);
            fileOggettoCandidato.setCandidato(candidato);

            fileRepository.save(fileOggettoCandidato);

        } catch (Exception e) {
            ra.addFlashAttribute("message", e.getMessage() );
        }
    }

    public String recuperaDescrizioneTipologia(Integer id) {
        return tipologiaRepository.findById(id).get().getDescrizione();
    }

    public String recuperaDescrizioneLivello(Integer id) {
        return livelloRepository.findById(id).get().getDescrizione();
    }

    public boolean controllaMailDuplicata(String email) {
        List<Candidato> candidati = candidatoRepository.findByEmail(email);
        return ((null != candidati) && !candidati.isEmpty());
    }
}