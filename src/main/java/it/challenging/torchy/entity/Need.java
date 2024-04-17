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
@Table( name = "need")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Need implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String progressivo;

    @Column(nullable = false, length = 200)
    private String descrizione;

    @Column(length = 2, name="anni_esperienza")
    private Integer anniEsperienza;

    @Column(length = 2, name="numero_risorse")
    private Integer numeroRisorse;

    @Column(name = "data_richiesta")
    private Date dataRichiesta;

    @Column(length = 45)
    private String location;

    @Column(length = 4000, name = "note")
    private String note;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "skill_need",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_skill", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Set<Skill> skills = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipologia_need",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipologiaN tipologia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "need_cliente",
        joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Cliente cliente = new Cliente();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "need_owner",
        joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "need_keypeople",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_keypeople", referencedColumnName = "id")
    )
    @ToString.Exclude
    private KeyPeople keyPeople = new KeyPeople();

    @Column(length = 1, name="tipo")
    private Integer tipo;

    @Column(length = 2, nullable = false, name="priorita")
    private Integer priorita;

    @Column(length = 15, name="week")
    private String week;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stato_need",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_stato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private StatoN stato;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "need_candidato",
        joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Candidato> candidati;

    @Column(length = 1, name="pubblicazione")
    private Integer pubblicazione;

    @Column(length = 1, name="screening")
    private Integer screening;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Need need = (Need)o;
        return id != null && Objects.equals(id, need.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
