/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
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

    Page<Need> findAllByOrderByDescrizioneAsc(Pageable p);

    @Query(value=" SELECT count(distinct id_need) FROM wehub.need_associazione ", nativeQuery=true)
    Integer findNeedAssociati();

    @Query(value= """
         select n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato
         from need n, need_cliente nc, tipologia_need tn, need_owner non, stato_need sn
         where n.id = sn.id_need
         and n.id = nc.id_need
         and n.id = tn.id_need
         and n.id = non.id_need
         and sn.id_stato not in (2,3,4,5)
         order by priorita,data_richiesta desc
         limit 6
        """, nativeQuery=true)
    List<Need> findNeedOrdinati();

    List<Need> findByCliente_Id(@Param("idCliente") Integer idCliente);

    Page<Need> findByCliente_IdOrderByDescrizioneAsc(Integer idCliente, Pageable p);

    Page<Need> findByKeyPeople_IdOrderByDescrizioneAsc(Integer idKeyPeople, Pageable p);

    @Query(value= """
         SELECT n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato
         FROM need n, need_cliente nc, need_keypeople nk, tipologia_need tn, need_owner non, stato_need sn
         WHERE n.id = nc.id_need
         and n.id = tn.id_need
         and n.id = non.id_need
         and n.id = sn.id_need
         and n.id = nk.id_need
         and if(?1 is not null, nc.id_cliente = ?1, 1=1)
         and if(?2 is not null, sn.id_stato = ?2, 1=1)
         and if(?3 is not null, nk.id_keypeople = ?3, 1=1)
         and if(?4 is not null, n.priorita = ?4, 1=1)
         and if(?5 is not null, tn.id_tipologia = ?5, 1=1)
         and if(?7 is not null, non.id_owner = ?7, 1=1)
         and if(?6 is not null, n.week = ?6, 1=1)
         and if(?8 is not null, n.descrizione like %?8%, 1=1)
         order by n.descrizione asc
        """, nativeQuery=true)
    Page<Need> ricerca(Integer idCliente, Integer idStato, Integer idKeyPeople, Integer priorita, Integer idTipologia, String week, Integer idOwner, String descrizione, Pageable p);

    @Query(value= """
           select distinct n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, stn.id_stato
           from need n, need_cliente nc, candidato c, tipologia_need tn, need_owner non, stato_need stn
           where  n.id = nc.id_need
           and n.id = tn.id_need
           and n.id = non.id_need
           and n.id = stn.id_need
           and c.id = ?1
           and n.id not in (select id_need from need_candidato where id_need = n.id and id_candidato = ?1)
           order by n.descrizione asc
          """, nativeQuery=true)
    List<Need> findNeedAssociabiliCandidato(Integer idCandidato);


    @Query(value= """
         SELECT n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato
         FROM need n, need_cliente nc, tipologia_need tn, need_owner non, stato_need sn
         WHERE n.id = nc.id_need
         and n.id = tn.id_need
         and n.id = non.id_need
         and n.id = sn.id_need
         and week(n.data_richiesta) = week(curdate())
        """, nativeQuery=true)
    List<Need> findNeedSettimanaCur();

    @Query(value= """
         SELECT n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato
         FROM need n, need_cliente nc, tipologia_need tn, need_owner non, stato_need sn
         WHERE n.id = nc.id_need
         and n.id = tn.id_need
         and n.id = non.id_need
         and n.id = sn.id_need
         and week(n.data_richiesta) = week(DATE_SUB(curdate(), interval 1 week))
        """, nativeQuery=true)
    List<Need> findNeedSettimanaCurMeno();

    @Query(value= """
         SELECT n.*, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato
         FROM need n, need_cliente nc, tipologia_need tn, need_owner non, stato_need sn
         WHERE n.id = nc.id_need
         and n.id = tn.id_need
         and n.id = non.id_need
         and n.id = sn.id_need
         and week(n.data_richiesta) = week(DATE_ADD(curdate(), interval 1 week))
        """, nativeQuery=true)
    List<Need> findNeedSettimanaCurPiu();

    @Query(value=" SELECT week(curdate()) ", nativeQuery=true)
    Integer findWeek();

    @Query(value=" SELECT week(DATE_SUB(curdate(), interval 1 week)) ", nativeQuery=true)
    Integer findWeekPre();

    @Query(value=" SELECT week(DATE_ADD(curdate(), interval 1 week)) ", nativeQuery=true)
    Integer findWeekSuc();
}