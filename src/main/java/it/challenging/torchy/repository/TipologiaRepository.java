/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Tipologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipologiaRepository extends JpaRepository<Tipologia, Integer> {

    List<Tipologia> findByFunzione_IdOrderByDescrizioneAsc(Integer id);

    List<Tipologia> findAllByOrderByDescrizioneAsc();

}