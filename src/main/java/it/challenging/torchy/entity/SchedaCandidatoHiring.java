/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "schede_candidato_hiring")
public class SchedaCandidatoHiring implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @Column(name="id_hiring")
    private Integer idHiring;

    @Column(name="id_scheda_candidato")
    private Integer idSchedaCandidato;

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
