/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    List<Owner> findByDescrizione(String descrizione);

    @Query(value= """
          SELECT  o.*
          FROM  owner o
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where u.username = ?1
        """,nativeQuery=true)
    Optional<Owner> findByUsername(String username);
}