/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.StatoA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatoARepository extends JpaRepository<StatoA, Integer> {

    @Query(value= """
         SELECT *
         FROM statoa
         order by id= 1 desc, id=13 desc, id=14 desc, id=12 desc, id=2 desc, id=3 desc, id=8 desc, id=9 desc,
         id=15 desc, id=16 desc, id=17 desc, id=11 desc
        """, nativeQuery=true)
    List<StatoA> findAllOrdered();

}