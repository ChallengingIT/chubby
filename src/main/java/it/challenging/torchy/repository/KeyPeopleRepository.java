/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.KeyPeople;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeyPeopleRepository extends JpaRepository<KeyPeople,Integer> {

  Optional<KeyPeople> findByEmail(String nome);

  Page<KeyPeople> findAllByOrderByNomeAsc(Pageable p);

  List<KeyPeople> findByCliente_Id(Integer idCliente);

  @Query(value= """
       SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato
       FROM key_people k
        left join key_people_owner ko on k.id = ko.id_key_people
        left join key_people_stato ks on k.id = ks.id_key_people
        left join key_people_cliente kc on k.id = kc.id_key_people
       where  if(?1 is not null, k.status = ?1, 1=1)
       and if(?2 is not null, kc.id_cliente = ?2, 1=1)
       and if(?3 is not null, ko.id_owner = ?3, 1=1)
       and if(?4 is not null, k.nome like %?4%, 1=1)
       order by k.nome asc
      """,nativeQuery=true)
  Page<KeyPeople> ricercaByStatusAndIdOwnerAndIdAzienda(String status, Integer azienda, Integer owner, String nome, Pageable p);

}