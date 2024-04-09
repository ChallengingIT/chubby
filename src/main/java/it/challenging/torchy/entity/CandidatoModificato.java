/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CandidatoModificato implements Serializable {

    private Integer id;

    private String nome;

    private String cognome;

    private String email;

    private Tipologia tipologia;

    private File file;

    private StatoC stato;

    private Date dataUltimoContatto;

    private Double rating;

    private String ral;

    private String note;

    private Owner owner;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CandidatoModificato candidato = (CandidatoModificato)o;
        return id != null && Objects.equals(id, candidato.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
