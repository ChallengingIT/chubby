/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Attivita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttivitaRepository extends JpaRepository<Attivita,Integer> {

    @Query(value= """
         SELECT a.*, ta.id_tipologia, oa.id_owner, ac.id_cliente,
           (ifnull ((select id_key_people from attivita_key_people where id_attivita = a.id),null)) id_key_people
         FROM attivita a, attivita_cliente ac, tipologia_attivita_attivita ta,  owner_attivita oa
         where a.id = ac.id_attivita
         and a.id = ta.id_attivita
         and a.id = oa.id_attivita
         and if(?1 is not null, ta.id_tipologia = ?1, 1=1)
         and if(?2 is not null, oa.id_owner = ?2, 1=1)
         and ac.id_cliente= ?3
        """, nativeQuery=true)
    List<Attivita> ricercaByTipologia_IdAndOwner_IdAndCliente_Id(Integer idTipologia, Integer idOwner, Integer idCliente);
}