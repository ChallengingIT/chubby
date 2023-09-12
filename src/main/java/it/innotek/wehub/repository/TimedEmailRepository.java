/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.TimedEmail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TimedEmailRepository extends CrudRepository<TimedEmail, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT max(id) from timed_email", nativeQuery=true)
    Long findUltimoId();

    @Modifying
    @Transactional
    @Query(value=" UPDATE timed_email set inviata = :inviata where id = :id", nativeQuery=true)
    void update(@Param("id") Long idEmail, @Param("inviata") Integer inviata);
}