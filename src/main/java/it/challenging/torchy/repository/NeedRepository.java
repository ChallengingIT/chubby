/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Need;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeedRepository extends JpaRepository<Need, Integer> {

    @Query(value= " SELECT n.descrizione FROM need n ",nativeQuery=true)
    List<String> findAllDescriptions();

    @Query(value= " SELECT n.location FROM need n ",nativeQuery=true)
    List<String> findAllLocations();

    @Query(value= " SELECT n.tipo FROM need n ",nativeQuery=true)
    List<Integer> findAllTypes();

    Page<Need> findAllByOrderByProgressivoDesc(Pageable p);

    Page<Need> findByKeyPeople_IdOrderByProgressivoDesc(Integer idKeyPeople, Pageable p);

    @Query(value= """
          SELECT  n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato, nk.id_keypeople, ssn.id_skill, nj.id_job_title,
           mi.id_modalita_impiego, ml.id_modalita_lavoro
          FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join skill_need ssn on n.id = ssn.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
          join owner o on non.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where 1=1
          ?1
        """,nativeQuery=true)
    List<Need> findByWhere(String where);

    @Query(value= """
          SELECT  n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato, nk.id_keypeople, nj.id_job_title,
           mi.id_modalita_impiego, ml.id_modalita_lavoro
          FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
          join owner o on non.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where u.username = ?1
          order by sn.id_stato = 1 desc, sn.id_stato = 7 desc, n.priorita asc
          """,nativeQuery=true)
    List<Need> ricercaByUsername(String username);

    @Query(value= """
          SELECT  n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato, nk.id_keypeople, nj.id_job_title,
           mi.id_modalita_impiego, ml.id_modalita_lavoro
          FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
          join owner o on non.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where u.username = ?1
          and sn.id_stato in (1,6,7)
          order by sn.id_stato = 1 desc, sn.id_stato = 7 desc, n.priorita asc
          """,nativeQuery=true)
    List<Need> ricercaByUsernameFiltered(String username);

    @Query(value= """
          SELECT  n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato, nk.id_keypeople, nj.id_job_title,
           mi.id_modalita_impiego, ml.id_modalita_lavoro
          FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
          join owner o on non.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where sn.id_stato in (1,6,7)
          order by sn.id_stato = 1 desc, sn.id_stato = 7 desc, n.priorita asc
          """,nativeQuery=true)
    List<Need> ricercaOrdinata();

    @Query(value= """
          SELECT  n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato, nk.id_keypeople, nj.id_job_title,
           mi.id_modalita_impiego, ml.id_modalita_lavoro
          FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
          join owner o on non.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
          where u.username = ?1
          order by id = 1 desc, id = 7 desc
        """,nativeQuery=true)
    Page<Need> find(String username, Pageable p);

    @Query(value= """
         select progressivo
         from need n
         order by id desc
         limit 1
        """, nativeQuery=true)
    String findUltimoProgressivo();

    @Query(value=" SELECT count(distinct id_need) FROM wehub.need_associazione ", nativeQuery=true)
    Integer findNeedAssociati();

    List<Need> findByCliente_Id(@Param("idCliente") Integer idCliente);

    Page<Need> findByCliente_IdOrderByProgressivoDesc(Integer idCliente, Pageable p);

    @Query(value= """
         SELECT n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato, nk.id_keypeople, nj.id_job_title,
           mi.id_modalita_impiego, ml.id_modalita_lavoro
         FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
         WHERE if(?1 is not null, nc.id_cliente = ?1, 1=1)
         and if(?2 is not null, sn.id_stato = ?2, 1=1)
         and if(?3 is not null, nk.id_keypeople = ?3, 1=1)
         and if(?4 is not null, n.priorita = ?4, 1=1)
         and if(?5 is not null, tn.id_tipologia = ?5, 1=1)
         and if(?7 is not null, non.id_owner = ?7, 1=1)
         and if(?6 is not null, n.week = ?6, 1=1)
         and if(?8 is not null, n.descrizione like %?8%, 1=1)
         order by n.progressivo desc
        """, nativeQuery=true)
    Page<Need> ricerca(Integer idCliente, Integer idStato, Integer idKeyPeople, Integer priorita, Integer idTipologia, String week, Integer idOwner, String descrizione, Pageable p);

    @Query(value= """
         SELECT count(*)
         FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
         WHERE if(?1 is not null, nc.id_cliente = ?1, 1=1)
         and if(?2 is not null, sn.id_stato = ?2, 1=1)
         and if(?3 is not null, nk.id_keypeople = ?3, 1=1)
         and if(?4 is not null, n.priorita = ?4, 1=1)
         and if(?5 is not null, tn.id_tipologia = ?5, 1=1)
         and if(?7 is not null, non.id_owner = ?7, 1=1)
         and if(?6 is not null, n.week = ?6, 1=1)
         and if(?8 is not null, n.descrizione like %?8%, 1=1)
         order by n.progressivo desc
        """, nativeQuery=true)
    Long countRicerca(Integer idCliente, Integer idStato, Integer idKeyPeople, Integer priorita, Integer idTipologia, String week, Integer idOwner, String descrizione);


    @Query(value= """
         SELECT n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato, nk.id_keypeople, nj.id_job_title,
           mi.id_modalita_impiego, ml.id_modalita_lavoro
         FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
          join owner o on non.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
         WHERE if(?1 is not null, nc.id_cliente = ?1, 1=1)
         and if(?2 is not null, sn.id_stato = ?2, 1=1)
         and if(?3 is not null, nk.id_keypeople = ?3, 1=1)
         and if(?4 is not null, n.priorita = ?4, 1=1)
         and if(?5 is not null, tn.id_tipologia = ?5, 1=1)
         and if(?7 is not null, non.id_owner = ?7, 1=1)
         and if(?6 is not null, n.week = ?6, 1=1)
         and if(?8 is not null, n.descrizione like %?8%, 1=1)
         and u.username = ?9
         order by n.progressivo desc
        """, nativeQuery=true)
    Page<Need> ricercaUsername(Integer idCliente, Integer idStato, Integer idKeyPeople, Integer priorita, Integer idTipologia, String week, Integer idOwner, String descrizione, String username, Pageable p);

    @Query(value= """
         SELECT count(*)
         FROM need n
          left join need_cliente nc on n.id = nc.id_need
          left join need_keypeople nk on n.id = nk.id_need
          left join need_job_title nj on n.id = nj.id_need
          left join modalita_impiego_need mi on n.id = mi.id_need
          left join modalita_lavoro_need ml on n.id = ml.id_need
          left join tipologia_need tn on n.id = tn.id_need
          left join need_owner non on n.id = non.id_need
          left join stato_need sn on n.id = sn.id_need
          join owner o on non.id_owner = o.id
          left join users u on o.nome = u.nome and o.cognome = u.cognome
         WHERE if(?1 is not null, nc.id_cliente = ?1, 1=1)
         and if(?2 is not null, sn.id_stato = ?2, 1=1)
         and if(?3 is not null, nk.id_keypeople = ?3, 1=1)
         and if(?4 is not null, n.priorita = ?4, 1=1)
         and if(?5 is not null, tn.id_tipologia = ?5, 1=1)
         and if(?7 is not null, non.id_owner = ?7, 1=1)
         and if(?6 is not null, n.week = ?6, 1=1)
         and if(?8 is not null, n.descrizione like %?8%, 1=1)
         and u.username = ?9
         order by n.progressivo desc
        """, nativeQuery=true)
    Long countRicercaUsername(Integer idCliente, Integer idStato, Integer idKeyPeople, Integer priorita, Integer idTipologia, String week, Integer idOwner, String descrizione, String username);


    @Query(value= """
           select distinct n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato, nk.id_keypeople, nj.id_job_title,
           mi.id_modalita_impiego, ml.id_modalita_lavoro
           from need n
              left join need_cliente nc on n.id = nc.id_need
              left join need_keypeople nk on n.id = nk.id_need
              left join modalita_impiego_need mi on n.id = mi.id_need
              left join modalita_lavoro_need ml on n.id = ml.id_need
              left join need_job_title nj on n.id = nj.id_need
              left join tipologia_need tn on n.id = tn.id_need
              left join need_owner non on n.id = non.id_need
              left join stato_need sn on n.id = sn.id_need
           where n.id not in (select id_need from need_candidato where id_need = n.id and id_candidato = ?1)
           order by n.progressivo desc
          """, nativeQuery=true)
    List<Need> findNeedAssociabiliCandidato(Integer idCandidato);

}