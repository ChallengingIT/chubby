/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.StatoA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatoARepository extends CrudRepository<StatoA, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT * FROM statoa order by id", nativeQuery=true)
    List<StatoA> findAllOrdered();
}