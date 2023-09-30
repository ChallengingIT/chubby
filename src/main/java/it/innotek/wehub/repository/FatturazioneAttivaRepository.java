/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.FatturazioneAttiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FatturazioneAttivaRepository extends JpaRepository<FatturazioneAttiva, Integer> {

    @Query(value= """
          SELECT fa.*, fc.id_cliente, sfa.id_stato
          FROM fatturazione_attiva fa, fattura_cliente fc, stato_fatturazione_attiva sfa
          where fa.id = fc.id_fattura
          and fa.id = sfa.id_fattura
          and if(?1 is not null, fc.id_cliente = ?1, 1=1)
          and if(?2 is not null, sfa.id_stato = ?2, 1=1)
         """, nativeQuery = true)
    List<FatturazioneAttiva> ricercaByIdClienteAndIdStato(Integer idCliente, Integer idStato);
}