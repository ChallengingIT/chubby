/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class OccorrenzeOwner implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    private String descrizione;

    private Integer occorrenze;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
