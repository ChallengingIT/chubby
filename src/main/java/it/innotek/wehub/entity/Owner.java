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
import java.util.List;
import java.util.Objects;

@Entity
@Table( name = "owner")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Owner implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 45)
    private String descrizione;

    @Column(nullable = false, unique = true, length = 45)
    private String nome;

    @Column(nullable = false, unique = true, length = 45)
    private String cognome;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "owner_attivita",
        joinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_attivita", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Attivita> attivita;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "intervista_owner",
        joinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_intervista", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Intervista> interviste;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "intervista_next_owner",
        joinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_intervista", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Intervista> nextInterviste;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "owner_associazione",
        joinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_associazione", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<AssociazioneCandidatoNeed> associazioni;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "need_owner",
        joinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Need> needs;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Owner owner = (Owner)o;
        return id != null && Objects.equals(id, owner.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
