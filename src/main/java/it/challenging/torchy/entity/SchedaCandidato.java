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
import java.sql.Date;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "scheda_candidato")
public class SchedaCandidato implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="id_candidato")
    private Integer idCandidato;

    @Column(length = 45, nullable = false, name = "nome_candidato")
    private String nomeCandidato;

    @Column(length = 45, nullable = false, name = "cognome_candidato")
    private String cognomeCandidato;

    @Column(length = 90)
    private String descrizione;

    @Column(name = "data_fatturazione")
    private Date dataFatturazione;

    @Column(name = "inizio_attivita")
    private Date inizioAttivita;

    @Column(name = "fine_attivita")
    private Date fineAttivita;

    @Column(length = 45)
    private String durata;

    @Column
    private Double economics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "termine_pagamento_scheda",
            joinColumns = @JoinColumn(name = "id_scheda_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_termine", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TerminePagamento terminePagamento;

    @Column
    private Double fee;

    @Column
    private Double rate;

    @Column(name = "giorni_lavorati")
    private Double giorniLavorati;

    @Column(name = "canone_mensile")
    private Double canoneMensile;

    @Column(name = "importo_fatturato")
    private Double fatturato;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        SchedaCandidato candidato = (SchedaCandidato)o;
        return id != null && Objects.equals(id, candidato.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
