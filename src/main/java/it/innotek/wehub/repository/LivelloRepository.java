/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.LivelloScolastico;
import org.springframework.data.repository.CrudRepository;

public interface LivelloRepository extends CrudRepository<LivelloScolastico, Integer> {
    Long countById(Integer id);
}