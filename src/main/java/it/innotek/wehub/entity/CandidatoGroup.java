/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CandidatoGroup implements Serializable {

    private Long record;

    private List<CandidatoModificato> candidati;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CandidatoGroup candidato = (CandidatoGroup)o;
        return record != null && Objects.equals(record, candidato.record);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}