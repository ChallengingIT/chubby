/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.StatoFA;
import org.springframework.data.repository.CrudRepository;

public interface StatoFARepository extends CrudRepository<StatoFA, Integer> {
    Long countById(Integer id);
}