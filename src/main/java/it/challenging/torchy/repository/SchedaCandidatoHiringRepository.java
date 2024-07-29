/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.SchedaCandidatoHiring;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedaCandidatoHiringRepository extends JpaRepository<SchedaCandidatoHiring, Integer> {

    @Transactional
    void deleteByIdSchedaCandidato(Integer idScheda);
}