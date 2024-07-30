/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Intervista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface IntervistaRepository extends JpaRepository<Intervista, Integer> {

    @Query("select count(i) from Intervista i where i.candidato.id = ?1")
    long countByCandidato_Id(Integer id);

    List<Intervista> findByCandidato_Id(Integer idCandidato);

    Page<Intervista> findByCandidato_IdOrderByDataColloquioDesc(Integer idCandidato, Pageable p);

    List<Intervista> findByCandidato_IdOrderByDataColloquioDesc(Integer idCandidato);

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
                order by i.data_colloquio desc
        """, nativeQuery=true)
    Page<Intervista> ricercaByStato_IdAndOwner_IdAndDataColloquioAndCandidato_Id(Integer idStato, Integer idOwner, Date dataColloquio, Integer idCandidato, Pageable p);


    @Query(value= """
                SELECT count(*)
                FROM intervista i, candidato_intervista ci, intervista_owner io, stato_intervista si, tipologia_intervista ti
                where i.id = ci.id_intervista
                and i.id = io.id_intervista
                and i.id = si.id_intervista
                and i.id = ti.id_intervista
                and ci.id_candidato = ?4
                and if(?1 is not null, si.id_stato = ?1, 1=1)
                and if(?2 is not null, io.id_owner = ?2, 1=1)
                and if(?3 is not null, i.data_colloquio <= ?3, 1=1)
                order by i.data_colloquio desc
        """, nativeQuery=true)
    Long countRicercaByStato_IdAndOwner_IdAndDataColloquioAndCandidato_Id(Integer idStato, Integer idOwner, Date dataColloquio, Integer idCandidato);


    @Query("SELECT coalesce(max(i.id), 0) FROM Intervista i")
    Integer findMaxId();

    @Query(value= """
                SELECT i.*, ci.id_candidato, io.id_owner, ino.id_owner as id_next_owner, si.id_stato, ti.id_tipologia
                FROM intervista i
                left join candidato_intervista ci on (i.id = ci.id_intervista)
                left join intervista_owner io on (i.id = io.id_intervista)
                left join intervista_next_owner ino on (i.id = ino.id_intervista)
                left join stato_intervista si on (i.id = si.id_intervista)
                left join tipologia_intervista ti on (i.id = ti.id_intervista)
                left join owner o on ino.id_owner = o.id
                left join users u on o.nome = u.nome and o.cognome = u.cognome
                where u.username = ?1
                and WEEK(DATE(i.ora_aggiornamento)) = WEEK(DATE(now()))
                order by i.ora_aggiornamento desc
        """, nativeQuery=true)
    Page<Intervista> ricercaAttivitaByUsername(String username, Pageable p);

    @Query(value= """
                SELECT i.*, ci.id_candidato, io.id_owner, ino.id_owner as id_next_owner, si.id_stato, ti.id_tipologia
                FROM intervista i
                left join candidato_intervista ci on (i.id = ci.id_intervista)
                left join intervista_owner io on (i.id = io.id_intervista)
                left join intervista_next_owner ino on (i.id = ino.id_intervista)
                left join stato_intervista si on (i.id = si.id_intervista)
                left join tipologia_intervista ti on (i.id = ti.id_intervista)
                left join owner o on ino.id_owner = o.id
                left join users u on o.nome = u.nome and o.cognome = u.cognome
                where u.username = ?1
                and WEEK(DATE(i.ora_aggiornamento)) = WEEK(DATE(now() + INTERVAL ?2 WEEK ))
                order by i.ora_aggiornamento desc
        """, nativeQuery=true)
    Page<Intervista> ricercaAttivitaByUsernameInterval(String username, Integer interval,  Pageable p);


    @Query(value= """
                SELECT i.*, ci.id_candidato, io.id_owner , ino.id_owner as id_next_owner, si.id_stato, ti.id_tipologia
                FROM intervista i
                left join candidato_intervista ci on (i.id = ci.id_intervista)
                left join intervista_owner io on (i.id = io.id_intervista)
                left join intervista_next_owner ino on (i.id = ino.id_intervista)
                left join stato_intervista si on (i.id = si.id_intervista)
                left join tipologia_intervista ti on (i.id = ti.id_intervista)
                left join owner o on ino.id_owner = o.id
                where WEEK(DATE(i.ora_aggiornamento)) = WEEK(DATE(now()))
                order by i.ora_aggiornamento desc
        """, nativeQuery=true)
    Page<Intervista> ricercaAttivita(Pageable p);

    @Query(value= """
                SELECT i.*, ci.id_candidato, io.id_owner , ino.id_owner as id_next_owner, si.id_stato, ti.id_tipologia
                FROM intervista i
                left join candidato_intervista ci on (i.id = ci.id_intervista)
                left join intervista_owner io on (i.id = io.id_intervista)
                left join intervista_next_owner ino on (i.id = ino.id_intervista)
                left join stato_intervista si on (i.id = si.id_intervista)
                left join tipologia_intervista ti on (i.id = ti.id_intervista)
                left join owner o on ino.id_owner = o.id
                where WEEK(DATE(i.ora_aggiornamento)) = WEEK(DATE(now() + INTERVAL ?1 WEEK))
                order by i.ora_aggiornamento desc
        """, nativeQuery=true)
    Page<Intervista> ricercaAttivitaInterval(Integer interval, Pageable p);
}