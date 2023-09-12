/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.KeyPeople;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface KeyPeopleRepository extends CrudRepository<KeyPeople,Integer> {

  Long countById(Integer id);

  @Query(value=" SELECT if(count(*)=1,1,0)\n" +
      "FROM key_people k\n" +
      "where k.nome = :nome ", nativeQuery=true)
  Integer checkNome(@Param("nome") String nome);

  @Query(value=" SELECT k.*, ko.id_owner,kc.id_cliente \n" +
      "             FROM key_people k, key_people_owner ko, key_people_cliente kc\n" +
      "             where k.id = ko.id_key_people " +
      "             and k.id = kc.id_key_people " +
      "             and if(:status is not null, k.status=:status, 1=1)\n"+
      "             and if(:azienda is not null, kc.id_cliente=:azienda, 1=1)\n"+
      "             and if(:owner is not null, ko.id_owner = :owner, 1=1)\n ",nativeQuery=true)
  List<KeyPeople> findRicercaKeyPeople(
      @Param("status") String status,
      @Param("owner") Integer owner,
      @Param("azienda") Integer azienda);

  @Transactional
  @Modifying
  @Query(value=" DELETE \n" +
      "FROM key_people_cliente \n" +
      "where id_cliente = :idCliente ", nativeQuery=true)
  void deleteByIdCliente(@Param("idCliente") Integer idCliente);
}