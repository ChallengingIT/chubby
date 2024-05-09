/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query(value= " SELECT c.denominazione FROM cliente c ",nativeQuery=true)
    List<String> findAllDescriptions();

    @Query(value= " SELECT c.sede_operativa FROM cliente c ",nativeQuery=true)
    List<String> findAllSedeLegali();

    @Query(value= " SELECT c.sede_legale FROM cliente c ",nativeQuery=true)
    List<String> findAllSedeOperative();

    @Query(value= " SELECT c.cap FROM cliente c ",nativeQuery=true)
    List<String> findAllCap();

    @Query(value= " SELECT c.indirizzo FROM cliente c ",nativeQuery=true)
    List<String> findAllAddress();

    @Query(value= " SELECT c.provincia FROM cliente c ",nativeQuery=true)
    List<String> findAllProvince();

    @Query(value= " SELECT c.provincia FROM cliente c ",nativeQuery=true)
    List<String> findAllEmail();

    @Query(value= " SELECT c.pec FROM cliente c ",nativeQuery=true)
    List<String> findAllPec();

    @Query(value= " SELECT c.codice_destinatario FROM cliente c ",nativeQuery=true)
    List<String> findAllCodiciDestinatario();

    @Query(value= " SELECT c.sito FROM cliente c ",nativeQuery=true)
    List<String> findAllSites();

    @Query(value= " SELECT c.sito FROM cliente c ",nativeQuery=true)
    List<String> findAllSettoreMercato();

    @Query(value= " SELECT c.tipologia FROM cliente c ",nativeQuery=true)
    List<String> findAllTipologie();

    @Query(value= " SELECT c.cf FROM cliente c ",nativeQuery=true)
    List<String> findAllCF();

    @Query(value= " SELECT c.pi FROM cliente c ",nativeQuery=true)
    List<String> findAllPI();

    @Query(value= " SELECT c.citta FROM cliente c ",nativeQuery=true)
    List<String> findAllCities();

    Page<Cliente> findAllByOrderByDenominazioneAsc(Pageable p);
    List<Cliente> findAllByOrderByDenominazioneAsc();

    @Query(value= """
         SELECT c.*
         FROM cliente c
         where if(?1 is not null, c.denominazione like %?1%, 1=1)
         and if(?2 is not null, c.citta like ?2%, 1=1)
        """, nativeQuery=true)
    List<Cliente> ricercaByDenominazioneAndCitta(String denominazione, String citta);

    List<Cliente> findByDenominazione(String denominazione);

    List<Cliente> findByEmail(String email);

    @Query(value= """
         SELECT c.*, co.id_owner
         FROM cliente c, cliente_owner co
         where c.id = co.id_cliente
         and if(?1 is not null, c.ida > ?1, 1=1)
         and if(?2 is not null, c.ida <= ?2, 1=1)
         and if(?3 is not null, co.id_owner = ?3, 1=1)
         and if(?4 is not null, c.tipologia like ?4%, 1=1)
         and if(?5 is not null, c.denominazione like %?5%, 1=1)
         order by c.denominazione asc
        """,nativeQuery=true)
    Page<Cliente> ricercaByIdaAndOwner_IdAndTipologiaAndDenominazione(
            Double idaMin, Double idaMax, Integer owner, String tipologia, String denominazione, Pageable p);

    @Query(value= """
          SELECT c.*, co.id_owner
          FROM cliente c
          join cliente_owner co on c.id = co.id_cliente
          join owner o on co.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where u.username = ?1
          order by c.denominazione asc
        """,nativeQuery=true)
    Page<Cliente> ricercaByUsername( String username, Pageable p);

    @Query(value= """
          SELECT c.*, co.id_owner
          FROM cliente c
          join cliente_owner co on c.id = co.id_cliente
          join owner o on co.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where 1=1
          ?1
        """,nativeQuery=true)
    List<Cliente> findByWhere(String where);

    @Query(value= """
         SELECT c.*, co.id_owner
         FROM cliente c
         join cliente_owner co on c.id = co.id_cliente
         join owner o on co.id_owner = o.id
         left join users u on o.nome = u.nome and o.cognome = u.cognome
         where u.username = ?1
         and if(?2 is not null, c.ida > ?2, 1=1)
         and if(?3 is not null, c.ida <= ?3, 1=1)
         and if(?4 is not null, c.tipologia like ?4%, 1=1)
         and if(?5 is not null, c.denominazione like %?5%, 1=1)
         order by c.denominazione asc
        """,nativeQuery=true)
    Page<Cliente> ricercaByUsernameAndIdaAndTipologiaAndDenominazione(
            String username, Double idaMin, Double idaMax, String tipologia, String denominazione, Pageable p);
}