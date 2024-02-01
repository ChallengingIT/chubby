/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

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
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "province")
public class Province implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_province")
    private Integer id;

    @Column(nullable = false, length = 128, name="nome_province")
    private String nomeProvince;

    @Column(nullable = false,  length = 5, name="sigla_province")
    private String siglaProvince;

    @Column(length = 128, name="regione_province")
    private String regioneProvince;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Province province = (Province)o;
        return id != null && Objects.equals(id, province.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
