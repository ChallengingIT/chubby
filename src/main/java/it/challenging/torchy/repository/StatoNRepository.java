/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.StatoN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatoNRepository extends JpaRepository<StatoN, Integer> {

    @Query(value= """
         SELECT *
         FROM staton
         order by id= 1 desc, id=7 desc, id=3 desc, id=6 desc, id=8 desc, id=2 desc, id=4 desc, id=5 desc
        """, nativeQuery=true)
    List<StatoN> findAllOrdered();
}