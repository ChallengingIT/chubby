/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Facolta;
import org.springframework.data.repository.CrudRepository;

public interface FacoltaRepository extends CrudRepository<Facolta, Integer> {
    Long countById(Integer id);
}