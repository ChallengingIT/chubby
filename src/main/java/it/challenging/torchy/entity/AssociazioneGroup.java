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
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AssociazioneGroup implements Serializable {

    private Long record;

    private List<AssociazioneModificata> associazioni;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AssociazioneGroup candidato = (AssociazioneGroup)o;
        return record != null && Objects.equals(record, candidato.record);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
