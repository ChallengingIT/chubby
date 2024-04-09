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
import java.util.Objects;

@Entity
@Table( name = "owner")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Owner implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

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
