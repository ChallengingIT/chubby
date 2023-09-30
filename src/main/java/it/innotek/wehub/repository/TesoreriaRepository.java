/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Tesoreria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TesoreriaRepository extends JpaRepository<Tesoreria,Integer> {

    Tesoreria findByMeseAndAnno(String mese, Integer anno);

    @Query("SELECT coalesce(max(te.id), 0) FROM Tesoreria te")
    Integer findMaxId();
}