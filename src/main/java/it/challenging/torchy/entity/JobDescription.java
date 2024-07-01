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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table( name = "job_description")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class JobDescription implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2, name="anni_esperienza")
    private Double anniEsperienza;

    @Column(length = 45)
    private String location;

    @Column(length = 4000, name = "note")
    private String note;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "skill_job_description",
            joinColumns = @JoinColumn(name = "id_job_description", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_skill", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Set<Skill> skills = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipologia_job_description",
            joinColumns = @JoinColumn(name = "id_job_description", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Tipologia tipologia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "modalita_lavoro_job_description",
            joinColumns = @JoinColumn(name = "id_job_description", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_modalita_lavoro", referencedColumnName = "id")
    )
    @ToString.Exclude
    private ModalitaLavoro modalitaLavoro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "modalita_impiego_job_description",
            joinColumns = @JoinColumn(name = "id_job_description", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_modalita_impiego", referencedColumnName = "id")
    )
    @ToString.Exclude
    private ModalitaImpiego modalitaImpiego;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "need_job_description",
            joinColumns = @JoinColumn(name = "id_job_description", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Need need;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        JobDescription need = (JobDescription)o;
        return id != null && Objects.equals(id, need.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
