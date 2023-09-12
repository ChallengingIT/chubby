/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Attivita;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttivitaRepository extends CrudRepository<Attivita,Integer> {

  Long countById(Integer id);

  @Query(value=" SELECT a.*, ta.id_tipologia, oa.id_owner, ac.id_cliente,\n" +
      "                 (ifnull ((select id_key_people from attivita_key_people where id_attivita = a.id),null)) id_key_people\n" +
      "                   FROM attivita a, attivita_cliente ac, tipologia_attivita_attivita ta,  owner_attivita oa\n" +
      "                   where a.id = ac.id_attivita\n" +
      "                   and a.id = ta.id_attivita\n" +
      "                   and a.id = oa.id_attivita\n" +
      "                   and if(:idTipologia is not null, ta.id_tipologia=:idTipologia, 1=1)\n" +
      "                   and if(:idOwner is not null, oa.id_owner=:idOwner, 1=1)", nativeQuery=true)
  List<Attivita> findRicerca(@Param("idTipologia") Integer idTipologia, @Param("idOwner") Integer idOwner);
}