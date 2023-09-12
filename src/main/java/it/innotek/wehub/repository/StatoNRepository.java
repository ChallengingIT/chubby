/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.StatoN;
import org.springframework.data.repository.CrudRepository;

public interface StatoNRepository extends CrudRepository<StatoN, Integer> {
    Long countById(Integer id);
}