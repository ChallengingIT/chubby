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
@Table( name = "candidato")
public class Candidato implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false, name = "nome")
    private String nome;

    @Column(length = 45, nullable = false, name = "cognome")
    private String cognome;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(length = 20)
    private String cellulare;

    @Column(length = 45, name = "residenza")
    private String citta;

    @Column(name = "data_nascita")
    private Date dataNascita;

    @Column(name="anni_esperienza")
    private Double anniEsperienza;

    @Column(name="anni_esperienza_ruolo")
    private Double anniEsperienzaRuolo;

    @Column(length = 1, name="modalita")
    private Integer modalita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipo_ricerca_candidato",
            joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_ricerca", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipoRicerca ricerca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipo_candidatura_candidato",
            joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_candidatura", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipoCandidatura candidatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "livello_candidato",
            joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_livello", referencedColumnName = "id")
    )
    @ToString.Exclude
    private LivelloScolastico livelloScolastico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipologia_candidato",
            joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Tipologia tipologia;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "file_candidato",
        joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_file", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<File> files = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "skill_candidato",
            joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_skill", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Set<Skill> skills = new HashSet<>();

    @Column(length = 4000, name = "note")
    private String note;

    @Column(length = 45)
    private String disponibilita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stato_candidato",
            joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_stato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private StatoC stato;

    @Column(nullable = false, name = "data_ultimo_contatto")
    private Date dataUltimoContatto;

    @Column(length = 10, name="rating")
    private Double rating;

    @Column(length = 100, name = "ral_tariffa")
    private String ral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipo_candidato",
            joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Tipo tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "facolta_candidato",
            joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_facolta", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Facolta facolta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "candidato_owner",
        joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "fornitore_candidato",
        joinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_fornitore", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Fornitore fornitore;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Candidato candidato = (Candidato)o;
        return id != null && Objects.equals(id, candidato.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
