/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import it.innotek.wehub.entity.staff.Staff;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "facolta")
public class Facolta implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 45)
    private String descrizione;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "facolta_candidato",
        joinColumns = @JoinColumn(name = "id_facolta", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Candidato> candidati = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "facolta_staff",
        joinColumns = @JoinColumn(name = "id_facolta", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Staff> staff = new ArrayList<>();

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Facolta facolta = (Facolta)o;
        return id != null && Objects.equals(id, facolta.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
