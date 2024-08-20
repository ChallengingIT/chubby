/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Chiesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ChurchRepository extends JpaRepository<Chiesa, BigDecimal> {

    @Transactional
    @Modifying
    @Query("update Chiesa c set c.ottenuta = ?1 where c.idChiesa = ?2 and c.username = ?3")
    void updateOttenutaByIdChiesaAndUsername(Byte ottenuta, Integer idChiesa, String username);

    List<Chiesa> findByUsername(String username);

    @Query(value= " select min(id) from chiese where ottenuta = 0 and username = ?1", nativeQuery=true)
    long findLastByUsername(String username);

}