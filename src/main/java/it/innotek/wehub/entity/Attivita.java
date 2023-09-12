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
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "attivita")
public class Attivita implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 4000)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "attivita_cliente",
        joinColumns = @JoinColumn(name = "id_attivita", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "attivita_key_people",
        joinColumns = @JoinColumn(name = "id_attivita", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_key_people", referencedColumnName = "id")
    )
    @ToString.Exclude
    private KeyPeople keyPeople;

    @Column
    private OffsetDateTime data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tipologia_attivita_attivita",
        joinColumns = @JoinColumn(name = "id_attivita", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipologiaAttivita tipologia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "owner_attivita",
        joinColumns = @JoinColumn(name = "id_attivita", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner owner;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Attivita facolta = (Attivita)o;
        return id != null && Objects.equals(id, facolta.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}