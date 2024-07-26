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
@Table( name = "users_candidato")
public class UserCandidato implements Serializable {

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

    @Column(length = 15)
    private String cellulare;

    @Column(length = 50)
    private String residenza;

    @Column(nullable = false, name="expiration_date")
    private LocalDateTime expirationDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "file_user",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "id_file", referencedColumnName = "id")
    )
    @ToString.Exclude
    private File file = new File();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "authorities_user",
        joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "username"),
        inverseJoinColumns = @JoinColumn(name = "id_authority", referencedColumnName = "username")
    )
    @ToString.Exclude
    private Authority authority = new Authority();

    public UserCandidato(String username, String nome, String cognome, String email, String cellulare, String residenza, String password, Byte enabled, LocalDateTime expirationDate) {

        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.enabled  = enabled;
        this.expirationDate = expirationDate;
        this.email = email;
        this.cellulare = cellulare;
        this.residenza = residenza;
    }

    public UserCandidato(String username, String nome, String cognome, String password, Byte enabled, String email) {

        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.enabled  = enabled;
        this.email  = email;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserCandidato user = (UserCandidato)o;
        return username != null && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}
