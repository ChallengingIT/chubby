/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.util;

import it.challenging.torchy.entity.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String cercaStringhe(String message, List<String> stringhe) {
        for (String s : stringhe) {
            if (message.toLowerCase().contains(s.toLowerCase())) {
                return s;
            }
        }
        return null;
    }

    public static String cercaStringheLike(String message, List<String> stringhe) {
        for (String s : stringhe) {
            if (Arrays.stream(message.split(" ")).anyMatch((p -> like(p,s)))){
                return s;
            }
        }
        return null;
    }

    public static String cercaSkills(String message, List<Skill> skill) {
        StringBuilder idSkills = new StringBuilder();

        for (Skill s : skill) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                idSkills.append(s.getId()).append(",");
            }
        }

        idSkills = new StringBuilder(idSkills.substring(0, idSkills.length() - 1));

        return (idSkills.isEmpty()) ? null : idSkills.toString();
    }

    public static String cercaFacolta(String message, List<Facolta> facolta) {
        StringBuilder facoltaList = new StringBuilder();

        for (Facolta s : facolta) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                facoltaList.append(s.getId()).append(",");
            }
        }

        facoltaList = new StringBuilder(facoltaList.substring(0, facoltaList.length() - 1));

        return (facoltaList.isEmpty()) ? null : facoltaList.toString();
    }

    public static String cercaOwner(String message, List<Owner> owner) {
        StringBuilder ownerList = new StringBuilder();

        for (Owner s : owner) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                ownerList.append(s.getId()).append(",");
            }
        }

        ownerList = new StringBuilder(ownerList.substring(0, ownerList.length() - 1));

        return (ownerList.isEmpty()) ? null : ownerList.toString();
    }

    public static String cercaTipologieN(String message, List<TipologiaN> tipologie) {
        StringBuilder tipologieList = new StringBuilder();

        for (TipologiaN s : tipologie) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                tipologieList.append(s.getId()).append(",");
            }
        }

        tipologieList = new StringBuilder(tipologieList.substring(0, tipologieList.length() - 1));

        return (tipologieList.isEmpty()) ? null : tipologieList.toString();
    }

    public static String cercaTipiN(String message, List<Integer> tipi) {
        StringBuilder tipologieList = new StringBuilder();

        for (Integer s : tipi) {
            if (message.toLowerCase().contains("cliente")){
                tipologieList.append("1").append(",");
            } else  if (message.toLowerCase().contains("consulenza")){
                tipologieList.append("2").append(",");
            } else  if (message.toLowerCase().contains("prospect")){
                tipologieList.append("3").append(",");
            }
        }

        tipologieList = new StringBuilder(tipologieList.substring(0, tipologieList.length() - 1));

        return (tipologieList.isEmpty()) ? null : tipologieList.toString();
    }

    public static String cercaTipologieK(String message) {
        StringBuilder tipologieList = new StringBuilder();
        if (message.toLowerCase().contains("keypeople")){
            tipologieList.append("1").append(",");
        } else  if (message.toLowerCase().contains("hook")){
            tipologieList.append("2").append(",");
        } else  if (message.toLowerCase().contains("link")){
            tipologieList.append("3").append(",");
        }

        tipologieList = new StringBuilder(tipologieList.substring(0, tipologieList.length() - 1));

        return (tipologieList.isEmpty()) ? null : tipologieList.toString();
    }

    public static String cercaTipologieA(String message, List<String> tipologie) {
        StringBuilder tipologieList = new StringBuilder();

        for (String s : tipologie) {
            if (message.toLowerCase().contains(s)){
                tipologieList.append(s).append(",");
            }
        }

        tipologieList = new StringBuilder(tipologieList.substring(0, tipologieList.length() - 1));

        return (tipologieList.isEmpty()) ? null : tipologieList.toString();
    }

    public static String cercaStatoN(String message, List<StatoN> stati) {
        StringBuilder statiList = new StringBuilder();

        for (StatoN s : stati) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                statiList.append(s.getId()).append(",");
            }
        }

        statiList = new StringBuilder(statiList.substring(0, statiList.length() - 1));

        return (statiList.isEmpty()) ? null : statiList.toString();
    }

    public static String cercaLivello(String message, List<LivelloScolastico> livelli) {
        StringBuilder livelliList = new StringBuilder();

        for (LivelloScolastico s : livelli) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                livelliList.append(s.getId()).append(",");
            }
        }

        livelliList = new StringBuilder(livelliList.substring(0, livelliList.length() - 1));

        return (livelliList.isEmpty()) ? null : livelliList.toString();
    }

    public static String cercaTipoCandidatura(String message, List<TipoCandidatura> candidature) {
        StringBuilder candidatureList = new StringBuilder();

        for (TipoCandidatura s : candidature) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                candidatureList.append(s.getId()).append(",");
            }
        }

        candidatureList = new StringBuilder(candidatureList.substring(0, candidatureList.length() - 1));

        return (candidatureList.isEmpty()) ? null : candidatureList.toString();
    }

    public static String cercaTipoRicerca(String message, List<TipoRicerca> ricerche) {
        StringBuilder ricercheList = new StringBuilder();

        for (TipoRicerca s : ricerche) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                ricercheList.append(s.getId()).append(",");
            }
        }

        ricercheList = new StringBuilder(ricercheList.substring(0, ricercheList.length() - 1));

        return (ricercheList.isEmpty()) ? null : ricercheList.toString();
    }

    public static String cercaTipo(String message, List<Tipo> tipi) {
        StringBuilder tipoList = new StringBuilder();

        for (Tipo s : tipi) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                tipoList.append(s.getId()).append(",");
            }
        }

        tipoList = new StringBuilder(tipoList.substring(0, tipoList.length() - 1));

        return (tipoList.isEmpty()) ? null : tipoList.toString();
    }


    public static String cercaJobTitle(String message, List<Tipologia> tipologie) {
        StringBuilder idTipologie = new StringBuilder();

        for (Tipologia s : tipologie) {
            if (message.toLowerCase().contains(s.getDescrizione().toLowerCase())) {
                idTipologie.append(s.getId()).append(",");
            }
        }

        idTipologie = new StringBuilder(idTipologie.substring(0, idTipologie.length() - 1));

        return (idTipologie.isEmpty()) ? null : idTipologie.toString();
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
        array.add(Constants.AZIENDE_SCADE);
        array.add(Constants.AZIENDE_INTERNET);
    }

    public static void caricaAziendeText(ArrayList<String> array) {
        array.add(Constants.AZIENDE_AZIENDE);
        array.add(Constants.AZIENDE_AZIENDA);
        array.add(Constants.AZIENDE_BUSINESS);
        array.add(Constants.AZIENDE_CLIENTE);
        array.add(Constants.AZIENDE_CLIENTI);
    }

    public static void caricaCandidatiQuery(ArrayList<String> array) {
        array.add(Constants.CANDIDATI_FACOLTA); array.add(Constants.CANDIDATI_CITTA); array.add(Constants.CANDIDATI_OWNER);
        array.add(Constants.CANDIDATI_STATO); array.add(Constants.CANDIDATI_SKILL); array.add(Constants.CANDIDATI_TIPOLOGIA);
        array.add(Constants.CANDIDATI_TIPO); array.add(Constants.CANDIDATI_NOME); array.add(Constants.CANDIDATI_HA_STUDIATO);
        array.add(Constants.CANDIDATI_JOB_TITLE); array.add(Constants.CANDIDATI_CELLULARE); array.add(Constants.CANDIDATI_RATING);
        array.add(Constants.CANDIDATI_RAL); array.add(Constants.CANDIDATI_PROPRIETARIO); array.add(Constants.CANDIDATI_NATO);
        array.add(Constants.CANDIDATI_MODALITA); array.add(Constants.CANDIDATI_LIVELLO_SCOLASTICO); array.add(Constants.CANDIDATI_ESPERIENZA);
        array.add(Constants.CANDIDATI_EMAIL); array.add(Constants.CANDIDATI_DISPONIBILITA); array.add(Constants.CANDIDATI_DATA_ULTIMO_CONTATTO);
        array.add(Constants.CANDIDATI_DATA_DI_NASCITA); array.add(Constants.CANDIDATI_COGNOME); array.add(Constants.CANDIDATI_ANNI_DI_ESPERIENZA);
        array.add(Constants.CANDIDATI_CONOSCE); array.add(Constants.CANDIDATI_CHIAMANO); array.add(Constants.CANDIDATI_VIVE);
        array.add(Constants.CANDIDATI_ABITA); array.add(Constants.CANDIDATI_TIPO_CANDIDATURA); array.add(Constants.CANDIDATI_TIPO_RICERCA);
        array.add(Constants.CANDIDATI_TELEFONINO); array.add(Constants.CANDIDATI_TELEFONO); array.add(Constants.CANDIDATI_SKILLATO);
        array.add(Constants.CANDIDATI_DISPONIBILE);
    }

    public static void caricaCandidatiText(ArrayList<String> array) {
        array.add(Constants.CANDIDATI_CANDIDATI);
        array.add(Constants.CANDIDATI_CANDIDATO);
        array.add(Constants.CANDIDATI_SVILUPPATORI);
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
        array.add(Constants.NEED_CONTACT);
        array.add(Constants.NEED_CONTATTO);
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

    public static String findIntegers(String stringToSearch) {
        Pattern integerPattern = Pattern.compile("-?\\d+");
        Matcher matcher = integerPattern.matcher(stringToSearch);

        List<String> integerList = new ArrayList<>();
        while (matcher.find()) {
            integerList.add(matcher.group());
        }

        return !integerList.isEmpty() ? integerList.get(0) : null;
    }

    public static String findDoubles(String stringToSearch) {
        Pattern p = Pattern.compile("(\\d+(?:\\.\\d+))");
        Matcher m = p.matcher(stringToSearch);

        List<String> doubleList = new ArrayList<>();

        while (m.find()) {
            doubleList.add(m.group());
        }

        return !doubleList.isEmpty() ? doubleList.get(0) : null;
    }
}
