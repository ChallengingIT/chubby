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
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class IntervistaModificato implements Serializable {

    private Integer id;

    private String nome;

    private String cognome;

    private Date dataNascita;

    private String mobilita;

    private String tipologia;

    private Integer anniEsperienza;

    private Date dataColloquio;

    private String recapiti;

    private Owner owner;

    private Owner nextOwner;

    private Integer aderenza;

    private Integer coerenza;

    private Integer motivazione;

    private Integer standing;

    private Integer energia;

    private Integer comunicazione;

    private Integer inglese;

    private Candidato candidato;

    private String descrizioneCandidato;

    private String descrizioneCandidatoUna;

    private String competenze;

    private Integer valutazione;

    private String teamSiNo;

    private String preavviso;

    private String disponibilita;

    private String attuale;

    private String desiderata;

    private String proposta;

    private String dataAVideo;

    private String oraAVideo;

    private TipologiaI tipo;

    private LocalDateTime dataAggiornamento;

    private StatoC stato;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        IntervistaModificato that = (IntervistaModificato)o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
