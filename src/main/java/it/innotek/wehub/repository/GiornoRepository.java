/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.timesheet.Giorno;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GiornoRepository extends CrudRepository<Giorno, Integer> {

    Long countById(Integer id);

    @Transactional
    @Modifying
    @Query(value=" DELETE\n" +
            "FROM giorno \n" +
            "where id_progetto = :id ", nativeQuery=true)
    void deleteByIdProgetto(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query(value=" DELETE \n" +
            "FROM mese_giorno \n" +
            "where id_giorno = :id ", nativeQuery=true)
    void deleteMeseByIdProgetto(@Param("id") Integer id);


    @Query(value=" SELECT g.*, mg.id_mese\n" +
            "FROM giorno g, mese_giorno mg \n" +
            "where g.id_progetto = :id " +
            "and g.id = mg.id_giorno", nativeQuery=true)
    List<Giorno> findAllByIdProgetto(@Param("id") Integer id);
}