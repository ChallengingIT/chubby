/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Tipo;
import org.springframework.data.repository.CrudRepository;

public interface TipoRepository extends CrudRepository<Tipo, Integer> {
    Long countById(Integer id);
}