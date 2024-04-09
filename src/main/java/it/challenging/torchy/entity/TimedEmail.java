/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table( name = "timed_email")
@NoArgsConstructor
public class TimedEmail implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_owner", length = 45)
    private String nomeOwner;

    @Column(name = "cognome_owner", length = 45)
    private String cognomeOwner;

    @Column(name = "email_owner", length = 45)
    private String emailOwner;

    @Column(name = "nome_candidato", length = 45)
    private String nomeCandidato;

    @Column(name = "cognome_candidato", length = 45)
    private String cognomeCandidato;

    @Column(name = "email_candidato", length = 45)
    private String emailCandidato;

    @Column(name = "cell_candidato", length = 45)
    private String cellCandidato;

    @Column(name = "data_aggiornamento")
    private LocalDateTime dataAggiornamento;

    @Column
    private Integer inviata;

    public TimedEmail(String nomeOwner, String cognomeOwner, String emailOwner, String nomeCandidato, String cognomeCandidato, String emailCandidato, String cellCandidato, LocalDateTime dataAggiornamento, Integer inviata) {

        this.nomeOwner         = nomeOwner;
        this.cognomeOwner      = cognomeOwner;
        this.emailOwner        = emailOwner;
        this.nomeCandidato     = nomeCandidato;
        this.cognomeCandidato  = cognomeCandidato;
        this.emailCandidato    = emailCandidato;
        this.cellCandidato     = cellCandidato;
        this.dataAggiornamento = dataAggiornamento;
        this.inviata           = inviata;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        TimedEmail that = (TimedEmail)o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
