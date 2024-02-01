/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.util;

import it.innotek.wehub.entity.timesheet.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UtilLib {

    public static Calendario creazioneCalendarioLib(int anno, int mese, int giorno, Progetto progetto){

        Calendario calendario    = new Calendario();
        LocalDate  local         = LocalDate.of(anno, mese, giorno);
        int        annoFinale    = anno;
        int        meseAttuale   = mese;
        int        giornoAttuale = giorno;
        Anno       annoNuovo     = new Anno();
        Mese       meseNuovo     = new Mese();
        Giorno     giornoNuovo;

        while(annoFinale == local.getYear()){

            while(meseAttuale == local.getMonthValue() && annoFinale == local.getYear()) {

                meseNuovo.setDescription(local.getMonth().name());
                meseNuovo.setValue(local.getMonthValue());
                meseNuovo.setInviato(false);

                while (giornoAttuale == local.getDayOfMonth() && meseAttuale == local.getMonthValue()) {

                    giornoNuovo = new Giorno();
                    giornoNuovo.setGiorno(local.getDayOfMonth());

                    giornoNuovo.setProgetto(progetto);
                    giornoNuovo.setOreTotali(0);
                    giornoNuovo.setOreOrdinarie(0);
                    giornoNuovo.setOrePermesso(0);
                    giornoNuovo.setOreStraordinarie(0);
                    giornoNuovo.setOreStraordinarieNotturne(0);
                    giornoNuovo.setFerie(false);
                    giornoNuovo.setPermesso(false);
                    giornoNuovo.setMalattia(false);
                    giornoNuovo.setData(local);

                    if (local.getDayOfWeek().name().equals("SATURDAY") || local.getDayOfWeek().name().equals("SUNDAY") || isGiornoFestivo(meseAttuale, giornoAttuale)) {
                        giornoNuovo.setFestivo(true);
                    } else {
                        giornoNuovo.setFestivo(calcoloPasqua(annoFinale)[0] == meseAttuale && calcoloPasqua(annoFinale)[1] == local.getDayOfMonth());
                    }
                    giornoNuovo.setIniziale(inizialeItaliana(local.getDayOfWeek().name()));

                    meseNuovo.getDays().add(giornoNuovo);

                    local         = local.plusDays(1);
                    giornoAttuale = local.getDayOfMonth();
                }

                List<Giorno> giorni = ordinaGiorni(meseNuovo.getDays());

                meseNuovo.setDays(giorni);

                annoNuovo.getMesi().add(meseNuovo);
                meseAttuale = local.getMonthValue();
                meseNuovo   = new Mese();
            }

            List<Mese> mesi = ordinaMesi(annoNuovo.getMesi());

            annoNuovo.setMesi(mesi);

            annoNuovo.setAnno(annoFinale);

            calendario.getAnni().add(annoNuovo);

            List<Anno> anni = ordinaAnni(calendario.getAnni());

            calendario.setAnni(anni);

            annoFinale = local.getYear();
            annoNuovo  = new Anno();

            if (annoFinale == 2051) {
                break;
            }
        }
        return calendario;
    }

    private static boolean isGiornoFestivo(int mese, int giorno) {
        if (mese == 1 && (giorno == 1 || giorno == 6)) {
            return true;
        } else if ((mese == 4 && giorno == 25) || (mese == 5 && giorno == 1) || (mese == 6 && giorno == 2) || (mese == 6 && giorno == 29)) {
            return true;
        } else if ((mese == 8 && giorno == 15) || (mese == 11 && giorno == 1) || (mese == 12 && giorno == 8)) {
            return true;
        } else {
            return mese == 12 && (giorno == 25 || giorno == 26);
        }
    }

    private static int[] calcoloPasqua (int anno) {

        int       y         = anno; //anno gregoriano compreso tra il 1583 ed il 2499
        int[]     risultato = new int[2];
        final int a         = y % 19;
        final int b         = y % 4;
        final int c         = y % 7;
        int       m         = 24;
        int       n         = 5;

        if (y >= 1583 && y <= 1699) {
            m = 22;
            n = 2;
        } else if (y >= 1700 && y <= 1799) {
            m = 23;
            n = 3;
        } else if (y >= 1800 && y <= 1899) {
            m = 23;
            n = 4;
        } else if (y >= 1900 && y <= 2099) {
            m = 24;
            n = 5;
        } else if (y >= 2100 && y <= 2199) {
            m = 24;
            n = 6;
        } else if (y >= 2200 && y <= 2299) {
            m = 25;
            n = 0;
        } else if (y >= 2300 && y <= 2399) {
            m = 26;
            n = 1;
        } else if (y >= 2400 && y <= 2499) {
            m = 25;
            n = 1;
        }
        int d = (19 * a + m) % 30;
        int e = (2 * b + 4 * c + 6 * d + n) % 7;

        if (d + e < 10) {
            int marzo = d + e + 22;//caso marzo
            risultato[0] = 3;
            risultato[1] = marzo;

            return risultato;
        } else {
            int aprile = d + e - 9;
            if (aprile == 26) {
                risultato[0] = 4;
                risultato[1] = 19;

                return risultato;
            } else if (aprile == 25 && e==6 && a>10){
                risultato[0] = 4;
                risultato[1] = 18;

                return risultato;
            } else {
                risultato[0] = 4;
                risultato[1] = aprile;

                return risultato;
            }
        }
    }

    public static Anno prendiAnno(List<Anno> anni, int annoRichiesto){

        Iterator<Anno> itAnni = anni.stream().iterator();

        while (itAnni.hasNext()) {
            Anno anno = itAnni.next();
            if (anno.getAnno() == annoRichiesto) {
                return anno;
            }
        }
        return null;
    }

    public static Anno prendiPrimoAnno(List<Anno> anni){

        Anno annoMinimo = null;

        if(null != anni){
            anni.sort(Comparator.comparing(Anno::getAnno));
            annoMinimo = anni.get(0);
        }

        return annoMinimo;
    }

    public static Mese prendiMese(List<Mese> mesi, int meseRichiesto){

        Iterator<Mese> itMesi = mesi.stream().iterator();

        while (itMesi.hasNext()) {
            Mese mese = itMesi.next();
            if (mese.getValue() == meseRichiesto) {
                return mese;
            }
        }
        return null;
    }

    public static Mese prendiPrimoMese(List<Mese> mesi){

        Mese meseMinimo = null;

        if(null != mesi){
            mesi.sort(Comparator.comparing(Mese::getValue));
            meseMinimo = mesi.get(0);
        }

        return meseMinimo;
    }

    public static Giorno prendiGiorno(List<Giorno> giorni, int giornoRichiesto){

        Iterator<Giorno> itGiorni = giorni.stream().iterator();

        while (itGiorni.hasNext()) {
            Giorno giorno = itGiorni.next();
            if (giorno.getGiorno() == giornoRichiesto) {
                return giorno;
            }
        }
        return null;
    }

    public static List<Giorno> prendiGiorniUguali(List<Giorno> giorni, int giornoRichiesto){

        return giorni.stream().filter(p -> p.getGiorno() == giornoRichiesto).collect(Collectors.toList());
    }

    public static List<Giorno> unisciGiorniUguali(List<Giorno> giorni, Integer giornoInizio, Integer giornoFine){
        List<Giorno> giorniUniti = new ArrayList<>();

        for (int i = giornoInizio ; i <= giornoFine; i++) {

            Giorno       nuovo                    = new Giorno();
            List<Giorno> uniti                    = prendiGiorniUguali(giorni, i);
            int          oreTotali                = 0;
            int          oreOrdinarie             = 0;
            int          oreStraordinarie         = 0;
            int          oreStraordinarieNotturne = 0;
            int          orePermesso              = 0;
            boolean      ferie                    = false;
            boolean      malattia                 = false;
            boolean      permesso                 = false;

            for (Giorno unito: uniti) {

                nuovo.setGiorno(unito.getGiorno());
                nuovo.setData(unito.getData());
                nuovo.setIniziale(unito.getIniziale());
                nuovo.setFestivo(unito.isFestivo());

                oreTotali                += unito.getOreTotali();
                oreOrdinarie             += unito.getOreOrdinarie();
                oreStraordinarie         += unito.getOreStraordinarie();
                oreStraordinarieNotturne += unito.getOreStraordinarieNotturne();
                orePermesso              += unito.getOrePermesso();

                if(unito.isFerie())    ferie    = true;
                if(unito.isMalattia()) malattia = true;
                if(unito.isPermesso()) permesso = true;
            }

            nuovo.setOreTotali(oreTotali);
            nuovo.setOreStraordinarie(oreStraordinarie);
            nuovo.setOreOrdinarie(oreOrdinarie);
            nuovo.setOreStraordinarieNotturne(oreStraordinarieNotturne);
            nuovo.setOrePermesso(orePermesso);
            nuovo.setFerie(ferie);
            nuovo.setMalattia(malattia);
            nuovo.setPermesso(permesso);

            giorniUniti.add(nuovo);
        }

        return ordinaGiorni(giorniUniti);
    }

    public static List<Giorno> prendiGiorni(Mese mese, Integer giornoInizio, Integer giornoFine){

        Iterator<Giorno> itGiorni = mese.getDays().stream().iterator();
        List<Giorno>     giorni   = new ArrayList<>();

        while (itGiorni.hasNext()) {
            Giorno giorno = itGiorni.next();
            if ((null != giornoInizio) && (null != giornoFine)){

                if (giorno.getGiorno() >= giornoInizio && giorno.getGiorno() <= giornoFine){
                    giorni.add(giorno);
                }

            } else if (giornoInizio != null) {

                if (giorno.getGiorno() >= giornoInizio) {
                    giorni.add(giorno);
                }
            } else {
                if (giorno.getGiorno() <= giornoFine) {
                    giorni.add(giorno);
                }
            }
        }
        return giorni;
    }

    public static Calendario aggiornaOreCalendario(Calendario calendario, Anno anno, Mese mese, Giorno giorno, AggiornaTimesheet aggiornamento){

        Iterator<Anno> itAnni = ordinaAnni(calendario.getAnni()).stream().iterator();

        while (itAnni.hasNext()) {
            Anno annoCorrente = itAnni.next();
            if (Objects.equals(anno.getAnno(), annoCorrente.getAnno())) {
                Iterator<Mese> itMesi =ordinaMesi(annoCorrente.getMesi()).stream().iterator();
                while (itMesi.hasNext()) {
                    Mese meseCorrente = itMesi.next();
                    if (Objects.equals(mese.getValue(), meseCorrente.getValue())) {
                        Iterator<Giorno> itGiorni = ordinaGiorni(meseCorrente.getDays()).stream().iterator();
                        while (itGiorni.hasNext()) {
                            Giorno giornoCorrente = itGiorni.next();
                            if (Objects.equals(giorno.getGiorno(), giornoCorrente.getGiorno())
                                && Objects.equals(giornoCorrente.getProgetto().getId(), aggiornamento.getProgetto().getId())) {
                                if (null != aggiornamento.getOre()) {
                                    giornoCorrente.setOreOrdinarie(aggiornamento.getOre());
                                } else {
                                    giornoCorrente.setOreOrdinarie(0);
                                }
                                if (null != aggiornamento.getOrePermesso()) {
                                    giornoCorrente.setOrePermesso(aggiornamento.getOrePermesso());
                                } else {
                                    giornoCorrente.setOrePermesso(0);
                                }
                                if (null != aggiornamento.getOreStraordinarie()) {
                                    giornoCorrente.setOreStraordinarie(aggiornamento.getOreStraordinarie());
                                } else {
                                    giornoCorrente.setOreStraordinarie(0);
                                }
                                if (null != aggiornamento.getOreStraordinarieNotturne()) {
                                    giornoCorrente.setOreStraordinarieNotturne(aggiornamento.getOreStraordinarieNotturne());
                                } else {
                                    giornoCorrente.setOreStraordinarieNotturne(0);
                                }
                                if (null != aggiornamento.getFerie()) {
                                    giornoCorrente.setFerie(aggiornamento.getFerie());
                                } else {
                                    giornoCorrente.setFerie(false);
                                }
                                if (null != aggiornamento.getMalattia()) {
                                    giornoCorrente.setMalattia(aggiornamento.getMalattia());
                                } else {
                                    giornoCorrente.setMalattia(false);
                                }
                                if (null != aggiornamento.getPermesso()) {
                                    giornoCorrente.setPermesso(aggiornamento.getPermesso());
                                } else {
                                    giornoCorrente.setPermesso(false);
                                }
                                giornoCorrente.setOreTotali(
                                    (aggiornamento.getOre() != null ? aggiornamento.getOre() : 0) +
                                    (aggiornamento.getOreStraordinarie() != null ? aggiornamento.getOreStraordinarie() : 0) +
                                    (aggiornamento.getOreStraordinarieNotturne() != null ? aggiornamento.getOreStraordinarieNotturne() : 0) +
                                    (aggiornamento.getOrePermesso() != null ? aggiornamento.getOrePermesso() : 0));
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return calendario;
    }

    public static Calendario aggiornaProgettoCalendario(Calendario calendario, Progetto progetto){

        Iterator<Anno> itAnni = ordinaAnni(calendario.getAnni()).stream().iterator();
        LocalDate      inizio = progetto.getInizio();
        LocalDate      fine   = progetto.getScadenza();

        progetto.setInizio(LocalDate.of(progetto.getInizio().getYear(),progetto.getInizio().getMonthValue(),1));
        progetto.setScadenza(LocalDate.of(progetto.getScadenza().getYear(),progetto.getScadenza().getMonthValue(),calcolaFineMese(progetto.getScadenza().getMonthValue(),progetto.getScadenza().getYear())));

        Giorno giornoNuovo;
        while(itAnni.hasNext()) {

            Anno           annoCorrente = itAnni.next();
            Iterator<Mese> itMesi       = ordinaMesi(annoCorrente.getMesi()).stream().iterator();

            while (itMesi.hasNext()) {

                Mese      meseCorrente   = itMesi.next();
                int       giornoIniziale = ordinaGiorni(meseCorrente.getDays()).stream().findFirst().get().getGiorno();
                LocalDate local          = LocalDate.of(annoCorrente.getAnno(), meseCorrente.getValue(), giornoIniziale);

                while ( (local.isEqual(progetto.getInizio()) || local.isEqual(progetto.getScadenza()) ||(local.isAfter(progetto.getInizio()) && local.isBefore(progetto.getScadenza()))) && meseCorrente.getValue() == local.getMonthValue()) {
                    giornoNuovo = new Giorno();
                    giornoNuovo.setGiorno(local.getDayOfMonth());
                    giornoNuovo.setProgetto(progetto);
                    giornoNuovo.setOreTotali(0);
                    giornoNuovo.setOreOrdinarie(0);
                    giornoNuovo.setOrePermesso(0);
                    giornoNuovo.setOreStraordinarie(0);
                    giornoNuovo.setOreStraordinarieNotturne(0);
                    giornoNuovo.setFerie(false);
                    giornoNuovo.setPermesso(false);
                    giornoNuovo.setMalattia(false);
                    giornoNuovo.setData(local);

                    if (local.getDayOfWeek().name().equals("SATURDAY") ||
                        local.getDayOfWeek().name().equals("SUNDAY") ||
                        isGiornoFestivo(meseCorrente.getValue(), local.getDayOfMonth())
                    ) {
                        giornoNuovo.setFestivo(true);
                    } else {
                        giornoNuovo.setFestivo(calcoloPasqua(annoCorrente.getAnno())[0] == meseCorrente.getValue() && calcoloPasqua(annoCorrente.getAnno())[1] == local.getDayOfMonth());
                    }

                    giornoNuovo.setIniziale(inizialeItaliana(local.getDayOfWeek().name()));

                    meseCorrente.getDays().add(giornoNuovo);

                    local = local.plusDays(1);
                }
            }
        }

        progetto.setInizio(inizio);
        progetto.setScadenza(fine);

        return calendario;
    }

    public static Calendario aggiornaProgettoCalendarioConDate(Calendario calendario, Progetto progetto, LocalDate inizio, LocalDate fine){

        Iterator<Anno> itAnni = ordinaAnni(calendario.getAnni()).stream().iterator();

        Giorno giornoNuovo;
        while (itAnni.hasNext()) {

            Anno           annoCorrente = itAnni.next();
            Iterator<Mese> itMesi       = ordinaMesi(annoCorrente.getMesi()).stream().iterator();

            while (itMesi.hasNext()) {

                Mese      meseCorrente   = itMesi.next();
                int       giornoIniziale = ordinaGiorni(meseCorrente.getDays()).stream().findFirst().get().getGiorno();
                LocalDate local          = LocalDate.of(annoCorrente.getAnno(), meseCorrente.getValue(), giornoIniziale);

                while ( (local.isEqual(inizio) || local.isEqual(fine) || (local.isAfter(inizio) && local.isBefore(fine)))
                        && meseCorrente.getValue() == local.getMonthValue()
                ) {
                    giornoNuovo = new Giorno();
                    giornoNuovo.setGiorno(local.getDayOfMonth());
                    giornoNuovo.setProgetto(progetto);
                    giornoNuovo.setOreTotali(0);
                    giornoNuovo.setOreOrdinarie(0);
                    giornoNuovo.setOrePermesso(0);
                    giornoNuovo.setOreStraordinarie(0);
                    giornoNuovo.setOreStraordinarieNotturne(0);
                    giornoNuovo.setFerie(false);
                    giornoNuovo.setPermesso(false);
                    giornoNuovo.setMalattia(false);
                    giornoNuovo.setData(local);

                    if (local.getDayOfWeek().name().equals("SATURDAY") ||
                        local.getDayOfWeek().name().equals("SUNDAY") ||
                        isGiornoFestivo(meseCorrente.getValue(), local.getDayOfMonth())
                    ) {
                        giornoNuovo.setFestivo(true);
                    } else {
                        giornoNuovo.setFestivo(calcoloPasqua(annoCorrente.getAnno())[0] == meseCorrente.getValue() && calcoloPasqua(annoCorrente.getAnno())[1] == local.getDayOfMonth());
                    }

                    giornoNuovo.setIniziale(inizialeItaliana(local.getDayOfWeek().name()));

                    meseCorrente.getDays().add(giornoNuovo);

                    local = local.plusDays(1);
                }
            }
        }
        return calendario;
    }

    public static Calendario rimuoviProgettoCalendario(Calendario calendario,int idProgetto){

        Iterator<Anno> itAnni = ordinaAnni(calendario.getAnni()).stream().iterator();
        while (itAnni.hasNext()) {

            Anno           annoCorrente = itAnni.next();
            Iterator<Mese> itMesi       = ordinaMesi(annoCorrente.getMesi()).stream().iterator();

            while (itMesi.hasNext()) {
                itMesi.next().getDays().removeIf(giornoCorrente -> giornoCorrente.getProgetto().getId() == idProgetto);
            }
        }
        return calendario;
    }

    public static Calendario rimuoviProgettoCalendarioConDate(Calendario calendario, Progetto progetto, LocalDate dataDa, LocalDate dataA){

        Iterator<Anno> itAnni = ordinaAnni(calendario.getAnni()).stream().iterator();

        while (itAnni.hasNext()) {

            Anno           annoCorrente = itAnni.next();
            Iterator<Mese> itMesi       = ordinaMesi(annoCorrente.getMesi()).stream().iterator();

            while (itMesi.hasNext()) {
                itMesi.next().getDays().removeIf(
                    giornoCorrente -> Objects.equals(giornoCorrente.getProgetto().getId(), progetto.getId())
                        && (!(giornoCorrente.getData().isBefore(dataDa) || giornoCorrente.getData().isAfter(dataA)))
                );
            }
        }
        return calendario;
    }

    public static Calendario aggiornaCalendario(Calendario calendario, Anno anno, Mese mese, Giorno giornoInizio, AggiornaTimesheet aggiornamento){

        Iterator<Anno> itAnni = ordinaAnni(calendario.getAnni()).stream().iterator();

        while (itAnni.hasNext()) {
            Anno annoCorrente = itAnni.next();
            if (Objects.equals(anno.getAnno(), annoCorrente.getAnno())) {
                Iterator<Mese> itMesi = ordinaMesi(annoCorrente.getMesi()).stream().iterator();
                while (itMesi.hasNext()) {
                    Mese meseCorrente = itMesi.next();
                    if (Objects.equals(mese.getValue(), meseCorrente.getValue())) {
                        Iterator<Giorno> itGiorni = ordinaGiorni(meseCorrente.getDays()).stream().iterator();
                        while (itGiorni.hasNext()) {
                            Giorno giornoCorrente = itGiorni.next();
                            if (giornoCorrente.getGiorno() >= giornoInizio.getGiorno() && null != aggiornamento.getDataFinePeriodo()
                                    && giornoCorrente.getGiorno() <= aggiornamento.getDataFinePeriodo().getDayOfMonth()
                                    && Objects.equals(giornoCorrente.getProgetto().getId(), aggiornamento.getProgetto().getId())
                                    && !giornoCorrente.isFestivo()) {
                                if (null != aggiornamento.getOre()) {
                                    giornoCorrente.setOreOrdinarie(aggiornamento.getOre());
                                } else {
                                    giornoCorrente.setOreOrdinarie(0);
                                }
                                if (null != aggiornamento.getOrePermesso()) {
                                    giornoCorrente.setOrePermesso(aggiornamento.getOrePermesso());
                                } else {
                                    giornoCorrente.setOrePermesso(0);
                                }
                                if (null != aggiornamento.getOreStraordinarie()) {
                                    giornoCorrente.setOreStraordinarie(aggiornamento.getOreStraordinarie());
                                } else {
                                    giornoCorrente.setOreStraordinarie(0);
                                }
                                if (null != aggiornamento.getOreStraordinarieNotturne()) {
                                    giornoCorrente.setOreStraordinarieNotturne(aggiornamento.getOreStraordinarieNotturne());
                                } else {
                                    giornoCorrente.setOreStraordinarieNotturne(0);
                                }
                                if (null != aggiornamento.getFerie()) {
                                    giornoCorrente.setFerie(aggiornamento.getFerie());
                                } else {
                                    giornoCorrente.setFerie(false);
                                }
                                if (null != aggiornamento.getMalattia()) {
                                    giornoCorrente.setMalattia(aggiornamento.getMalattia());
                                } else {
                                    giornoCorrente.setMalattia(false);
                                }
                                if (null != aggiornamento.getPermesso()) {
                                    giornoCorrente.setPermesso(aggiornamento.getPermesso());
                                } else {
                                    giornoCorrente.setPermesso(false);
                                }
                                giornoCorrente.setOreTotali(
                                    (aggiornamento.getOre() != null ? aggiornamento.getOre() : 0) +
                                    (aggiornamento.getOreStraordinarie() != null ? aggiornamento.getOreStraordinarie() : 0) +
                                    (aggiornamento.getOreStraordinarieNotturne() != null ? aggiornamento.getOreStraordinarieNotturne() : 0) +
                                    (aggiornamento.getOrePermesso() != null ? aggiornamento.getOrePermesso() : 0)
                                );
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return calendario;
    }

    public static Calendario aggiornaCalendarioCompletaMese(Calendario calendario, Anno anno, Giorno giornoInizio, AggiornaTimesheet aggiornamento){

        Iterator<Anno> itAnni = ordinaAnni(calendario.getAnni()).stream().iterator();

        while (itAnni.hasNext()) {
            Anno annoCorrente = itAnni.next();
            if (Objects.equals(anno.getAnno(), annoCorrente.getAnno())) {
                Iterator<Mese> itMesi = ordinaMesi(annoCorrente.getMesi()).stream().iterator();
                while (itMesi.hasNext()) {
                    Mese meseCorrente = itMesi.next();
                    if (meseCorrente.getValue() == aggiornamento.getData().getMonthValue()) {
                        Iterator<Giorno> itGiorni = ordinaGiorni(meseCorrente.getDays()).stream().iterator();
                        while (itGiorni.hasNext()) {
                            Giorno giornoCorrente = itGiorni.next();
                            if (giornoCorrente.getGiorno() >= giornoInizio.getGiorno()
                                    && Objects.equals(giornoCorrente.getProgetto().getId(), aggiornamento.getProgetto().getId())
                                    && !giornoCorrente.isFestivo()) {
                                if (null != aggiornamento.getOre()) {
                                    giornoCorrente.setOreOrdinarie(aggiornamento.getOre());
                                } else {
                                    giornoCorrente.setOreOrdinarie(0);
                                }
                                if (null != aggiornamento.getOrePermesso()) {
                                    giornoCorrente.setOrePermesso(aggiornamento.getOrePermesso());
                                } else {
                                    giornoCorrente.setOrePermesso(0);
                                }
                                if (null != aggiornamento.getOreStraordinarie()) {
                                    giornoCorrente.setOreStraordinarie(aggiornamento.getOreStraordinarie());
                                } else {
                                    giornoCorrente.setOreStraordinarie(0);
                                }
                                if (null != aggiornamento.getOreStraordinarieNotturne()) {
                                    giornoCorrente.setOreStraordinarieNotturne(aggiornamento.getOreStraordinarieNotturne());
                                } else {
                                    giornoCorrente.setOreStraordinarieNotturne(0);
                                }
                                if (null != aggiornamento.getFerie()) {
                                    giornoCorrente.setFerie(aggiornamento.getFerie());
                                } else {
                                    giornoCorrente.setFerie(false);
                                }
                                if (null != aggiornamento.getMalattia()) {
                                    giornoCorrente.setMalattia(aggiornamento.getMalattia());
                                } else {
                                    giornoCorrente.setMalattia(false);
                                }
                                if (null != aggiornamento.getPermesso()) {
                                    giornoCorrente.setPermesso(aggiornamento.getPermesso());
                                } else {
                                    giornoCorrente.setPermesso(false);
                                }
                                giornoCorrente.setOreTotali(
                                    (aggiornamento.getOre() != null ? aggiornamento.getOre() : 0) +
                                    (aggiornamento.getOreStraordinarie() != null ? aggiornamento.getOreStraordinarie() : 0) +
                                    (aggiornamento.getOreStraordinarieNotturne() != null ? aggiornamento.getOreStraordinarieNotturne() : 0) +
                                    (aggiornamento.getOrePermesso() != null ? aggiornamento.getOrePermesso() : 0)
                                );
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return calendario;
    }

    public static Calendario aggiornaCalendarioInizioMese(Calendario calendario, Anno anno, AggiornaTimesheet aggiornamento){

        Iterator<Anno> itAnni = ordinaAnni(calendario.getAnni()).stream().iterator();

        while (itAnni.hasNext()) {
            Anno annoCorrente = itAnni.next();
            if (anno.getAnno()==annoCorrente.getAnno()) {
                Iterator<Mese> itMesi = ordinaMesi(annoCorrente.getMesi()).stream().iterator();
                while (itMesi.hasNext()) {
                    Mese meseCorrente = itMesi.next();
                    if (meseCorrente.getValue() == aggiornamento.getDataFinePeriodo().getMonthValue()) {
                        Iterator<Giorno> itGiorni = ordinaGiorni(meseCorrente.getDays()).stream().iterator();
                        while (itGiorni.hasNext()) {
                            Giorno giornoCorrente = itGiorni.next();
                            if (giornoCorrente.getGiorno() <= aggiornamento.getDataFinePeriodo().getDayOfMonth()
                                    && Objects.equals(giornoCorrente.getProgetto().getId(), aggiornamento.getProgetto().getId())
                                    && !giornoCorrente.isFestivo()) {
                                if (null != aggiornamento.getOre()) {
                                    giornoCorrente.setOreOrdinarie(aggiornamento.getOre());
                                } else {
                                    giornoCorrente.setOreOrdinarie(0);
                                }
                                if (null != aggiornamento.getOrePermesso()) {
                                    giornoCorrente.setOrePermesso(aggiornamento.getOrePermesso());
                                } else {
                                    giornoCorrente.setOrePermesso(0);
                                }
                                if (null != aggiornamento.getOreStraordinarie()) {
                                    giornoCorrente.setOreStraordinarie(aggiornamento.getOreStraordinarie());
                                } else {
                                    giornoCorrente.setOreStraordinarie(0);
                                }
                                if (null != aggiornamento.getOreStraordinarieNotturne()) {
                                    giornoCorrente.setOreStraordinarieNotturne(aggiornamento.getOreStraordinarieNotturne());
                                } else {
                                    giornoCorrente.setOreStraordinarieNotturne(0);
                                }
                                if (null != aggiornamento.getFerie()) {
                                    giornoCorrente.setFerie(aggiornamento.getFerie());
                                } else {
                                    giornoCorrente.setFerie(false);
                                }
                                if (null != aggiornamento.getMalattia()) {
                                    giornoCorrente.setMalattia(aggiornamento.getMalattia());
                                } else {
                                    giornoCorrente.setMalattia(false);
                                }
                                if (null != aggiornamento.getPermesso()) {
                                    giornoCorrente.setPermesso(aggiornamento.getPermesso());
                                } else {
                                    giornoCorrente.setPermesso(false);
                                }

                                giornoCorrente.setOreTotali(
                                    aggiornamento.getOre() +
                                    aggiornamento.getOreStraordinarie() +
                                    aggiornamento.getOreStraordinarieNotturne() +
                                    (aggiornamento.getOrePermesso() != null ? aggiornamento.getOrePermesso() : 0)
                                );

                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return calendario;
    }

    public static List<Anno> ordinaAnni(List<Anno> anni){

        return anni.stream()
                .sorted(Comparator.comparing(Anno::getAnno))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Mese> ordinaMesi(List<Mese> mesi){

        return mesi.stream()
                .sorted(Comparator.comparing(Mese::getValue))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Giorno> ordinaGiorni(List<Giorno> giorni){

        return giorni.stream()
                .sorted(Comparator.comparing(Giorno::getGiorno))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static String meseItaliano(String meseInglese){

        return switch (meseInglese) {
            case "JANUARY"   -> "GENNAIO";
            case "FEBRUARY"  -> "FEBBRAIO";
            case "MARCH"     -> "MARZO";
            case "APRIL"     -> "APRILE";
            case "MAY"       -> "MAGGIO";
            case "JUNE"      -> "GIUGNO";
            case "JULY"      -> "LUGLIO";
            case "AUGUST"    -> "AGOSTO";
            case "SEPTEMBER" -> "SETTEMBRE";
            case "OCTOBER"   -> "OTTOBRE";
            case "NOVEMBER"  -> "NOVEMBRE";
            case "DECEMBER"  -> "DICEMBRE";
            default          -> meseInglese;
        };

    }

    public static String inizialeItaliana(String giornoSettimana){

        return switch (giornoSettimana) {
            case "MONDAY"               -> "L";
            case "TUESDAY", "WEDNESDAY" -> "M";
            case "THURSDAY"             -> "G";
            case "FRIDAY"               -> "V";
            case "SATURDAY"             -> "S";
            case "SUNDAY"               -> "D";
            default                     -> giornoSettimana.substring(0, 1);
        };

    }

    public static int contaOre(List<Giorno> giorni){
        int              totale   = 0;
        Iterator<Giorno> itGiorni = giorni.stream().iterator();

        while (itGiorni.hasNext()) {
            Giorno giornoCorrente = itGiorni.next();

            totale += giornoCorrente.getOreTotali();
        }

        return totale;
    }

    public static int calcolaFineMese(int mese, int anno){

        switch (mese) {
            case 4, 6, 9, 11 -> {
                return 30;
            }
            case 2 -> {
                if (bisestile(anno)) {
                    return 29;
                } else {
                    return 28;
                }
            }
            default -> {
                return 31;
            }
        }
    }

    public static boolean bisestile(int anno){
        return (anno > 1584 && ( (anno % 400 == 0) || (anno %4 == 0 && anno % 100 != 0) ) );
    }

    public static boolean  progettoNelMese(Mese mese, Integer idProgetto, Integer idTipologia){
        boolean presente = false;

        if (idTipologia == 1) {
            return true;
        }

        Iterator<Giorno> itGiorni = mese.getDays().stream().iterator();

        while (itGiorni.hasNext()) {
            Giorno giorno = itGiorni.next();

            if ((null != giorno.getProgetto()) &&
                Objects.equals(giorno.getProgetto().getId(), idProgetto))
            {
                presente = true;
                break;
            }
        }

        return presente;
    }
}