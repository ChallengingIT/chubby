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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "attivita_business")
public class AttivitaBusiness implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_owner")
    private Integer idOwner;

    @Column(length = 45, nullable = false, name = "sigla_owner")
    private String siglaOwner;

    @Column(name="id_contatto")
    private Integer idContatto;

    @Column(length = 90, nullable = false, name = "nome_contatto")
    private String nomeContatto;

    @Column(name="id_cliente")
    private Integer idCliente;

    @Column(length = 90, nullable = false, name = "descrizione_cliente")
    private String descrizioneCliente;

    @Column( name = "data")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime data;

    @Column(length = 45, name = "azione")
    private String azione;

    @Column(name = "id_azione")
    private Integer idAzione;

    @Column(name="id_azione_key_people")
    private Integer idAzioneKeyPeople;

    @Column(length = 4000, name = "note")
    private String note;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AttivitaBusiness appuntamento = (AttivitaBusiness)o;
        return id != null && Objects.equals(id, appuntamento.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}