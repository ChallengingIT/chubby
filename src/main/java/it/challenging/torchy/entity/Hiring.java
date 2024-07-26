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
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "hiring")
public class Hiring implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="id_cliente")
    private Integer idCliente;

    @Column(length = 90, name = "denominazione_cliente")
    private String denominazioneCliente;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipo_servizio_hiring",
            joinColumns = @JoinColumn(name = "id_hiring", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_servizio", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<TipoServizio> tipiServizio;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "schede_candidato_hiring",
            joinColumns = @JoinColumn(name = "id_hiring", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_scheda_candidato", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<SchedaCandidato> schedeCandidato;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Hiring candidato = (Hiring)o;
        return id != null && Objects.equals(id, candidato.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
