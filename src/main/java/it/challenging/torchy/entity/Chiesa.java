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
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "chiese")
public class Chiesa implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false)
    private BigDecimal id;

    @Column(nullable = false, name = "id_chiesa")
    private Integer idChiesa;

    @Column(nullable = false, name = "username", length = 50)
    private String username;

    @Column(nullable = false)
    private Byte ottenuta;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Chiesa chiesa = (Chiesa)o;
        return id != null && Objects.equals(id, chiesa.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}
