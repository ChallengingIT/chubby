/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.FatturazioneAttiva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FatturazioneAttivaRepository extends CrudRepository<FatturazioneAttiva, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT fa.*, fc.id_cliente, sfa.id_stato \n" +
                 " FROM fatturazione_attiva fa, fattura_cliente fc, stato_fatturazione_attiva sfa\n" +
                 " where fa.id = fc.id_fattura\n" +
                 " and fa.id = sfa.id_fattura\n" +
                 "and if(:cliente is not null, fc.id_cliente = :cliente, 1=1)\n" +
                 "and if(:stato is not null, sfa.id_stato = :stato, 1=1) ", nativeQuery=true)
    List<FatturazioneAttiva> findRicerca(@Param("cliente") Integer cliente, @Param("stato") Integer stato);
}