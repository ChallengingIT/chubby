/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Tesoreria;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TesoreriaRepository extends CrudRepository<Tesoreria,Integer> {

    @Query(value=" SELECT * from tesoreria where mese = :mese and anno = :anno", nativeQuery=true)
    Tesoreria findSelected(@Param("mese") String mese, @Param("anno") Integer anno);

    @Query(value=" SELECT max(id) from tesoreria", nativeQuery=true)
    Integer findMaxId();

    @Transactional
    @Modifying
    @Query(value=" DELETE \n" +
        "FROM tesoreria_cliente \n" +
        "where id_cliente = :idCliente ", nativeQuery=true)
    void deleteByIdCliente(@Param("idCliente") Integer idCliente);
}