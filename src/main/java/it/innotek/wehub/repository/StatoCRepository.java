/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.StatoC;
import it.innotek.wehub.entity.Tipologia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatoCRepository extends CrudRepository<StatoC, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT * FROM statoc order by id", nativeQuery=true)
    List<StatoC> findAllOrdered();
}