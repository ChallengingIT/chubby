/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class UtilLib {

    public static boolean like(String str, String expr) {
        expr = expr.toLowerCase(); // ignoring locale for now
        expr = expr.replace(".", "\\."); // "\\" is escaped to "\" (thanks, Alan M)
        // ... escape any other potentially problematic characters here
        expr = expr.replace("?", ".");
        expr = expr.replace("%", ".*");
        str = str.toLowerCase();
        return str.matches(expr);
    }

    public static boolean findInArray(String message, ArrayList<String> findArray) {
        for (String s : findArray) {
            if (message.contains(s.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String cercaNome(String message, List<String> nomi) {
        for (String s : nomi) {
            if (message.toLowerCase().contains(s.toLowerCase())) {
                return s;
            }
        }
        return null;
    }

    public static void caricaKeyFind(ArrayList<String> findArray) {
        findArray.add(Constants.FIND_CERCA);
        findArray.add(Constants.FIND_CERCAMI);
        findArray.add(Constants.FIND_DAI);
        findArray.add(Constants.FIND_DAMMI);
        findArray.add(Constants.FIND_RICERCA);
        findArray.add(Constants.FIND_RICERCAMI);
        findArray.add(Constants.FIND_RITROVA);
        findArray.add(Constants.FIND_RITROVAMI);
        findArray.add(Constants.FIND_TROVA);
        findArray.add(Constants.FIND_TROVAMI);
    }

    public static void caricaKeyOrder(ArrayList<String> findArray) {
        findArray.add(Constants.ORDER_ORDINA);
        findArray.add(Constants.ORDER_ORDINAMI);
        findArray.add(Constants.ORDER_ORDINATI);
        findArray.add(Constants.ORDER_PER);
        findArray.add(Constants.ORDER_RIORDINA);
        findArray.add(Constants.ORDER_RIORDINAMI);
        findArray.add(Constants.ORDER_RIORDINATI);
    }

    public static void caricaKeyLimit(ArrayList<String> findArray) {
        findArray.add(Constants.LIMIT_PRIMI);
        findArray.add(Constants.LIMIT_ULTIMI);
    }

    public static void caricaAziendeQuery(ArrayList<String> array) {
        array.add(Constants.AZIENDE_CF);
        array.add(Constants.AZIENDE_PI);
        array.add(Constants.AZIENDE_PEC);
        array.add(Constants.AZIENDE_DESCRIZIONE);
        array.add(Constants.AZIENDE_NOME);
        array.add(Constants.AZIENDE_PROPRIETARIO);
        array.add(Constants.AZIENDE_OWNER);
        array.add(Constants.AZIENDE_DENOMINAZIONE);
        array.add(Constants.AZIENDE_SETTORE);
        array.add(Constants.AZIENDE_CODICE_FISCALE);
        array.add(Constants.AZIENDE_RAGIONE_SOCIALE);
        array.add(Constants.AZIENDE_SEDE_LEGALE);
        array.add(Constants.AZIENDE_CHIAMANO);
        array.add(Constants.AZIENDE_TIPOLOGIA);
        array.add(Constants.AZIENDE_STATUS);
        array.add(Constants.AZIENDE_SITO);
        array.add(Constants.AZIENDE_STATO);
        array.add(Constants.AZIENDE_SETTORE_MERCATO);
        array.add(Constants.AZIENDE_SETTORE_DI_MERCATO);
        array.add(Constants.AZIENDE_SEMPLICITA);
        array.add(Constants.AZIENDE_SEDE_OPERATIVA);
        array.add(Constants.AZIENDE_SCADENZA_DEL_CONTRATTO);
        array.add(Constants.AZIENDE_SCADENZA_CONTRATTO);
        array.add(Constants.AZIENDE_PROVINCIA);
        array.add(Constants.AZIENDE_INDIRIZZO);
        array.add(Constants.AZIENDE_CAP);
        array.add(Constants.AZIENDE_CITTA);
        array.add(Constants.AZIENDE_EMAIL);
        array.add(Constants.AZIENDE_CODICE_DESTINATARIO);
        array.add(Constants.AZIENDE_POTENZIALITA);
        array.add(Constants.AZIENDE_IDA);
    }

    public static void caricaAziendeText(ArrayList<String> array) {
        array.add(Constants.AZIENDE_AZIENDE);
        array.add(Constants.AZIENDE_AZIENDA);
        array.add(Constants.AZIENDE_BUSINESS);
        array.add(Constants.AZIENDE_CLIENTE);
        array.add(Constants.AZIENDE_CLIENTI);
    }

    public static void caricaCandidatiQuery(ArrayList<String> array) {
        array.add(Constants.CANDIDATI_FACOLTA);
        array.add(Constants.CANDIDATI_CITTA);
        array.add(Constants.CANDIDATI_OWNER);
        array.add(Constants.CANDIDATI_STATO);
        array.add(Constants.CANDIDATI_SKILL);
        array.add(Constants.CANDIDATI_TIPOLOGIA);
        array.add(Constants.CANDIDATI_TIPO);
        array.add(Constants.CANDIDATI_NOME);
        array.add(Constants.CANDIDATI_HA_STUDIATO);
        array.add(Constants.CANDIDATI_JOB_TITLE);
        array.add(Constants.CANDIDATI_CELLULARE);
        array.add(Constants.CANDIDATI_RATING);
        array.add(Constants.CANDIDATI_RAL);
        array.add(Constants.CANDIDATI_PROPRIETARIO);
        array.add(Constants.CANDIDATI_NATO);
        array.add(Constants.CANDIDATI_MODALITA);
        array.add(Constants.CANDIDATI_LIVELLO_SCOLASTICO);
        array.add(Constants.CANDIDATI_ESPERIENZA);
        array.add(Constants.CANDIDATI_EMAIL);
        array.add(Constants.CANDIDATI_DISPONIBILITA);
        array.add(Constants.CANDIDATI_DATA_ULTIMO_CONTATTO);
        array.add(Constants.CANDIDATI_DATA_DI_NASCITA);
        array.add(Constants.CANDIDATI_COGNOME);
        array.add(Constants.CANDIDATI_ANNI_DI_ESPERIENZA);
        array.add(Constants.CANDIDATI_CONOSCE);
        array.add(Constants.CANDIDATI_CHIAMANO);
    }

    public static void caricaCandidatiText(ArrayList<String> array) {
        array.add(Constants.CANDIDATI_CANDIDATI);
        array.add(Constants.CANDIDATI_CANDIDATO);
    }

    public static void caricaKeyPeopleQuery(ArrayList<String> array) {
        array.add(Constants.KEY_PEOPLE_NOME);
        array.add(Constants.KEY_PEOPLE_CLIENTE);
        array.add(Constants.KEY_PEOPLE_TIPO);
        array.add(Constants.KEY_PEOPLE_PROPRIETARIO);
        array.add(Constants.KEY_PEOPLE_EMAIL);
        array.add(Constants.KEY_PEOPLE_BUSINESS);
        array.add(Constants.KEY_PEOPLE_CELLULARE);
        array.add(Constants.KEY_PEOPLE_TIPOLOGIA);
        array.add(Constants.KEY_PEOPLE_RUOLO);
        array.add(Constants.KEY_PEOPLE_OWNER);
        array.add(Constants.KEY_PEOPLE_DATA_ULTIMA_ATTIVITA);
        array.add(Constants.KEY_PEOPLE_AZIONI);
        array.add(Constants.KEY_PEOPLE_AZIENDA);
    }

    public static void caricaKeyPeopleText(ArrayList<String> array) {
        array.add(Constants.KEY_PEOPLE_KEYPEOPLE);
        array.add(Constants.KEY_PEOPLE_KEY_PEOPLE);
        array.add(Constants.KEY_PEOPLE_CONTACT);
        array.add(Constants.KEY_PEOPLE_CONTACTS);
    }

    public static void caricaNeedQuery(ArrayList<String> array) {
        array.add(Constants.NEED_PROGRESSIVO);
        array.add(Constants.NEED_DESCRIZIONE);
        array.add(Constants.NEED_CLIENTE);
        array.add(Constants.NEED_KEYPEOPLE);
        array.add(Constants.NEED_ESPERIENZA);
        array.add(Constants.NEED_LOCATION);
        array.add(Constants.NEED_RICHIESTA);
        array.add(Constants.NEED_WEEK);
        array.add(Constants.NEED_KEY_PEOPLE);
        array.add(Constants.NEED_TIPOLOGIA);
        array.add(Constants.NEED_STATO);
        array.add(Constants.NEED_SKILL);
        array.add(Constants.NEED_SETTIMANA);
        array.add(Constants.NEED_SCREENING);
        array.add(Constants.NEED_RICHIESTO);
        array.add(Constants.NEED_PROPRIETARIO);
        array.add(Constants.NEED_PRIORITA);
        array.add(Constants.NEED_OWNER);
        array.add(Constants.NEED_LUOGO);
        array.add(Constants.NEED_LOCALITA);
        array.add(Constants.NEED_CHIAMATO);
        array.add(Constants.NEED_CANDIDATI);
        array.add(Constants.NEED_BUSINESS);
        array.add(Constants.NEED_AZIENDA);
        array.add(Constants.NEED_ASSOCIATI);
        array.add(Constants.NEED_ANNI_DI_ESPERIENZA);
        array.add(Constants.NEED_RISORSE);
        array.add(Constants.NEED_TIPO);
        array.add(Constants.NEED_PUBBLICAZIONE);
    }

    public static void caricaNeedText(ArrayList<String> array) {
        array.add(Constants.NEED_NEED);
    }

    public static void caricaDictionary(LinkedHashMap<ArrayList<String>, ArrayList<String>> dictionary) {

        ArrayList<String> aziendeQuery   = new ArrayList<>();
        ArrayList<String> aziendeText    = new ArrayList<>();
        ArrayList<String> candidatiQuery = new ArrayList<>();
        ArrayList<String> candidatiText  = new ArrayList<>();
        ArrayList<String> keyPeopleQuery = new ArrayList<>();
        ArrayList<String> keyPeopleText  = new ArrayList<>();
        ArrayList<String> needQuery      = new ArrayList<>();
        ArrayList<String> needText       = new ArrayList<>();
        HashMap<ArrayList<String>, ArrayList<String>> aziendeMap = new HashMap<>();
        HashMap<ArrayList<String>, ArrayList<String>> candidatiMap = new HashMap<>();
        HashMap<ArrayList<String>, ArrayList<String>> keyPeopleMap = new HashMap<>();
        HashMap<ArrayList<String>, ArrayList<String>> needMap = new HashMap<>();


        caricaAziendeQuery(aziendeQuery);
        caricaAziendeText(aziendeText);
        caricaCandidatiQuery(candidatiQuery);
        caricaCandidatiText(candidatiText);
        caricaKeyPeopleQuery(keyPeopleQuery);
        caricaKeyPeopleText(keyPeopleText);
        caricaNeedQuery(needQuery);
        caricaNeedText(needText);

        dictionary.put(aziendeText,aziendeQuery);
        dictionary.put(candidatiText,candidatiQuery);
        dictionary.put(keyPeopleText,keyPeopleQuery);
        dictionary.put(needText,needQuery);

        /*dictionary.put("aziende", aziendeMap);
        dictionary.put("candidati", candidatiMap);
        dictionary.put("keypeople", keyPeopleMap);
        dictionary.put("need", needMap);*/
    }

    public static ArrayList<String> getElementByIndex(LinkedHashMap<ArrayList<String>,ArrayList<String>> map,int index){
        return map.get( (map.keySet().toArray())[ index ] );
    }
}
