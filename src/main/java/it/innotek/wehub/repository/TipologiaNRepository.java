/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.TipologiaN;
import org.springframework.data.repository.CrudRepository;

public interface TipologiaNRepository extends CrudRepository<TipologiaN, Integer> {
    Long countById(Integer id);
}