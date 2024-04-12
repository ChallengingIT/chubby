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
       SELECT a.*
       FROM key_people k, key_people_azioni ka, azioni a
       where k.id = ka.id_key_people
       and ka.id_azione = a.id
       and k.id = ?1
       order by k.nome asc
      """,nativeQuery=true)
  List<AzioneKeyPeople> findByKeyPeople(Integer id);

}