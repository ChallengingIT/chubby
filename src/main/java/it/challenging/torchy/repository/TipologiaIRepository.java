/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.TipologiaI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipologiaIRepository extends JpaRepository<TipologiaI, Integer> {

    List<TipologiaI> findAllByOrderByIdAsc();
}