/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.staff;

import it.innotek.wehub.entity.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.sql.Date;
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class StaffModificato implements Serializable {

    private Integer id;

    private String nome;

    private String cognome;

    private String email;

    private String note;

    private String cellulare;

    private String citta;

    private Date dataNascita;

    private String luogoNascita;

    private Date dataInizio;

    private Date dataScadenza;

    private Double anniEsperienza;

    private LivelloScolastico livelloScolastico;

    private Tipologia tipologia;

    private TipologiaContratto tipologiaContratto;

    private Set<Skill> skills = new HashSet<>();

    private String ral;

    private String stipendio;

    private Facolta facolta;

    private List<File> files = new ArrayList<>();

    private String codFiscale;

    private String iban;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StaffModificato staff = (StaffModificato)o;
        return id != null && Objects.equals(id, staff.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
