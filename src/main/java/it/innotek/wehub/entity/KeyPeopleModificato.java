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
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class KeyPeopleModificato implements Serializable {

    private Integer id;

    private String ruolo;

    private String status;

    private String nome;

    private String email;

    private String cellulare;

    private Date dataCreazione;

    private Date dataUltimaAttivita;

    private String note;

    private String comunicazioniRecenti;

    private Cliente cliente;

    private Owner owner;

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
        return false;
      }
      KeyPeopleModificato keyPeople = (KeyPeopleModificato)o;
      return id != null && Objects.equals(id, keyPeople.id);
    }

    @Override
    public int hashCode() {

      return getClass().hashCode();
    }

}
