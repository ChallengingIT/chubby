/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.SchedaCandidato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SchedaCandidatoRepository extends JpaRepository<SchedaCandidato, Integer> {

    @Transactional
    @Modifying
    @Query("delete from scheda_candidato_hiring where id_scheda = ?1")
    void deleteFromSchedaCandidatoHiring(Integer idScheda);

    Page<SchedaCandidato> findByIdCandidato(Integer idCandidato, Pageable p);
}