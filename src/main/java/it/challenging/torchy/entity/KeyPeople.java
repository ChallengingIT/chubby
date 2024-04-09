/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "key_people")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class KeyPeople implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ruolo;

    @Column
    private String status;

    @Column
    private String nome;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(length = 20)
    private String cellulare;

    @Column(name = "data_creazione")
    private Date dataCreazione;

    @Column(name = "data_ultima_attivita")
    private Date dataUltimaAttivita;

    @Column(length = 2000, name = "note")
    private String note;

    @Column(length = 2000, name = "comunicazioni_recenti")
    private String comunicazioniRecenti;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
        name = "key_people_cliente",
        joinColumns = @JoinColumn(name = "id_key_people", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "key_people_owner",
        joinColumns = @JoinColumn(name = "id_key_people", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner owner;

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
        return false;
      }
      KeyPeople keyPeople = (KeyPeople)o;
      return id != null && Objects.equals(id, keyPeople.id);
    }

    @Override
    public int hashCode() {

      return getClass().hashCode();
    }

}
