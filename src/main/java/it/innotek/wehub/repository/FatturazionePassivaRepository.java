/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.FatturazionePassiva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FatturazionePassivaRepository extends CrudRepository<FatturazionePassiva, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT fp.*, ff.id_fornitore, sfp.id_stato \n" +
    " FROM fatturazione_passiva fp, fornitore_fattura ff, stato_fatturazione_passiva sfp\n" +
    " where fp.id = ff.id_fattura\n" +
    " and fp.id = sfp.id_fattura\n" +
    " and if(:fornitore is not null, ff.id_fornitore = :fornitore, 1=1)\n" +
    " and if(:stato is not null, sfp.id_stato = :stato, 1=1) ", nativeQuery=true)
    List<FatturazionePassiva> findRicerca(@Param("fornitore") Integer fornitore, @Param("stato") Integer stato);

    @Query(value=" SELECT fp.*, ff.id_fornitore, sfp.id_stato \n" +
    " FROM fatturazione_passiva fp, fornitore_fattura ff, stato_fatturazione_passiva sfp\n" +
    " where fp.id = ff.id_fattura\n" +
    " and fp.id = sfp.id_fattura \n" +
    " and fp.scadenza <= :date\n ", nativeQuery=true)
    List<FatturazionePassiva> findFattureDaPagare(@Param("date") LocalDate date);
}