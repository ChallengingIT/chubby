/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table( name = "associazione_candidato_need")
public class AssociazioneCandidatoNeed implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "candidato_associazione",
            joinColumns = @JoinColumn(name = "id_associazione", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Candidato candidato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "need_associazione",
            joinColumns = @JoinColumn(name = "id_associazione", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_need", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Need need;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "owner_associazione",
        joinColumns = @JoinColumn(name = "id_associazione", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_owner", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Owner owner;

    @Column(nullable = false, name = "data_modifica")
    private Date dataModifica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stato_associazione",
            joinColumns = @JoinColumn(name = "id_associazione", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_stato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private StatoA stato;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AssociazioneCandidatoNeed that = (AssociazioneCandidatoNeed)o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
