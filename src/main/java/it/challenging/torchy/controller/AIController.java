/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.repository.*;
import it.challenging.torchy.util.Constants;
import it.challenging.torchy.util.UtilLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ai")
public class AIController {

    private static final Logger logger = LoggerFactory.getLogger(AIController.class);
    private static final ArrayList<String> keyOrder = new ArrayList<>();
    private static final ArrayList<String> keyLimit = new ArrayList<>();
    private static final LinkedHashMap<ArrayList<String>, ArrayList<String>> dictionary = new LinkedHashMap<>();
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private CandidatoRepository candidatoRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private TipologiaRepository tipologiaRepository;
    @Autowired
    private FacoltaRepository facoltaRepository;
    @Autowired
    private LivelloRepository livelloRepository;
    @Autowired
    private TipoCandidaturaRepository tipoCandidaturaRepository;
    @Autowired
    private TipoRicercaRepository tipoRicercaRepository;
    @Autowired
    private TipoRepository tipoRepository;

    @GetMapping
    public ResponseEntity<?> findById(
            @RequestParam("message") String message
    ) {
        logger.info("AI Message");
        boolean ricerca = false;
        boolean ordina = false;
        boolean candidati = false;
        boolean aziende = false;
        boolean need = false;
        boolean keyPeople = false;
        boolean almeno = message.contains(Constants.ALMENO) || message.contains(Constants.PIU);
        boolean massimo = message.contains(Constants.MENO) || message.contains(Constants.MASSIMO);
        StringBuilder where = new StringBuilder();

        UtilLib.caricaDictionary(dictionary);
        UtilLib.caricaKeyOrder(keyOrder);
        UtilLib.caricaKeyLimit(keyLimit);
        ArrayList<String> keyCandidati = UtilLib.getElementByIndex(dictionary, 1);
        ArrayList<String> keyNeed      = UtilLib.getElementByIndex(dictionary, 3);
        ArrayList<String> keyAziende   = UtilLib.getElementByIndex(dictionary, 0);
        ArrayList<String> keyKeyPeople = UtilLib.getElementByIndex(dictionary, 2);

        for (ArrayList<String> s : dictionary.keySet()) {
            if (UtilLib.findInArray(message, s)) {
                switch (s.get(0)) {
                    case "candidati":
                        candidati = true;
                        break;
                    case "aziende":
                        aziende = true;
                        break;
                    case "keyPeople":
                        keyPeople = true;
                        break;
                    case "need":
                        need = true;
                        break;
                    default:
                        break;
                }
            }
        }

        if (UtilLib.findInArray(message, keyOrder)) {
            ordina = true;
        }

        if (candidati && need) {

        }
        else if (aziende && need) {

        }
        else if (keyPeople && need) {

        }
        else if (keyPeople && aziende) {

        }
        else if (candidati) {

            for (String s : keyCandidati) {
                if (message.toLowerCase().contains(s)) {


                    switch (s) {
                        case Constants.CANDIDATI_NOME, Constants.CANDIDATI_CHIAMANO:
                            String nome = UtilLib.cercaStringhe(message, candidatoRepository.findAllNames());

                            if (null == nome) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_NOME);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_NOME)
                                    .append(nome);
                            }

                            break;

                        case Constants.CANDIDATI_COGNOME:
                            String cognome = UtilLib.cercaStringhe(message, candidatoRepository.findAllSurnames());

                            if (null == cognome) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_COGNOME);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_COGNOME)
                                    .append(cognome);
                            }

                            break;

                        case Constants.CANDIDATI_ANNI_DI_ESPERIENZA, Constants.CANDIDATI_ESPERIENZA:
                            String anniEsperienza = UtilLib.findIntegers(message);

                            if (null != anniEsperienza) {
                                if (almeno) {
                                    where
                                        .append(Constants.CANDIDATI_WHERE_ESP_MAG)
                                        .append(anniEsperienza);
                                } else if (massimo) {
                                    where
                                        .append(Constants.CANDIDATI_WHERE_ESP_MIN)
                                        .append(anniEsperienza);
                                } else {
                                    where
                                        .append(Constants.CANDIDATI_WHERE_ESP_EQ)
                                        .append(anniEsperienza);
                                }
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_ANNI_ESPERIENZA);
                            }

                            break;

                        case Constants.CANDIDATI_CELLULARE, Constants.CANDIDATI_TELEFONINO,
                             Constants.CANDIDATI_TELEFONO:
                            String cellulare = UtilLib.findIntegers(message);

                            if (null != cellulare) {
                                where
                                    .append(Constants.CANDIDATI_WHERE_CELLULARE)
                                    .append(cellulare);
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_CELLULARE);
                            }

                            break;

                        case Constants.CANDIDATI_CITTA, Constants.CANDIDATI_ABITA, Constants.CANDIDATI_VIVE:
                            String citta = UtilLib.cercaStringhe(message, candidatoRepository.findAllCity());

                            if (null == citta) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_CITTA);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_CITTA)
                                    .append(citta);
                            }

                            break;

                        case Constants.CANDIDATI_CONOSCE, Constants.CANDIDATI_SKILL, Constants.CANDIDATI_SKILLATO:
                            String idsSkill = UtilLib.cercaSkills(message, skillRepository.findAll());

                            if (null == idsSkill) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_SKILL);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_SKILL)
                                    .append(idsSkill)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        //TODO
                        case Constants.CANDIDATI_DATA_DI_NASCITA, Constants.CANDIDATI_NATO, Constants.CANDIDATI_ANNI:
                            String anni = UtilLib.findIntegers(message);

                            if (null != anni) {
                                //return ResponseEntity.ok(candidatoRepository.findByAnni(anni));
                                return null;
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_CELLULARE);
                            }


                        case Constants.CANDIDATI_HA_STUDIATO, Constants.CANDIDATI_FACOLTA:
                            String idFacolta = UtilLib.cercaFacolta(message, facoltaRepository.findAll());

                            if (null == idFacolta) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_FACOLTA);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_FACOLTA)
                                    .append(idFacolta)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.CANDIDATI_EMAIL:
                            String email = UtilLib.cercaStringhe(message, candidatoRepository.findAllEmail());

                            if (null == email) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_EMAIL);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_EMAIL)
                                    .append(email);
                            }

                            break;

                        case Constants.CANDIDATI_JOB_TITLE:
                            String idJobTitle = UtilLib.cercaJobTitle(message, tipologiaRepository.findAll());

                            if (null == idJobTitle) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_JOB_TITLE);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_JOB_TITLE)
                                    .append(idJobTitle)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.CANDIDATI_DISPONIBILITA, Constants.CANDIDATI_DISPONIBILE:
                            String disponibilita = UtilLib.cercaStringhe(message, candidatoRepository.findAllDisponibilita());

                            if (null == disponibilita) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_DISPONIBILITA);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_DISPONIBILITA)
                                    .append(disponibilita);
                            }

                            break;

                        case Constants.CANDIDATI_OWNER, Constants.CANDIDATI_PROPRIETARIO:
                            String idOwner = UtilLib.cercaOwner(message, ownerRepository.findAll());

                            if (null == idOwner) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_OWNER);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_OWNER)
                                    .append(idOwner)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.CANDIDATI_LIVELLO_SCOLASTICO, Constants.CANDIDATI_TITOLO,
                             Constants.CANDIDATI_TITOLO_DI_STUDIO:
                            String idLivelli = UtilLib.cercaLivello(message, livelloRepository.findAll());

                            if (null == idLivelli) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_LIVELLO);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_LIVELLI)
                                    .append(idLivelli)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.CANDIDATI_TIPO_RICERCA:
                            String idTipoRicerca = UtilLib.cercaTipoRicerca(message, tipoRicercaRepository.findAll());

                            if (null == idTipoRicerca) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_TIPO_RICERCA);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_TIPO_RICERCA)
                                    .append(idTipoRicerca)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.CANDIDATI_TIPO_CANDIDATURA:
                            String idTipoCandidatura = UtilLib.cercaTipoCandidatura(message, tipoCandidaturaRepository.findAll());

                            if (null == idTipoCandidatura) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_TIPO_CANDIDATURA);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_TIPO_CANDI)
                                    .append(idTipoCandidatura)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.CANDIDATI_TIPO, Constants.CANDIDATI_TIPOLOGIA:
                            String idTipo = UtilLib.cercaTipo(message, tipoRepository.findAll());

                            if (null == idTipo) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_TIPO);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_TIPO)
                                    .append(idTipo)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.CANDIDATI_MODALITA:
                            String modalita = UtilLib.cercaStringhe(message, candidatoRepository.findAllModalita());

                            if (null == modalita) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_MODALITA);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_MODALITA)
                                    .append(modalita);
                            }

                            break;

                        case Constants.CANDIDATI_RAL:
                            String ral = UtilLib.cercaStringhe(message, candidatoRepository.findAllRal());

                            if (null == ral) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_RAL);
                            } else {
                                where
                                    .append(Constants.CANDIDATI_WHERE_RAL)
                                    .append(ral);
                            }

                            break;

                        case Constants.CANDIDATI_RATING:
                            String rating = UtilLib.findDoubles(message);

                            if (null != rating) {
                                if (almeno) {
                                    where
                                        .append(Constants.CANDIDATI_WHERE_RATING_MAG)
                                        .append(rating);
                                } else if (massimo) {
                                    where
                                        .append(Constants.CANDIDATI_WHERE_RATING_MIN)
                                        .append(rating);
                                } else {
                                    where
                                        .append(Constants.CANDIDATI_WHERE_RATING_EQ)
                                        .append(rating);
                                }
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_CANDIDATO_RATING);
                            }

                            break;
                    }
                }
            }

            //TODO
            if (ordina) {

            }

            return ResponseEntity.ok(candidatoRepository.findByWhere(where.toString()));

        }
        /*else if (need) {
            for (String s : keyNeed) {
                if (message.toLowerCase().contains(s)) {


                    switch (s) {
                        case Constants.NEED_DESCRIZIONE, Constants.NEED_CHIAMATO:
                            String descrizione = UtilLib.cercaStringheLike(message, needRepository.findAllDescriptions());

                            if (null == descrizione) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_DESCRIZIONE);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_DESCRIZIONE)
                                    .append(descrizione);
                            }

                            break;

                        case Constants.NEED_RISORSE:
                            String risorse = UtilLib.findIntegers(message);

                            if (null != risorse) {
                                if (almeno) {
                                    where
                                        .append(Constants.NEED_WHERE_RISORSE_MAG)
                                        .append(risorse);
                                } else if (massimo) {
                                    where
                                        .append(Constants.NEED_WHERE_RISORSE_MIN)
                                        .append(risorse);
                                } else {
                                    where
                                        .append(Constants.NEED_WHERE_RISORSE_EQ)
                                        .append(risorse);
                                }
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_RISORSE);
                            }

                            break;

                        case Constants.NEED_ANNI_DI_ESPERIENZA, Constants.NEED_ESPERIENZA:
                            String anniEsperienza = UtilLib.findIntegers(message);

                            if (null != anniEsperienza) {
                                if (almeno) {
                                    where
                                        .append(Constants.NEED_WHERE_ESP_MAG)
                                        .append(anniEsperienza);
                                } else if (massimo) {
                                    where
                                        .append(Constants.NEED_WHERE_ESP_MIN)
                                        .append(anniEsperienza);
                                } else {
                                    where
                                        .append(Constants.NEED_WHERE_ESP_EQ)
                                        .append(anniEsperienza);
                                }
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_ANNI_ESPERIENZA);
                            }

                            break;

                        case Constants.NEED_LUOGO, Constants.NEED_LOCALITA, Constants.NEED_LOCATION:
                            String citta = UtilLib.cercaStringhe(message, needRepository.findAllLocation());

                            if (null == citta) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_LOCATION);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_LOCATION)
                                    .append(citta);
                            }

                            break;

                        case Constants.NEED_SKILL:
                            String idsSkill = UtilLib.cercaSkills(message, skillRepository.findAll());

                            if (null == idsSkill) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_SKILL);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_SKILL)
                                    .append(idsSkill)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        //TODO
                        case Constants.NEED_RICHIESTA, Constants.NEED_RICHIESTO:
                            String anni = UtilLib.findIntegers(message);

                            if (null != anni) {
                                //return ResponseEntity.ok(candidatoRepository.findByAnni(anni));
                                return null;
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_DATA_RICHIESTA);
                            }

                            break;

                        //TODO
                        case Constants.NEED_WEEK, Constants.NEED_SETTIMANA:
                            String week = UtilLib.findIntegers(message);

                            if (null != week) {
                                //return ResponseEntity.ok(candidatoRepository.findByAnni(anni));
                                return null;
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_WEEK);
                            }

                            break;

                        case Constants.NEED_PRIORITA:
                            String priorita = UtilLib.findIntegers(message);

                            if (null == priorita) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_PRIORITA);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_PRIORITA)
                                    .append(priorita);
                            }

                            break;

                        case Constants.NEED_PUBBLICAZIONE:
                            String pubblicazione = UtilLib.findIntegers(message);

                            if (null == pubblicazione) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_PUBBLICAZIONE);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_PUBBLICAZIONE)
                                    .append(pubblicazione);
                            }

                            break;

                        case Constants.NEED_SCREENING:
                            String screening = UtilLib.findIntegers(message);

                            if (null == screening) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_SCREENING);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_SCREENING)
                                    .append(screening);
                            }

                            break;

                        case Constants.NEED_OWNER, Constants.NEED_PROPRIETARIO:
                            String idOwner = UtilLib.cercaOwner(message, ownerRepository.findAll());

                            if (null == idOwner) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_OWNER);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_OWNER)
                                    .append(idOwner)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.NEED_STATO:
                            String idStati = UtilLib.cercaStato(message, statoNRepository.findAll());

                            if (null == idStati) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_STATO);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_STATI)
                                    .append(idStati)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.NEED_TIPOLOGIA:
                            String idTipoLogie = UtilLib.cercaTipologieN(message, tipologiaNRepository.findAll());

                            if (null == idTipoLogie) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_TIPOLOGIA);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_TIPOLOGIA)
                                    .append(idTipoLogie)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.NEED_TIPO:
                            String idTipi = UtilLib.cercaTipiN(message, needRepository.findAllTypes());

                            if (null == idTipi) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_NEED_TIPO);
                            } else {
                                where
                                    .append(Constants.NEED_WHERE_TIPO)
                                    .append(idTipi)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;
                    }
                }
            }

            //TODO
            if (ordina) {

            }

            return ResponseEntity.ok(needRepository.findByWhere(where.toString()));

        }
        else if (aziende) {

            for (String s : keyAziende) {
                if (message.toLowerCase().contains(s)) {
                    switch (s) {
                        case Constants.AZIENDE_DESCRIZIONE, Constants.AZIENDE_DENOMINAZIONE,
                             Constants.AZIENDE_CHIAMANO, Constants.AZIENDE_NOME, Constants.AZIENDE_RAGIONE_SOCIALE:

                            String descrizione = UtilLib.cercaStringheLike(message, aziendeRepository.findAllDescriptions());

                            if (null == descrizione) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_DESCRIZIONE);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_DESCRIZIONE)
                                    .append(descrizione);
                            }

                            break;

                        case Constants.AZIENDE_SEDE_OPERATIVA:

                            String sedeOperativa = UtilLib.cercaStringheLike(message, aziendeRepository.findAllSedeLegali());

                            if (null == sedeOperativa) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_SEDE_OPERATIVA);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_SEDE_OPERATIVA)
                                    .append(sedeOperativa);
                            }

                            break;


                        case Constants.AZIENDE_SEDE_LEGALE:

                            String sedeLegale = UtilLib.cercaStringheLike(message, aziendeRepository.findAllSedeOperative());

                            if (null == sedeLegale) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_SEDE_LEGALE);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_SEDE_LEGALE)
                                    .append(sedeLegale);
                            }

                            break;

                        case Constants.AZIENDE_CF, Constants.AZIENDE_CODICE_FISCALE:

                            String codFiscale = UtilLib.cercaStringhe(message, aziendeRepository.findAllCF());

                            if (null == codFiscale) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_CF);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_CF)
                                    .append(codFiscale);
                            }

                            break;

                        case Constants.AZIENDE_PI:

                            String pi = UtilLib.cercaStringhe(message, aziendeRepository.findAllPI());

                            if (null == pi) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_PI);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_PI)
                                    .append(pi);
                            }

                            break;

                        case Constants.AZIENDE_CITTA:
                            String citta = UtilLib.cercaStringhe(message, aziendeRepository.findAllCities());

                            if (null == citta) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_CITTA);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_CITTA)
                                    .append(citta);
                            }

                            break;

                        case Constants.AZIENDE_CAP:
                            String cap = UtilLib.cercaStringhe(message, aziendeRepository.findAllCap());

                            if (null == cap) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_CAP);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_CAP)
                                    .append(cap);
                            }

                            break;

                        case Constants.AZIENDE_INDIRIZZO:
                            String indirizzo = UtilLib.cercaStringhe(message, aziendeRepository.findAllAddress());

                            if (null == indirizzo) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_INDIRIZZO);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_INDIRIZZO)
                                    .append(indirizzo);
                            }

                            break;

                        //TODO
                        case Constants.AZIENDE_PROVINCIA:
                            String province = UtilLib.cercaProvince(message, aziendeRepository.findAllProvince());

                            if (null == province) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_PROVINCE);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_PROVINCE)
                                    .append(province)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.AZIENDE_EMAIL:
                            String email = UtilLib.cercaStringhe(message, aziendeRepository.findAllEmail());

                            if (null == email) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_EMAIL);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_EMAIL)
                                    .append(email);
                            }

                            break;

                        case Constants.AZIENDE_PEC:
                            String pec = UtilLib.cercaStringhe(message, aziendeRepository.findAllPec());

                            if (null == pec) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_PEC);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_PEC)
                                    .append(pec);
                            }

                            break;

                        case Constants.AZIENDE_CODICE_DESTINATARIO:
                            String codiceDestinatario = UtilLib.cercaStringhe(message, aziendeRepository.findAllCodiciDestinatario());

                            if (null == codiceDestinatario) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_CODICE_DESTINATARIO);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_CODICE_DESTINATARIO)
                                    .append(codiceDestinatario);
                            }

                            break;

                        //TODO
                        case Constants.AZIENDE_SCADE, Constants.AZIENDE_SCADENZA_DEL_CONTRATTO, Constants.AZIENDE_SCADENZA_CONTRATTO:
                            String scadenza = UtilLib.findIntegers(message);

                            if (null != scadenza) {
                                //return ResponseEntity.ok(candidatoRepository.findByAnni(anni));
                                return null;
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_SCADENZA_CONTRATTO);
                            }

                            break;

                        case Constants.AZIENDE_SITO, Constants.AZIENDE_INTERNET:
                            String sito = UtilLib.cercaStringhe(message, aziendeRepository.findAllSites());

                            if (null == sito) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_SITO);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_SITO)
                                    .append(sito);
                            }

                            break;

                        case Constants.AZIENDE_POTENZIALITA:
                            String potenzialita = UtilLib.findIntegers(message);

                            if (null == potenzialita) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_POTENZIALITA);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_POTENZIALITA)
                                    .append(potenzialita);
                            }

                            break;

                        case Constants.AZIENDE_SEMPLICITA:
                            String semplicita = UtilLib.findIntegers(message);

                            if (null == semplicita) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_SEMPLICITA);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_SEMPLICITA)
                                    .append(semplicita);
                            }

                            break;

                        case Constants.AZIENDE_OWNER, Constants.AZIENDE_PROPRIETARIO:
                            String idOwner = UtilLib.cercaOwner(message, ownerRepository.findAll());

                            if (null == idOwner) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_OWNER);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_OWNER)
                                    .append(idOwner)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.AZIENDE_STATO, Constants.AZIENDE_STATUS:
                            String idStati = UtilLib.cercaStati(message, statoARepository.findAll());

                            if (null == idStati) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_STATO);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_STATI)
                                    .append(idStati)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.AZIENDE_TIPOLOGIA:
                            String idTipoLogie = UtilLib.cercaTipologieA(message, tipologiaARepository.findAll());

                            if (null == idTipoLogie) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_TIPOLOGIA);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_TIPOLOGIA)
                                    .append(idTipoLogie)
                                    .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.AZIENDE_SETTORE, Constants.AZIENDE_SETTORE_DI_MERCATO,Constants.AZIENDE_SETTORE_MERCATO:
                            String settore = UtilLib.cercaStringhe(message, aziendeRepository.findAllSettoreMercato());

                            if (null == settore) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_AZIENDE_SETTORE);
                            } else {
                                where
                                    .append(Constants.AZIENDE_WHERE_SETTORE)
                                    .append(settore);
                            }

                            break;
                    }
                }
            }

            //TODO
            if (ordina) {

            }

            return ResponseEntity.ok(aziendeRepository.findByWhere(where.toString()));

        }
        else if (keyPeople) {
            for (String s : keyAziende) {
                if (message.toLowerCase().contains(s)) {
                    switch (s) {
                        case Constants.KEY_PEOPLE_NOME:

                            String nome = UtilLib.cercaStringheLike(message, keyPeopleRepository.findAllNames());

                            if (null == nome) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_KEY_PEOPLE_NOME);
                            } else {
                                where
                                        .append(Constants.KEY_PEOPLE_WHERE_NOME)
                                        .append(nome);
                            }

                            break;

                        case Constants.KEY_PEOPLE_EMAIL:
                            String email = UtilLib.cercaStringhe(message, aziendeRepository.findAllEmail());

                            if (null == email) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_KEY_PEOPLE_EMAIL);
                            } else {
                                where
                                        .append(Constants.KEY_PEOPLE_WHERE_EMAIL)
                                        .append(email);
                            }

                            break;

                        case Constants.KEY_PEOPLE_OWNER, Constants.KEY_PEOPLE_PROPRIETARIO:
                            String idOwner = UtilLib.cercaOwner(message, ownerRepository.findAll());

                            if (null == idOwner) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_KEY_PEOPLE_OWNER);
                            } else {
                                where
                                        .append(Constants.KEY_PEOPLE_WHERE_OWNER)
                                        .append(idOwner)
                                        .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        case Constants.KEY_PEOPLE_TIPOLOGIA, Constants.KEY_PEOPLE_TIPO:
                            String idTipoLogie = UtilLib.cercaTipologiek(message, tipologiakRepository.findAll());

                            if (null == idTipoLogie) {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_KEY_PEOPLE_TIPOLOGIA);
                            } else {
                                where
                                        .append(Constants.KEY_PEOPLE_WHERE_TIPOLOGIA)
                                        .append(idTipoLogie)
                                        .append(Constants.PARENTESI_CHIUSA);
                            }

                            break;

                        //TODO
                        case Constants.KEY_PEOPLE_DATA_ULTIMA_ATTIVITA:
                            String ultimaAttivita = UtilLib.findIntegers(message);

                            if (null != ultimaAttivita) {
                                //return ResponseEntity.ok(candidatoRepository.findByAnni(anni));
                                return null;
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_KEY_PEOPLE_ULTIMA_ATTIVITA);
                            }

                            break;

                        case Constants.KEY_PEOPLE_CELLULARE:
                            String cellulare = UtilLib.findIntegers(message);

                            if (null != cellulare) {
                                where
                                        .append(Constants.KEY_PEOPLE_WHERE_CELLULARE)
                                        .append(cellulare);
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_KEY_PEOPLE_CELLULARE);
                            }

                            break;

                        //TODO
                        case Constants.KEY_PEOPLE_AZIONI:
                            String azioni = UtilLib.findIntegers(message);

                            if (null != azioni) {
                                //return ResponseEntity.ok(candidatoRepository.findByAnni(anni));
                                return null;
                            } else {
                                return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT_KEY_PEOPLE_AZIONI);
                            }

                            break;

                    }
                }
            }
            if (ordina) {

            }

            return ResponseEntity.ok(keyPeopleRepository.findByWhere(where.toString()));
        }*/

        return ResponseEntity.ok(Constants.AI_MESSAGE_DEFAULT);
    }

}
