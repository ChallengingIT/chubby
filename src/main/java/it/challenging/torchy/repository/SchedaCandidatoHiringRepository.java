/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.SchedaCandidatoHiring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedaCandidatoHiringRepository extends JpaRepository<SchedaCandidatoHiring, Integer> {

    void deleteByIdSchedaCandidato(Integer idScheda);
}