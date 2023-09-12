/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.timesheet;

import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "mese")
public class Mese implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="value")
    private Integer value;

    @Column(length = 45, name="description")
    private String description;

    private boolean inviato;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "mese_giorno",
            joinColumns = @JoinColumn(name = "id_mese", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_giorno", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Giorno> days = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(
        name = "anno_mese",
        joinColumns = @JoinColumn(name = "id_mese", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_anno", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Anno anno;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Mese mese = (Mese)o;
        return id != null && Objects.equals(id, mese.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
