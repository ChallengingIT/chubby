/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.AzioneKeyPeople;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AzioneRepository extends JpaRepository<AzioneKeyPeople,Integer> {

  @Query(value= """
       SELECT a.*, ta.id_tipologia
       FROM key_people k
        left join key_people_azioni ka on k.id = ka.id_key_people
        left join azioni a on ka.id_azione = a.id
        left join tipologia_azione ta on ta.id_azione = a.id
       where k.id = ?1
       order by a.data_modifica desc
      """,nativeQuery=true)
  List<AzioneKeyPeople> findByKeyPeople(Integer id);

}