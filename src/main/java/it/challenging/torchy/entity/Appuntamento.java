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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "appuntamento")
public class Appuntamento implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 120)
    private String oggetto;

    @Column
    private OffsetDateTime data;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "appuntamento_owner",
        joinColumns = @JoinColumn(name = "id_appuntamento", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")

    )
    @ToString.Exclude
    private List<Owner> owners;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Appuntamento appuntamento = (Appuntamento)o;
        return id != null && Objects.equals(id, appuntamento.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}