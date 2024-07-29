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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "users")
public class User implements UserDetails {

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

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String nome, String cognome, String password, Byte enabled, LocalDateTime expirationDate) {

        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.enabled  = enabled;
        this.expirationDate = expirationDate;
    }

    public User(String username, String nome, String cognome, String password, Byte enabled, String email) {

        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.enabled  = enabled;
        this.email  = email;
    }

    public User(String username, String nome, String cognome, String email, String cellulare, String residenza, String password, Byte enabled, LocalDateTime expirationDate) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.expirationDate = expirationDate;
        this.enabled  = enabled;
        this.email  = email;
        this.cellulare = cellulare;
        this.residenza = residenza;
        this.password = password;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
