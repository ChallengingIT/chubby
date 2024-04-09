/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ClienteSelect implements Serializable {

    private Integer id;

    private String denominazione;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ClienteSelect cliente = (ClienteSelect)o;
        return id != null && Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
