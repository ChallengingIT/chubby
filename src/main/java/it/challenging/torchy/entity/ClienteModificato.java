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
public class ClienteModificato implements Serializable {

    private Integer id;

    private String denominazione;

    private String pi;

    private String cf;

    private String indirizzo;

    private Date dataScadenzaContratto;

    private String cap;

    private String citta;

    private String provincia;

    private String email;

    private String pec;

    private String sito;

    private String note;

    private String tipologia;

    private Integer status;

    private Integer potenzialita;

    private Integer semplicita;

    private Double ida;

    private Owner owner;

    private String sedeOperativa;

    private String settoreMercato;

    private String logo;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ClienteModificato cliente = (ClienteModificato)o;
        return id != null && Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
