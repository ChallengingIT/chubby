/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.FileCandidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface FileCandidatoRepository extends JpaRepository<FileCandidato, Integer> {

    @Procedure
    void elimina_file_vecchi(String lista_candidato_id, Integer tipologia);
}