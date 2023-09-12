/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Candidato;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidatoRepository extends CrudRepository<Candidato, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT c.*, tc.id_tipologia, sc.id_stato, lc.id_livello, ttc.id_tipo, " +
        "           (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore, " +
        "           (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta,  \n" +
        "           (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner  \n" +
        "             FROM candidato c, stato_candidato sc, tipologia_candidato tc,  tipo_candidato ttc,livello_candidato lc\n" +
        "             where c.id = sc.id_candidato\n" +
        "             and c.id = tc.id_candidato\n" +
        "             and c.id = lc.id_candidato\n" +
        "             and c.id = ttc.id_candidato\n" +
        "             and if(:nome is not null, c.nome LIKE :nome%, 1=1) \n" +
        "             and if(:cognome is not null, c.cognome LIKE :cognome%, 1=1)\n" +
        "             and if(:email is not null, c.email LIKE :email%, 1=1)\n" +
        "             and if(:idTipologia is not null, tc.id_tipologia=:idTipologia, 1=1)\n" +
        "             and if(:idStato is not null, sc.id_stato=:idStato, 1=1)\n" +
        "             and if(:idTipo is not null, ttc.id_tipo=:idTipo, 1=1) ", nativeQuery=true)
    List<Candidato> findRicerca(@Param("nome") String nome, @Param("cognome") String cognome, @Param("email") String email, @Param("idTipologia") Integer idTipologia, @Param("idStato") Integer idStato, @Param("idTipo") Integer idTipo);

    @Query(value=" SELECT c.*, tc.id_tipologia, sc.id_stato, lc.id_livello, ttc.id_tipo, (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore, (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta  \n" +
            "             FROM candidato c, stato_candidato sc, tipologia_candidato tc,  tipo_candidato ttc,livello_candidato lc, need_candidato nc\n" +
            "             where c.id = sc.id_candidato\n" +
            "             and c.id = tc.id_candidato\n" +
            "             and c.id = lc.id_candidato\n" +
            "             and c.id = ttc.id_candidato\n" +
            "             and c.id = nc.id_candidato\n" +
            "             and nc.id_need=:idNeed ", nativeQuery=true)
    List<Candidato> findAllByNeed(@Param("idNeed") Integer idNeed);

    @Query(value=" SELECT if(count(*)=1,1,0)\n" +
                    "FROM candidato c\n" +
                    "where email = :email ", nativeQuery=true)
    Integer checkEmail(@Param("email") String email);

    @Query(value=" SELECT count(*)>0 FROM fornitore_candidato where id_fornitore =:idFornitore", nativeQuery=true)
    Integer findAssociatiFornitori(@Param("idFornitore") Integer idFornitore);

    @Query(value=" SELECT max(id) from candidato", nativeQuery=true)
    Integer findUltimoId();

    @Query(value=" SELECT max(i.id) FROM intervista i , candidato_intervista ci\n" +
            "where i.id = ci.id_intervista\n" +
            "and ci.id_candidato =:idCandidato", nativeQuery=true)
    Integer findUltimoIdIntervista(@Param("idCandidato") Integer idCandidato);

    @Query(value=" select distinct c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo, (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore, (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta,\n" +
            "   (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner  \n" +
            "   from candidato c, need n,stato_candidato scc, tipologia_candidato tc,  tipo_candidato ttc,livello_candidato lc, livello_need ln\n" +
            "   where 1= 1" +
            "   and c.id = scc.id_candidato\n" +
            "   and c.id = tc.id_candidato\n" +
            "   and c.id = lc.id_candidato\n" +
            "   and c.id = ttc.id_candidato\n" +
            "   and n.id = ln.id_need\n" +
            "   and n.anni_esperienza <= c.anni_esperienza\n" +
            "   and n.id = :idNeed\n" +
            "   and c.id not in (select id_candidato from need_candidato where id_candidato = c.id and id_need = :idNeed)\n", nativeQuery=true)
    List<Candidato> findCandidatiNonAssociati(@Param("idNeed") Integer idNeed);

    @Query(value=" select distinct c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo, (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore, (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta,\n" +
            " (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner  \n" +
            "   from candidato c, need n,stato_candidato scc, tipologia_candidato tc,  tipo_candidato ttc,livello_candidato lc, livello_need ln\n" +
            "   where 1=1" +
            "   and c.id = scc.id_candidato\n" +
            "   and c.id = tc.id_candidato\n" +
            "   and c.id = lc.id_candidato\n" +
            "   and c.id = ttc.id_candidato\n" +
            "   and n.id = ln.id_need\n" +
            "   and n.anni_esperienza <= c.anni_esperienza\n" +
            "   and n.id = :idNeed\n" +
            "   and c.id not in (select id_candidato from need_candidato where id_candidato = c.id and id_need = :idNeed)\n" +
            "   and if(:nome is not null, c.nome=:nome, 1=1)\n" +
            "   and if(:cognome is not null, c.cognome=:cognome, 1=1)\n" +
            "   and if(:idTipologia is not null, tc.id_tipologia=:idTipologia, 1=1)\n" +
            "   and if(:idTipo is not null, ttc.id_tipo=:idTipo, 1=1) \n" +
            "   and if(:anniMinimi is not null, c.anni_esperienza_ruolo>=:anniMinimi, 1=1) \n" +
            "   and if(:anniMassimi is not null, c.anni_esperienza_ruolo<:anniMassimi, 1=1) ", nativeQuery=true)
    List<Candidato> findRicercaCandidatiNonAssociati(@Param("idNeed") Integer idNeed, @Param("nome") String nome, @Param("cognome") String cognome, @Param("idTipologia") Integer idTipologia, @Param("idTipo") Integer idTipo, @Param("anniMinimi") Integer anniMinimi, @Param("anniMassimi") Integer anniMassimi);

    @Query(value=" select distinct c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo, (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore, (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta,\n" +
            " (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner  \n" +
            " from candidato c, need n,stato_candidato scc, tipologia_candidato tc, tipo_candidato ttc,livello_candidato lc, livello_need ln\n" +
            " where c.id = scc.id_candidato\n" +
            "   and c.id = tc.id_candidato\n" +
            "   and c.id = lc.id_candidato\n" +
            "   and c.id = ttc.id_candidato\n" +
            "   and n.id = ln.id_need\n" +
            "   and n.anni_esperienza <= c.anni_esperienza\n" +
            " and n.id = :idNeed\n" +
            " and c.id in (select id_candidato from need_candidato where id_candidato = c.id and id_need = :idNeed) ", nativeQuery=true)
    List<Candidato> findCandidatiAssociati(@Param("idNeed") Integer idNeed);
}