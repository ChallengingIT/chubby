/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.entity.Fornitore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FornitoreRepository extends CrudRepository<Fornitore, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT f.* \n" +
            "             FROM fornitore f\n" +
            "             where if(:denominazione is not null, f.denominazione like %:denominazione%, 1=1)\n" +
            "             and if(:referente is not null, f.referente like %:referente%, 1=1)\n"+
            "             and if(:email is not null, f.email like %:email%, 1=1) ", nativeQuery=true)
    List<Fornitore> findRicerca(@Param("denominazione") String denominazione, @Param("referente") String referente, @Param("email") String email);

    @Query(value=" SELECT if(count(*)=1,1,0)\n" +
            "FROM fornitore c\n" +
            "where denominazione = :denominazione ", nativeQuery=true)
    Integer checkDenominazione(@Param("denominazione") String denominazione);
}