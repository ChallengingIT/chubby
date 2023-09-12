/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.StatoFP;
import org.springframework.data.repository.CrudRepository;

public interface StatoFPRepository extends CrudRepository<StatoFP, Integer> {
    Long countById(Integer id);
}