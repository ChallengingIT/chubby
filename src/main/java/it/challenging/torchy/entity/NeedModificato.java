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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NeedModificato implements Serializable {

    private Integer id;

    private String progressivo;

    private String descrizione;

    private Double anniEsperienza;

    private Integer numeroRisorse;

    private String location;

    private String note;

    private Set<Skill> skills = new HashSet<>();

    private TipologiaN tipologia;

    private Cliente cliente = new Cliente();

    private KeyPeople keyPeople = new KeyPeople();

    private Owner owner;

    private Integer tipo;

    private Integer priorita;

    private String week;

    private StatoN stato;

    private Integer pubblicazione;

    private Integer screening;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        NeedModificato need = (NeedModificato)o;
        return id != null && Objects.equals(id, need.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
