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
@Table( name = "azioni")
public class AzioneKeyPeople implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "data_modifica")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataModifica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipologia_azione",
            joinColumns = @JoinColumn(name = "id_azione", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipologiaAz tipologia;

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
        AzioneKeyPeople that = (AzioneKeyPeople)o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
