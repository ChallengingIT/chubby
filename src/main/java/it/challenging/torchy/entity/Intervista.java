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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table( name = "intervista")
public class Intervista implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45)
    private String nome;

    @Column(length = 45)
    private String cognome;

    @Column( name = "data_nascita")
    private Date dataNascita;

    @Column(length = 45)
    private String mobilita;

    @Column(length = 45)
    private String tipologia;

    @Column(name="anni_esperienza")
    private Double anniEsperienza;

    @Column( name = "data_colloquio")
    private Date dataColloquio;

    @Column(length = 45)
    private String recapiti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "intervista_owner",
        joinColumns = @JoinColumn(name = "id_intervista", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "intervista_next_owner",
        joinColumns = @JoinColumn(name = "id_intervista", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner nextOwner;

    @Column(length = 2, name="aderenza_posizione")
    private Integer aderenza;

    @Column(length = 2, name="coerenza_percorso")
    private Integer coerenza;

    @Column(length = 2,  name="motivazione_posizione")
    private Integer motivazione;

    @Column(length = 2)
    private Integer standing;

    @Column(length = 2,  name="energia")
    private Integer energia;

    @Column(length = 2, name="comunicazione")
    private Integer comunicazione;

    @Column(length = 2, name="inglese")
    private Integer inglese;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "candidato_intervista",
        joinColumns = @JoinColumn(name = "id_intervista", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Candidato candidato;

    @Column(length = 120, name="descrizione_candidato")
    private String descrizioneCandidato;

    @Column(length = 45, name="descrizione_candidato_una")
    private String descrizioneCandidatoUna;

    @Column(length = 2, name = "competenze")
    private Integer competenze;

    @Column(length = 2, name="valutazione_competenze")
    private Integer valutazione;

    @Column(length = 2, name="team_si_no")
    private Integer teamSiNo;

    @Column(length = 45)
    private String preavviso;

    @Column(length = 45)
    private String disponibilita;

    @Column(length = 90)
    private String attuale;

    @Column(length = 90)
    private String desiderata;

    @Column(length = 90)
    private String proposta;

    @Column(length = 45)
    private String dataAVideo;

    @Column(length = 45)
    private String oraAVideo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipologia_intervista",
            joinColumns = @JoinColumn(name = "id_intervista", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipologiaI tipo;

    @Column( name = "ora_aggiornamento")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataAggiornamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stato_intervista",
            joinColumns = @JoinColumn(name = "id_intervista", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_stato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private StatoC stato;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Intervista that = (Intervista)o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
