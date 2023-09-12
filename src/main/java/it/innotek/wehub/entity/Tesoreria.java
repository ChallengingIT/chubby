/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tesoreria")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Tesoreria implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, name = "F_24")
    private String f24;

    @Column(length = 20, name = "iva")
    private String iva;

    @Column(length = 30, name = "totale_spese")
    private String totaleSpese;

    @Column(length = 30, name = "totale_incassi")
    private String totaleIncassi;

    @Column(length = 30, name = "stipendi")
    private String stipendi;

    @Column(length = 30, name = "conto_banca_uno")
    private String contoBancaUno;

    @Column(length = 30, name = "conto_banca_due")
    private String contoBancaDue;

    @Column(length = 11)
    private String mese;

    @Column(length = 10)
    private Integer anno;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tesoreria_cliente",
        joinColumns = @JoinColumn(name = "id_tesoreria", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Cliente> clienti  = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tesoreria_fattura_passiva",
        joinColumns = @JoinColumn(name = "id_tesoreria", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_fattura_passiva", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<FatturazionePassiva> fatture = new ArrayList<>();

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
        return false;
      }
      Tesoreria tesoreria = (Tesoreria)o;
      return id != null && Objects.equals(id, tesoreria.id);
    }

    @Override
    public int hashCode() {

      return getClass().hashCode();
    }

}
