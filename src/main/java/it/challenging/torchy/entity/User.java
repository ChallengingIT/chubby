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
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Id
    @Column(nullable = false, length = 50)
    private String username;

    @Column(length = 50)
    private String nome;

    @Column(length = 50)
    private String cognome;

    @Column(length = 50)
    private String email;

    @Column(nullable = false, length = 120)
    private String password;

    @Column(nullable = false)
    private Byte enabled;

    @Column(nullable = false, name="id_azienda")
    private Integer idAzienda;

    @Column(nullable = false, name="expiration_date")
    private LocalDateTime expirationDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "authorities_user",
        joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "username"),
        inverseJoinColumns = @JoinColumn(name = "id_authority", referencedColumnName = "username")
    )
    @ToString.Exclude
    private Authority authority = new Authority();

    public User(String username, String nome, String cognome, String password, Byte enabled, LocalDateTime expirationDate) {

        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.enabled  = enabled;
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User)o;
        return username != null && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}
