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
@Table(name = "Prospection")
public class Prospection implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name = "oggetto")
    private String oggetto;

    @Column(name = "partecipanti")
    private String partecipanti;

    @Column(name = "data_incontro")
    private Date dataIncontro;

    @Column(name = "stato")
    private String stato;

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
        return false;
      }
      Prospection that = (Prospection)o;
      return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

      return getClass().hashCode();
    }

}
