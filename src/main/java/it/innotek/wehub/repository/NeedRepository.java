/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Need;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NeedRepository extends CrudRepository<Need, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT count(distinct id_need) FROM wehub.need_associazione ", nativeQuery=true)
    Integer findNeedAssociati();

    @Query(value=" select n.*, ln.id_livello, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato " +
        " from need n, need_cliente nc, livello_need ln , tipologia_need tn, need_owner non, stato_need sn  " +
        " where n.id = sn.id_need " +
        " and n.id = nc.id_need " +
        " and n.id = ln.id_need " +
        " and n.id = tn.id_need " +
        " and n.id = non.id_need " +
        " and sn.id_stato not in (2,3,4,5) " +
        " order by priorita,data_richiesta " +
        " DESC limit 6", nativeQuery=true)
    List<Need> findNeedOrdinati();

    @Query(value="SELECT n.*, ln.id_livello, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato " +
            " FROM need n, need_cliente nc, livello_need ln , tipologia_need tn, need_owner non, stato_need sn " +
            " WHERE n.id = nc.id_need " +
            " and n.id = ln.id_need " +
            " and n.id = tn.id_need " +
            " and n.id = non.id_need " +
            " and n.id = sn.id_need " +
            " and nc.id_cliente= :idCliente", nativeQuery=true)
    List<Need> findAllByClienti(@Param("idCliente") Integer idCliente);

    @Query(value=" SELECT n.*, ln.id_livello, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato  " +
            "       FROM need n, need_cliente nc, livello_need ln , tipologia_need tn, need_owner non, stato_need sn  " +
            "       WHERE n.id = nc.id_need " +
            "       and n.id = ln.id_need " +
            "       and n.id = tn.id_need " +
            "       and n.id = non.id_need " +
            "       and n.id = sn.id_need " +
            "       and if(:idCliente is not null, nc.id_cliente =:idCliente, 1=1) " +
            "       and if(:idStato is not null, sn.id_stato =:idStato, 1=1) " +
            "       and if(:priorita is not null, n.priorita =:priorita, 1=1) " +
            "       and if(:idTipologia is not null, tn.id_tipologia =:idTipologia, 1=1) " +
            "       and if(:idOwner is not null, non.id_owner =:idOwner, 1=1) " +
            "       and if(:week is not null, n.week=:week, 1=1) ", nativeQuery=true)
    List<Need> findRicerca(@Param("idCliente") Integer idCliente,@Param("idStato") Integer idStato,@Param("priorita") Integer priorita,@Param("idTipologia") Integer idTipologia,@Param("week") String week, @Param("idOwner") Integer idOwner);

    @Query(value="select distinct n.*, ln.id_livello, nc.id_cliente, tn.id_tipologia, non.id_owner, stn.id_stato    " +
        " from need n, livello_need ln, need_cliente nc, livello_candidato lc, candidato c, tipologia_need tn, need_owner non, stato_need stn " +
        " where  n.id = nc.id_need    " +
        " and n.id = tn.id_need    " +
        " and n.id = ln.id_need    " +
        " and n.id = non.id_need   " +
        " and c.id = lc.id_candidato " +
        " and n.id = stn.id_need    " +
        " and n.anni_esperienza <= c.anni_esperienza  " +
        " and c.id = :idCandidato  " +
        " and n.id not in (select id_need from need_candidato where id_need = n.id and id_candidato = :idCandidato)  ", nativeQuery=true)
    List<Need> findNeedAssociabiliCandidato(@Param("idCandidato") Integer idCandidato);


    @Query(value=" SELECT n.*, ln.id_livello, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato  " +
        "       FROM need n, need_cliente nc, livello_need ln , tipologia_need tn, need_owner non, stato_need sn  " +
        "       WHERE n.id = nc.id_need " +
        "       and n.id = ln.id_need " +
        "       and n.id = tn.id_need " +
        "       and n.id = non.id_need " +
        "       and n.id = sn.id_need " +
        "       and week(n.data_richiesta) = week(curdate())", nativeQuery=true)
    List<Need> findNeedSettimanaCur();

    @Query(value=" SELECT n.*, ln.id_livello, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato  " +
        "       FROM need n, need_cliente nc, livello_need ln , tipologia_need tn, need_owner non, stato_need sn  " +
        "       WHERE n.id = nc.id_need " +
        "       and n.id = ln.id_need " +
        "       and n.id = tn.id_need " +
        "       and n.id = non.id_need " +
        "       and n.id = sn.id_need " +
        "       and week(n.data_richiesta) = week(DATE_SUB(curdate(), interval 1 week))", nativeQuery=true)
    List<Need> findNeedSettimanaCurMeno();

    @Query(value=" SELECT n.*, ln.id_livello, nc.id_cliente, tn.id_tipologia, non.id_owner, sn.id_stato  " +
        "       FROM need n, need_cliente nc, livello_need ln , tipologia_need tn, need_owner non, stato_need sn  " +
        "       WHERE n.id = nc.id_need " +
        "       and n.id = ln.id_need " +
        "       and n.id = tn.id_need " +
        "       and n.id = non.id_need " +
        "       and n.id = sn.id_need " +
        "       and week(n.data_richiesta) = week(DATE_ADD(curdate(), interval 1 week))", nativeQuery=true)
    List<Need> findNeedSettimanaCurPiu();

    @Query(value=" SELECT week(curdate()) ", nativeQuery=true)
    Integer findWeek();

    @Query(value=" SELECT week(DATE_SUB(curdate(), interval 1 week)) ", nativeQuery=true)
    Integer findWeekPre();

    @Query(value=" SELECT week(DATE_ADD(curdate(), interval 1 week)) ", nativeQuery=true)
    Integer findWeekSuc();
}