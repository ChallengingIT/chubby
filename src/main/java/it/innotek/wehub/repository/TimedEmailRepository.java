/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.TimedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TimedEmailRepository extends JpaRepository<TimedEmail, Integer> {

    @Transactional
    @Modifying
    @Query("update TimedEmail t set t.inviata = ?1 where t.id = ?2")
    void updateInviataByEmailCandidato(Integer inviata, Long idEmail);

    @Query("SELECT coalesce(max(te.id), 0) FROM TimedEmail te")
    Long findMaxId();
}