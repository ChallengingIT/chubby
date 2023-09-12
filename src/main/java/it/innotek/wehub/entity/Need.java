/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
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

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String descrizione;

    @Column(length = 2, name="anni_esperienza")
    private Integer anniEsperienza;

    @Column(length = 2, name="numero_risorse")
    private Integer numeroRisorse;

    @Column(name = "data_richiesta")
    private Date dataRichiesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "livello_need",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_livello", referencedColumnName = "id")
    )
    @ToString.Exclude
    private LivelloScolastico livelloScolastico;

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
            name = "need_cliente",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipologia_need",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipologiaN tipologia;

    @Column(length = 1, name="tipo")
    private Integer tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "need_owner",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner owner;

    @Column(length = 2, nullable = false, name="priorita")
    private Integer priorita;

    @Column(length = 90, name="job_description")
    private String jobDescription;

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
    private List<Candidato> candidati = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "need_associazione",
            joinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_associazione", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<AssociazioneCandidatoNeed> associazioni = new ArrayList<>();

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
