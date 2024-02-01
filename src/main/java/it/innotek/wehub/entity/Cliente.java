/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "cliente")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 90)
    private String denominazione;

    @Column(length = 45, name = "sede_operativa")
    private String sedeOperativa;

    @Column(length = 45, name = "sede_legale")
    private String sedeLegale;

    @Column(length = 45)
    private String pi;

    @Column(length = 45)
    private String cf;

    @Column(length = 45)
    private String indirizzo;

    @Column(length = 6)
    private String cap;

    @Column(length = 45)
    private String citta;

    @Column(length = 45, nullable = false)
    private String provincia;

    @Column(unique = true, length = 45)
    private String email;

    @Column(length = 45)
    private String pec;

    @Column(length = 45,name="codice_destinatario")
    private String codiceDestinatario;

    @Column(length = 45,name="codice_pa")
    private String codicePa;

    @Column(length = 90)
    private String sito;

    @Column(length = 2000, name = "note")
    private String note;

    @Column(name="paese")
    private String paese;

    @Column(name="tipologia")
    private String tipologia;

    @Column(name="status")
    private Integer status;

    @Column(name="settoreMercato")
    private String settoreMercato;

    @Column(length = 2000, name = "azioni_commerciali")
    private String azioniCommerciali;

    @Column(length = 2000, name = "comunicazioni_need")
    private String comunicazioniNeed;

    @Column(length = 2000, name = "note_trattative")
    private String noteTrattative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cliente_owner",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tesoreria_cliente",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_tesoreria", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Tesoreria> tesorerie  = new ArrayList<>();

    @Column(length = 20, name = "guadagno")
    private String guadagno;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Cliente cliente = (Cliente)o;
        return id != null && Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
