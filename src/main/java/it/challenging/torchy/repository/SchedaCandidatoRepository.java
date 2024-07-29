/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.SchedaCandidato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedaCandidatoRepository extends JpaRepository<SchedaCandidato, Integer> {
    Page<SchedaCandidato> findByIdCandidato(Integer idCandidato, Pageable p);
}