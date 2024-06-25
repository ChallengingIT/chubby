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

  @Query(value= " SELECT k.nome FROM key_people k ",nativeQuery=true)
  List<String> findAllNames();

  @Query(value= " SELECT k.email FROM key_people k ",nativeQuery=true)
  List<String> findAllEmail();

  Optional<KeyPeople> findByEmail(String nome);

  Page<KeyPeople> findAllByOrderByNomeAsc(Pageable p);

  List<KeyPeople> findByCliente_Id(Integer idCliente);

  @Query(value= """
          SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato
          FROM key_people k
          left join key_people_owner ko on k.id = ko.id_key_people
          left join key_people_stato ks on k.id = ks.id_key_people
          left join key_people_cliente kc on k.id = kc.id_key_people
          left join owner o on ko.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where u.username = ?1
          order by k.nome asc
        """,nativeQuery=true)
  Page<KeyPeople> ricercaByUsername(String username, Pageable p);

  @Query(value= """
          SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato, ka.id_azione
          FROM key_people k
          left join key_people_owner ko on k.id = ko.id_key_people
          left join key_people_stato ks on k.id = ks.id_key_people
          left join key_people_cliente kc on k.id = kc.id_key_people
          join key_people_azioni ka on  k.id = ka.id_key_people
          join azioni a on ka.id_azione = a.id
          left join owner o on ko.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where u.username = ?1
          and DATE(a.data_modifica) = DATE(now())
          order by k.nome asc
        """,nativeQuery=true)
  Page<KeyPeople> ricercaAzioniByUsername(String username, Pageable p);

  @Query(value= """
          SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato, ka.id_azione
          FROM key_people k
          left join key_people_owner ko on k.id = ko.id_key_people
          left join key_people_stato ks on k.id = ks.id_key_people
          left join key_people_cliente kc on k.id = kc.id_key_people
          join key_people_azioni ka on  k.id = ka.id_key_people
          join azioni a on ka.id_azione = a.id
          left join owner o on ko.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where u.username = ?1
          and DATE(a.data_modifica) = DATE(now() + INTERVAL ?1 DAY)
          order by k.nome asc
        """,nativeQuery=true)
  Page<KeyPeople> ricercaAzioniByUsernameInterval(String username, Integer interval, Pageable p);

  @Query(value= """
          SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato, ka.id_azione
          FROM key_people k
          left join key_people_owner ko on k.id = ko.id_key_people
          left join key_people_stato ks on k.id = ks.id_key_people
          left join key_people_cliente kc on k.id = kc.id_key_people
          join key_people_azioni ka on  k.id = ka.id_key_people
          join azioni a on ka.id_azione = a.id
          where DATE(a.data_modifica) = DATE(now())
          order by k.nome asc
        """,nativeQuery=true)
  Page<KeyPeople> ricercaAzioni(Pageable p);

  @Query(value= """
          SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato, ka.id_azione
          FROM key_people k
          left join key_people_owner ko on k.id = ko.id_key_people
          left join key_people_stato ks on k.id = ks.id_key_people
          left join key_people_cliente kc on k.id = kc.id_key_people
          join key_people_azioni ka on  k.id = ka.id_key_people
          join azioni a on ka.id_azione = a.id
          where DATE(a.data_modifica) = DATE(now() + INTERVAL ?1 DAY)
          order by k.nome asc
        """,nativeQuery=true)
  Page<KeyPeople> ricercaAzioniInterval(Integer interval, Pageable p);


  @Query(value= """
          SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato
          FROM key_people k
          left join key_people_owner ko on k.id = ko.id_key_people
          left join key_people_stato ks on k.id = ks.id_key_people
          left join key_people_cliente kc on k.id = kc.id_key_people
          join owner o on ko.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where 1=1
          ?1
        """,nativeQuery=true)
  List<KeyPeople> findByWhere(String where);

  @Query(value= """
       SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato
       FROM key_people k
        left join key_people_owner ko on k.id = ko.id_key_people
        left join key_people_stato ks on k.id = ks.id_key_people
        left join key_people_cliente kc on k.id = kc.id_key_people
       where  if(?1 is not null, ks.id_stato = ?1, 1=1)
       and if(?2 is not null, kc.id_cliente = ?2, 1=1)
       and if(?3 is not null, ko.id_owner = ?3, 1=1)
       and if(?4 is not null, k.nome like %?4%, 1=1)
       order by k.nome asc
      """,nativeQuery=true)
  Page<KeyPeople> ricercaByIdStatoAndIdOwnerAndIdAzienda(Integer status, Integer azienda, Integer owner, String nome, Pageable p);

  @Query(value= """
       SELECT count(*)
       FROM key_people k
        left join key_people_owner ko on k.id = ko.id_key_people
        left join key_people_stato ks on k.id = ks.id_key_people
        left join key_people_cliente kc on k.id = kc.id_key_people
       where  if(?1 is not null, ks.id_stato = ?1, 1=1)
       and if(?2 is not null, kc.id_cliente = ?2, 1=1)
       and if(?3 is not null, ko.id_owner = ?3, 1=1)
       and if(?4 is not null, k.nome like %?4%, 1=1)
      """,nativeQuery=true)
  Long countRicercaByIdStatoAndIdOwnerAndIdAzienda(Integer status, Integer azienda, Integer owner, String nome);


  @Query(value= """
       SELECT k.*, ko.id_owner, kc.id_cliente, ks.id_stato
       FROM key_people k
        left join key_people_owner ko on k.id = ko.id_key_people
        left join key_people_stato ks on k.id = ks.id_key_people
        left join key_people_cliente kc on k.id = kc.id_key_people
        join owner o on ko.id_owner = o.id
        left join users u on o.nome = u.nome and o.cognome = u.cognome
       where  if(?1 is not null, ks.id_stato = ?1, 1=1)
       and if(?2 is not null, kc.id_cliente = ?2, 1=1)
       and if(?3 is not null, ko.id_owner = ?3, 1=1)
       and if(?4 is not null, k.nome like %?4%, 1=1)
       and u.username = ?5
       order by k.nome asc
      """,nativeQuery=true)
  Page<KeyPeople> ricercaByIdStatoAndIdOwnerAndIdAziendaAndUsername(Integer status, Integer azienda, Integer owner, String nome, String username, Pageable p);

  @Query(value= """
       SELECT count(*)
       FROM key_people k
        left join key_people_owner ko on k.id = ko.id_key_people
        left join key_people_stato ks on k.id = ks.id_key_people
        left join key_people_cliente kc on k.id = kc.id_key_people
        join owner o on ko.id_owner = o.id
        left join users u on o.nome = u.nome and o.cognome = u.cognome
       where  if(?1 is not null, ks.id_stato = ?1, 1=1)
       and if(?2 is not null, kc.id_cliente = ?2, 1=1)
       and if(?3 is not null, ko.id_owner = ?3, 1=1)
       and if(?4 is not null, k.nome like %?4%, 1=1)
       and u.username = ?5
      """,nativeQuery=true)
  Long countRicercaByIdStatoAndIdOwnerAndIdAziendaAndUsername(Integer status, Integer azienda, Integer owner, String nome, String username);

}