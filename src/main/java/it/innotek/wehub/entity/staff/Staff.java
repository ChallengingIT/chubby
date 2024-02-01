/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.staff;

import it.innotek.wehub.entity.*;
import it.innotek.wehub.entity.timesheet.Calendario;
import it.innotek.wehub.entity.timesheet.Progetto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "staff")
public class Staff implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false, name = "nome")
    private String nome;

    @Column(length = 45, nullable = false, name = "cognome")
    private String cognome;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(length = 20)
    private String cellulare;

    @Column(length = 110, name = "residenza")
    private String citta;

    @Column(name = "data_nascita")
    private Date dataNascita;

    @Column(length = 110,name = "luogo_nascita")
    private String luogoNascita;

    @Column(name = "data_inizio")
    private Date dataInizio;

    @Column(name = "data_scadenza")
    private Date dataScadenza;

    @Column(name="anni_esperienza")
    private Double anniEsperienza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "livello_staff",
            joinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_livello", referencedColumnName = "id")
    )
    @ToString.Exclude
    private LivelloScolastico livelloScolastico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipologia_staff",
            joinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Tipologia tipologia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipo_contratto_staff",
            joinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_contratto", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipologiaContratto tipologiaContratto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "skill_staff",
            joinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_skill", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Set<Skill> skills = new HashSet<>();

    @Column(length = 2000, name = "note")
    private String note;

    @Column(length = 100, name = "ral_tariffa")
    private String ral;

    @Column(length = 20, name = "stipendio")
    private String stipendio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "facolta_staff",
            joinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_facolta", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Facolta facolta;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "calendario_staff",
            joinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_calendario", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Calendario timesheet;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "progetto_staff",
        joinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_progetto", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Progetto> progetti = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "file_staff",
        joinColumns = @JoinColumn(name = "id_staff", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_file", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<File> files = new ArrayList<>();

    @Column(length = 30, name = "cod_fiscale")
    private String codFiscale;

    @Column(length = 45, name = "iban")
    private String iban;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Staff staff = (Staff)o;
        return id != null && Objects.equals(id, staff.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
