/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.util;

public class UtilLib {

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

}