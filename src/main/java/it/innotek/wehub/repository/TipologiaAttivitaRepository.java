/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.TipologiaAttivita;
import org.springframework.data.repository.CrudRepository;

public interface TipologiaAttivitaRepository extends CrudRepository<TipologiaAttivita, Integer> {
    Long countById(Integer id);
}