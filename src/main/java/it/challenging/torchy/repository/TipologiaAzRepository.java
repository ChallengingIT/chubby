/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.TipologiaAz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipologiaAzRepository extends JpaRepository<TipologiaAz, Integer> {

    List<TipologiaAz> findAllByOrderByIdAsc();
}