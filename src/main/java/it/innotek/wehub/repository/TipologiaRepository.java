/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Tipologia;
import org.springframework.data.repository.CrudRepository;

public interface TipologiaRepository extends CrudRepository<Tipologia, Integer> {
    Long countById(Integer id);
}