/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Intervista;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface IntervistaRepository extends CrudRepository<Intervista, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT i.*, ci.id_candidato, io.id_owner, si.id_stato, ti.id_tipologia \n" +
                 " FROM intervista i, candidato_intervista ci, intervista_owner io, stato_intervista si, tipologia_intervista ti\n" +
                 " where i.id = ci.id_intervista\n" +
                 " and i.id = io.id_intervista\n" +
                 " and i.id = si.id_intervista\n" +
                 " and i.id = ti.id_intervista\n" +
                 " and ci.id_candidato = :idCandidato", nativeQuery=true)
    List<Intervista> findAllByCandidati(@Param("idCandidato") Integer idCandidato);

    @Query(value=" SELECT i.*, ci.id_candidato, io.id_owner, si.id_stato, ti.id_tipologia \n" +
            " FROM intervista i, candidato_intervista ci, intervista_owner io, stato_intervista si, tipologia_intervista ti\n" +
            " where i.id = ci.id_intervista\n" +
            " and i.id = io.id_intervista\n" +
            " and i.id = si.id_intervista\n" +
            " and i.id = ti.id_intervista\n" +
            " and ci.id_candidato = :idCandidato\n" +
            " order by i.id desc\n" +
            " limit 1 ", nativeQuery=true)
    Intervista findByCandidato(@Param("idCandidato") Integer idCandidato);

    @Query(value=" SELECT i.*, ci.id_candidato, io.id_owner, si.id_stato, ti.id_tipologia " +
            "       FROM intervista i, candidato_intervista ci, intervista_owner io, stato_intervista si, tipologia_intervista ti" +
            "       where i.id = ci.id_intervista " +
            "       and i.id = io.id_intervista " +
            "       and i.id = si.id_intervista " +
            "       and i.id = ti.id_intervista\n" +
            "       and ci.id_candidato = :idCandidato" +
            "       and if(:idStato is not null, si.id_stato =:idStato, 1=1) " +
            "       and if(:idOwner is not null, io.id_owner =:idOwner, 1=1) " +
            "       and if(:dataColloquio is not null, i.data_colloquio<=:dataColloquio, 1=1) ", nativeQuery=true)
    List<Intervista> findRicerca(@Param("idStato") Integer idStato, @Param("idOwner") Integer idOwner, @Param("dataColloquio") Date dataColloquio, @Param("idCandidato") Integer idCandidato);

    @Query(value=" SELECT i.*, ci.id_candidato, io.id_owner, si.id_stato, ti.id_tipologia \n" +
            "FROM intervista i, tipologia_intervista ti, tipologiei tei, candidato_intervista ci, intervista_owner io, stato_intervista si\n" +
            "where i.id = ti.id_intervista \n" +
            "and ci.id_intervista = i.id \n" +
            "and ti.id_tipologia = tei.id\n" +
            "and i.id = io.id_intervista\n" +
            "and i.id = si.id_intervista\n" +
            "and ora_aggiornamento is not null\n" +
            "and i.id in ( select max(id_intervista) from candidato_intervista group by id_candidato )\n" +
            "order by ora_aggiornamento " +
            "limit 6 ", nativeQuery=true)
    List<Intervista> findIntervisteImminenti();


    @Query(value=" SELECT i.*, io.id_owner, ci.id_candidato, si.id_stato, ti.id_tipologia \n" +
        "FROM intervista_owner io, tipologia_intervista ti, tipologiei tei, intervista i, candidato_intervista ci, stato_intervista si \n" +
        "where io.id_intervista = i.id\n" +
        "and i.id = ci.id_intervista\n" +
        "and i.id = ti.id_intervista\n" +
        "and i.id = si.id_intervista\n" +
        "and ti.id_tipologia = tei.id\n" +
        "and week(i.data_colloquio) = week(curdate()) ", nativeQuery=true)
    List<Intervista> findIntervisteSettimanaCur();

    @Query(value=" SELECT i.*, io.id_owner, ci.id_candidato, si.id_stato, ti.id_tipologia \n" +
        "FROM intervista_owner io, tipologia_intervista ti, tipologiei tei, intervista i, candidato_intervista ci, stato_intervista si \n" +
        "where io.id_intervista = i.id\n" +
        "and i.id = ci.id_intervista\n" +
        "and i.id = ti.id_intervista\n" +
        "and i.id = si.id_intervista\n" +
        "and ti.id_tipologia = tei.id\n" +
        "and week(i.data_colloquio) = week(DATE_SUB(curdate(), interval 1 week)) ", nativeQuery=true)
    List<Intervista> findIntervisteSettimanaCurMeno();

    @Query(value=" SELECT i.*, io.id_owner, ci.id_candidato, si.id_stato, ti.id_tipologia \n" +
        "FROM intervista_owner io, tipologia_intervista ti, tipologiei tei, intervista i, candidato_intervista ci, stato_intervista si \n" +
        "where io.id_intervista = i.id\n" +
        "and i.id = ci.id_intervista\n" +
        "and i.id = ti.id_intervista\n" +
        "and i.id = si.id_intervista\n" +
        "and ti.id_tipologia = tei.id\n" +
        "and week(i.data_colloquio) = week(DATE_ADD(curdate(), interval 1 week)) ", nativeQuery=true)
    List<Intervista> findIntervisteSettimanaCurPiu();
}