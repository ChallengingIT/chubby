/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.TipologiaI;
import org.springframework.data.repository.CrudRepository;

public interface TipologiaIRepository extends CrudRepository<TipologiaI, Integer> {
    Long countById(Integer id);
}