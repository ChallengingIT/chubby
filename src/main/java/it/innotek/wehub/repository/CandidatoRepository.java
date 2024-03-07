/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Candidato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Integer> {

    Page<Candidato> findAllByOrderByCognomeAsc(Pageable p);

    @Query(value= """
         SELECT c.*, tc.id_tipologia, sc.id_stato, lc.id_livello, ttc.id_tipo,
              (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore,            (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta, \s
              (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner
         FROM candidato c, stato_candidato sc, tipologia_candidato tc,  tipo_candidato ttc,livello_candidato lc
         where c.id = sc.id_candidato
         and c.id = tc.id_candidato
         and c.id = lc.id_candidato
         and c.id = ttc.id_candidato
         and if(?1 is not null, c.nome LIKE ?1%, 1=1)
         and if(?2 is not null, c.cognome LIKE ?2%, 1=1)
         and if(?3 is not null, c.email LIKE ?3%, 1=1)
         and if(?4 is not null, tc.id_tipologia = ?4, 1=1)
         and if(?5 is not null, sc.id_stato=?5, 1=1)
         and if(?6 is not null, ttc.id_tipo=?6, 1=1)
         order by c.cognome asc
        """, nativeQuery=true)
    Page<Candidato> ricercaByNomeAndCognomeAndEmailAndTipologia_IdAndStato_IdAndTipo_Id
        (String nome, String cognome, String email,Integer idTipologia, Integer idStato, Integer idTipo, Pageable p);

    @Query(value= """
         SELECT c.*, tc.id_tipologia, sc.id_stato, lc.id_livello, ttc.id_tipo,
         (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore,
         (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta,
         (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner
         FROM candidato c, stato_candidato sc, tipologia_candidato tc,  tipo_candidato ttc,livello_candidato lc, need_candidato nc
         where c.id = sc.id_candidato
         and c.id = tc.id_candidato
         and c.id = lc.id_candidato
         and c.id = ttc.id_candidato
         and c.id = nc.id_candidato
         and nc.id_need = ?1
         limit 80
        """, nativeQuery=true)
    List<Candidato> findByNeed_Id(Integer idNeed);

    List<Candidato> findByEmail(String email);

    @Query(value=""" 
            SELECT count(*)>0
            FROM fornitore_candidato
            where id_fornitore = ?1
        """, nativeQuery=true)
    Integer findFornitoriAssociati(Integer idFornitore);

    @Query("SELECT coalesce(max(c.id), 0) FROM Candidato c")
    Long findMaxId();

    @Query(value= """
         SELECT coalesce(max(i.id), 0)
         FROM intervista i , candidato_intervista ci
         where i.id = ci.id_intervista
         and ci.id_candidato = ?1
        """, nativeQuery=true)
    Integer findUltimoIdIntervistaCandidato(Integer idCandidato);

    @Query(value= """
         select distinct c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo,
           (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore,
           (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta,
           (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner
         from candidato c, need n,stato_candidato scc, tipologia_candidato tc,  tipo_candidato ttc,livello_candidato lc
         where 1= 1   and c.id = scc.id_candidato
         and c.id = tc.id_candidato
         and c.id = lc.id_candidato
         and c.id = ttc.id_candidato
         and n.id = ?1
         and c.id not in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
         order by c.cognome asc
        """, nativeQuery=true)
    Page<Candidato> findCandidatiNonAssociati(Integer idNeed, Pageable p);

    @Query(value= """
         select distinct c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo,
           (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore,
           (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta,
           (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner
         from candidato c, need n,stato_candidato scc, tipologia_candidato tc,  tipo_candidato ttc,livello_candidato lc
         where 1=1   and c.id = scc.id_candidato
         and c.id = tc.id_candidato
         and c.id = lc.id_candidato
         and c.id = ttc.id_candidato
         and n.id = ?1
         and c.id not in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
         and if(?2 is not null, c.nome LIKE ?2%, 1=1)
         and if(?3 is not null, c.cognome LIKE ?3%, 1=1)
         and if(?4 is not null, tc.id_tipologia = ?4, 1=1)
         and if(?5 is not null, ttc.id_tipo = ?5, 1=1)
         and if(?6 is not null, c.anni_esperienza_ruolo >= ?6, 1=1)
         and if(?7 is not null, c.anni_esperienza_ruolo < ?7, 1=1)
         limit 80
        """, nativeQuery=true)
    Page<Candidato> ricercaCandidatiNonAssociati(Integer idNeed, String nome, String cognome,
                                                 Integer idTipologia, Integer idTipo, Integer anniMinimi,
                                                 Integer anniMassimi, Pageable p);

    @Query(value= """
         select distinct c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo,
           (ifnull ((select id_fornitore from fornitore_candidato where id_candidato = c.id),null)) id_fornitore,
           (ifnull ((select id_facolta from facolta_candidato where id_candidato = c.id),null)) id_facolta,
           (ifnull ((select id_owner from candidato_owner where id_candidato = c.id),null)) id_owner
         from candidato c, need n,stato_candidato scc, tipologia_candidato tc, tipo_candidato ttc,livello_candidato lc
         where c.id = scc.id_candidato
         and c.id = tc.id_candidato
         and c.id = lc.id_candidato
         and c.id = ttc.id_candidato
         and n.id = ?1
         and c.id in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
         order by c.cogmome asc
        """, nativeQuery=true)
    Page<Candidato> findCandidatiAssociati(Integer idNeed, Pageable p);
}