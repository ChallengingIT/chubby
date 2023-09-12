/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.timesheet.Calendario;
import org.springframework.data.repository.CrudRepository;

public interface CalendarioRepository extends CrudRepository<Calendario, Integer> {
    Long countById(Integer id);
}