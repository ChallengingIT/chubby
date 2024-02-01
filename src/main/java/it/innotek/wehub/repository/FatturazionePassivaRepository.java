/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.FatturazionePassiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FatturazionePassivaRepository extends JpaRepository<FatturazionePassiva, Integer> {

    List<FatturazionePassiva> findByFornitore_Id(Integer id);

    List<FatturazionePassiva> findByScadenzaLessThan(LocalDate scadenza);

    @Query(value= """
          SELECT fp.*, ff.id_fornitore, sfp.id_stato
          FROM fatturazione_passiva fp, fornitore_fattura ff, stato_fatturazione_passiva sfp
          where fp.id = ff.id_fattura
          and fp.id = sfp.id_fattura
          and if(?1 is not null, ff.id_fornitore = ?1, 1=1)
          and if(?2 is not null, sfp.id_stato = ?2, 1=1)
         """, nativeQuery = true)
    List<FatturazionePassiva> ricercaByIdFornitoreAndIdStato(Integer idFornitore, Integer idStato);

}