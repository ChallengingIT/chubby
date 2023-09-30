/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import it.innotek.wehub.entity.timesheet.Progetto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "cliente")
public class Cliente  implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 90)
    private String denominazione;

    @Column(length = 90)
    private String referente;

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

    @Column(length = 45)
    private String telefono;

    @Column(length = 45)
    private String fax;

    @Column(unique = true, length = 45)
    private String email;

    @Column(length = 45)
    private String pec;

    @Column(length = 45,name="codice_destinatario")
    private String codiceDestinatario;

    @Column(name="pa")
    private boolean pa;

    @Column(length = 45,name="codice_pa")
    private String codicePa;

    @Column(length = 90)
    private String sito;

    @Column(name="antiriciclaggio")
    private boolean antiriciclaggio;

    @Column(name="privacy")
    private boolean privacy;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cliente_prospection",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_prospection", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Prospection prospection;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "progetto_cliente",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_progetto", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Progetto> progetto;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "cliente_qm",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_qm", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Qm qm;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "need_cliente",
            joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Need> needs = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "key_people_cliente",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_key_people", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<KeyPeople> keyPeoples;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "attivita_cliente",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_attivita", referencedColumnName = "id")

    )
    @ToString.Exclude
    private List<Attivita> attivita;

    @Column(length = 20, name = "guadagno")
    private String guadagno;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "fattura_cliente",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_fattura", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<FatturazioneAttiva> fatture;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tesoreria_cliente",
        joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_tesoreria", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Tesoreria> tesorerie;

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
