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
@Table( name = "anno")
public class Anno  implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="anno")
    private Integer anno;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "anno_mese",
            joinColumns = @JoinColumn(name = "id_anno", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_mese", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Mese> mesi = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(
        name = "calendario_anno",
        joinColumns = @JoinColumn(name = "id_anno", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_calendario", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Calendario calendario;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Anno anno = (Anno)o;
        return id != null && Objects.equals(id, anno.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
