/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Intervista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface IntervistaRepository extends JpaRepository<Intervista, Integer> {

    List<Intervista> findByCandidato_Id(Integer idCandidato);

    Intervista findByCandidato_IdOrderByIdAsc(Integer idCandidato);

    @Query(value= """
                SELECT i.*, ci.id_candidato, io.id_owner, si.id_stato, ti.id_tipologia
                FROM intervista i, candidato_intervista ci, intervista_owner io, stato_intervista si, tipologia_intervista ti
                where i.id = ci.id_intervista
                and i.id = io.id_intervista
                and i.id = si.id_intervista
                and i.id = ti.id_intervista
                and ci.id_candidato = ?4
                and if(?1 is not null, si.id_stato = ?1, 1=1)
                and if(?2 is not null, io.id_owner = ?2, 1=1)
                and if(?3 is not null, i.data_colloquio <= ?3, 1=1)
        """, nativeQuery=true)
    List<Intervista> ricercaByStato_IdAndOwner_IdAndDataColloquioAndCandidato_Id(Integer idStato, Integer idOwner, Date dataColloquio, Integer idCandidato);

    @Query(value= """
         SELECT i.*, ci.id_candidato, io.id_owner, si.id_stato, ti.id_tipologia
         FROM intervista i, tipologia_intervista ti, tipologiei tei, candidato_intervista ci, intervista_owner io, stato_intervista si
         where i.id = ti.id_intervista
         and ci.id_intervista = i.id
         and ti.id_tipologia = tei.id
         and i.id = io.id_intervista
         and i.id = si.id_intervista
         and ora_aggiornamento is not null
         and i.id in ( select max(id_intervista) from candidato_intervista group by id_candidato )
         order by ora_aggiornamento limit 6
        """, nativeQuery=true)
    List<Intervista> findIntervisteImminenti();


    @Query(value= """
         SELECT i.*, io.id_owner, ci.id_candidato, si.id_stato, ti.id_tipologia
         FROM intervista_owner io, tipologia_intervista ti, tipologiei tei, intervista i, candidato_intervista ci, stato_intervista si
         where io.id_intervista = i.id
         and i.id = ci.id_intervista
         and i.id = ti.id_intervista
         and i.id = si.id_intervista
         and ti.id_tipologia = tei.id
         and week(i.data_colloquio) = week(curdate())
        """, nativeQuery=true)
    List<Intervista> findIntervisteSettimanaCur();

    @Query(value= """
         SELECT i.*, io.id_owner, ci.id_candidato, si.id_stato, ti.id_tipologia
         FROM intervista_owner io, tipologia_intervista ti, tipologiei tei, intervista i, candidato_intervista ci, stato_intervista si
         where io.id_intervista = i.id
         and i.id = ci.id_intervista
         and i.id = ti.id_intervista
         and i.id = si.id_intervista
         and ti.id_tipologia = tei.id
         and week(i.data_colloquio) = week(DATE_SUB(curdate(), interval 1 week))
        """, nativeQuery=true)
    List<Intervista> findIntervisteSettimanaCurMeno();

    @Query(value= """
         SELECT i.*, io.id_owner, ci.id_candidato, si.id_stato, ti.id_tipologia
         FROM intervista_owner io, tipologia_intervista ti, tipologiei tei, intervista i, candidato_intervista ci, stato_intervista si
         where io.id_intervista = i.id
         and i.id = ci.id_intervista
         and i.id = ti.id_intervista
         and i.id = si.id_intervista
         and ti.id_tipologia = tei.id
         and week(i.data_colloquio) = week(DATE_ADD(curdate(), interval 1 week))
        """, nativeQuery=true)
    List<Intervista> findIntervisteSettimanaCurPiu();
}