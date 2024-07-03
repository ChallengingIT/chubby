/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PassRepository extends JpaRepository<Pass, BigDecimal> {

    @Query(value= """
                SELECT p.*
                FROM pass p
                where p.username = ?1
                order by id desc
                limit 1
        """, nativeQuery=true)
    Pass findByUsername(String username);

}