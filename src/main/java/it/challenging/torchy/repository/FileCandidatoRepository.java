/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.FileCandidato;
import it.challenging.torchy.entity.FileCandidatoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileCandidatoRepository extends JpaRepository<FileCandidato, FileCandidatoId> {
}