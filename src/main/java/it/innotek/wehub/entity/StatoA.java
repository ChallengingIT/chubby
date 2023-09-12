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
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "statoa")
public class StatoA implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 45)
    private String descrizione;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "stato_associazione",
        joinColumns = @JoinColumn(name = "id_stato", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_associazione", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<AssociazioneCandidatoNeed> associazioni;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StatoA statoA = (StatoA)o;
        return id != null && Objects.equals(id, statoA.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
