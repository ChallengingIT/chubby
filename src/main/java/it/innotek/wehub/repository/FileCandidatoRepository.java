/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.FileCandidato;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FileCandidatoRepository extends CrudRepository<FileCandidato, Integer> {

    Long countById(Integer id);

    @Procedure(procedureName  = "elimina_file_vecchi")
    void removeFileDoppione(@Param("lista_candidato_id") String listId, @Param("tipologia") Integer tipologia);
}