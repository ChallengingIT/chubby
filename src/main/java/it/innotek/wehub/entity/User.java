/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 120)
    private String password;

    @Column(nullable = false)
    private Byte enabled;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "authorities_user",
        joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_authority", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Authority authority;

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
