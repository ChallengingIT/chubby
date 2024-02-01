/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.timesheet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "giorno")
public class Giorno  implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 2)
    private Integer giorno;

    @Column(length = 2)
    private String iniziale;

    @Column(length = 2, name="ore_ordinarie")
    private Integer oreOrdinarie;

    @Column(length = 2, name="ore_straordinarie")
    private Integer oreStraordinarie;

    @Column(length = 2, name="ore_straordinarie_notturne")
    private Integer oreStraordinarieNotturne;

    @Column(length = 2, name="data")
    private LocalDate data;

    @Column(length = 2, name="ore_totali")
    private Integer oreTotali;

    @Column(name="festivo")
    private boolean isFestivo;

    @Column(name="ferie")
    private boolean isFerie;

    @Column(name="malattia")
    private boolean isMalattia;

    @Column(name="permesso")
    private boolean isPermesso;

    @Column(length = 2, name="ore_permesso")
    private Integer orePermesso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "progetto_giorno",
        joinColumns = @JoinColumn(name = "id_giorno", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_progetto", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Progetto progetto;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Giorno giorno = (Giorno)o;
        return id != null && Objects.equals(id, giorno.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
