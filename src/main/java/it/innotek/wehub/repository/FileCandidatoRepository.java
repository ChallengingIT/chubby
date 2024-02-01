/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.FileCandidato;
import it.innotek.wehub.entity.FileCandidatoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileCandidatoRepository extends JpaRepository<FileCandidato, FileCandidatoId> {
}