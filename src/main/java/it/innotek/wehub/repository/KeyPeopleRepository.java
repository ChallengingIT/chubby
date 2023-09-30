/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.KeyPeople;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeyPeopleRepository extends JpaRepository<KeyPeople,Integer> {

  Optional<KeyPeople> findByNome(String nome);

  @Query(value= """
       SELECT k.*, ko.id_owner, kc.id_cliente
       FROM key_people k, key_people_owner ko, key_people_cliente kc
       where k.id = ko.id_key_people
       and k.id = kc.id_key_people
       and if(?1 is not null, k.status like ?1%, 1=1)
       and if(?2 is not null, kc.id_cliente = ?2, 1=1)
       and if(?3 is not null, ko.id_owner = ?3, 1=1)
      """,nativeQuery=true)
  List<KeyPeople> ricercaByStatusAndIdOwnerAndIdAzienda(String status, Integer azienda, Integer owner);

}