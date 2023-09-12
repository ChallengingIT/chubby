/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "fatturazione_passiva")
public class FatturazionePassiva implements Serializable{

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_fattura")
    private Date dataFattura;

    @Column(length = 45)
    private String tipologia;

    @Column(length = 30)
    private String descrizione;

    @Column
    private Date scadenza;

    @Column(length = 10)
    private String imponibile;

    @Column(length = 10)
    private String iva;

    @Column(length = 10)
    private String importo;

    @Column(length = 30)
    private String riferimenti;

    @Column(length = 4000)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stato_fatturazione_passiva",
            joinColumns = @JoinColumn(name = "id_fattura", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_stato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private StatoFP stato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "fornitore_fattura",
            joinColumns = @JoinColumn(name = "id_fattura", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_fornitore", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Fornitore fornitore;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        FatturazionePassiva that = (FatturazionePassiva)o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
