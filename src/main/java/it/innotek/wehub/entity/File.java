/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.*;
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
@Table( name = "file")
public class File implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 90)
    private String descrizione;

    @Column(length = 45)
    private String tipo;

    @Lob
    private byte[] data;

    @Column( name = "data_inserimento")
    private Date dataInserimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "file_candidato",
            joinColumns = @JoinColumn(name = "id_file", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_candidato", referencedColumnName = "id")
    )
    @ToString.Exclude
    public Candidato candidato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipologia_file",
            joinColumns = @JoinColumn(name = "id_file", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    )
    @ToString.Exclude
    private TipologiaF tipologia;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        File file = (File)o;
        return id != null && Objects.equals(id, file.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
