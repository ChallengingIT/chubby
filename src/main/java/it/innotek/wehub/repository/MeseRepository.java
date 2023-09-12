/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.timesheet.Mese;
import org.springframework.data.repository.CrudRepository;

public interface MeseRepository extends CrudRepository<Mese, Integer> {
    Long countById(Integer id);
}