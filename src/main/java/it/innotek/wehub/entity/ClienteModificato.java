/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
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

    private String cap;

    private String citta;

    private String provincia;

    private String email;

    private String pec;

    private String sito;

    private String note;

    private String tipologia;

    private Integer status;

    private Owner owner;

    private String sedeOperativa;

    private String settoreMercato;


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
