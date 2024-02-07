/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.timesheet;

import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.TipologiaProgetto;
import it.innotek.wehub.entity.staff.StaffModificato;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProgettoModificato implements Serializable {

    private Integer id;

    private String description;

    private LocalDate inizio;

    private LocalDate scadenza;

    private TipologiaProgetto tipologia;

    private StaffModificato staff;

    private Integer rate;

    private Integer costo;

    private Integer margine;

    private Integer marginePerc;

    private Integer durata;

    private Integer durataStimata;

    private Integer durataEffettiva;

    private Integer molTotale;

    private Integer valoreTotale;

    private String note;

    private Cliente cliente;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ProgettoModificato progetto = (ProgettoModificato)o;
        return id != null && Objects.equals(id, progetto.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
