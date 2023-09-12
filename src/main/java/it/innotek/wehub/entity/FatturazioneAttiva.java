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
@Table( name = "fatturazione_attiva")
public class FatturazioneAttiva implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_emissione")
    private Date dataEmissione;

    @Column(length = 45)
    private String termine;

    @Column(name = "data_scadenza")
    private Date dataScadenza;

    @Column(length = 10)
    private String tariffa;

    @Column(length = 10, name="giorni_lavorati")
    private String giorniLavorati;

    @Column(length = 10)
    private String imponibile;

    @Column(length = 10, name="totale_con_iva")
    private String totaleConIva;

    @Column(length = 30, name="n_fattura")
    private String nFattura;

    @Column(length = 30)
    private String consulente;

    @Column(length = 30)
    private String oggetto;

    @Column(length = 30)
    private String descrizione;

    @Column(length = 30)
    private String oda;

    @Column(length = 4000)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stato_fatturazione_attiva",
            joinColumns = @JoinColumn(name = "id_fattura", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_stato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private StatoFA stato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "fattura_cliente",
            joinColumns = @JoinColumn(name = "id_fattura", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Cliente cliente;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        FatturazioneAttiva that = (FatturazioneAttiva)o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
