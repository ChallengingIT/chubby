/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.timesheet;

import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.TipologiaProgetto;
import it.innotek.wehub.entity.staff.Staff;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table( name = "progetto")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Progetto  implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 90, name="description")
    private String description;

    @Column(name="inizio")
    private LocalDate inizio;

    @Column(name="scadenza")
    private LocalDate scadenza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "progetto_staff",
            joinColumns = @JoinColumn(name = "id_progetto", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "progetto_cliente",
            joinColumns = @JoinColumn(name = "id_progetto", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tipologia_progetto_progetto",
        joinColumns = @JoinColumn(name = "id_progetto", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipologiaProgetto tipologia;

    @Column(name="rate")
    private Integer   rate;

    @Column(name="costo")
    private Integer costo;

    @Column(name="margine")
    private Integer margine;

    @Column(name="margine_perc")
    private Integer marginePerc;

    @Column(name="durata")
    private Integer durata;

    @Column(name="durata_stimata")
    private Integer durataStimata;

    @Column(name="durata_effettiva")
    private Integer durataEffettiva;

    @Column(name="mol_totale")
    private Integer molTotale;

    @Column(name="valore_totale")
    private Integer valoreTotale;

    @Column(length = 4000, name = "note")
    private String note;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Progetto progetto = (Progetto)o;
        return id != null && Objects.equals(id, progetto.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
