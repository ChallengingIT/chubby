/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "fornitore")
public class Fornitore implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 90)
    private String denominazione;

    @Column(length = 45)
    private String referente;

    @Column(length = 45)
    private String email;

    @Column(length = 20)
    private String cellulare;

    @Column(length = 45)
    private String codice;

    @Column(length = 45)
    private String citta;

    @Column(length = 30)
    private String pi;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Fornitore fornitore = (Fornitore)o;
        return id != null && Objects.equals(id, fornitore.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
