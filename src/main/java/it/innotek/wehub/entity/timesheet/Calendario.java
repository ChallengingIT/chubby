/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.timesheet;

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
@Table( name = "calendario")
public class Calendario  implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "calendario_anno",
            joinColumns = @JoinColumn(name = "id_calendario", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_anno", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Anno> anni = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "calendario_staff",
        joinColumns = @JoinColumn(name = "id_calendario", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Staff staff;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Calendario that = (Calendario)o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
