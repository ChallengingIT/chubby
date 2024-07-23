/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Candidato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Integer> {

    Page<Candidato> findAllByOrderByCognomeAsc(Pageable p);

    List<Candidato> findAllByOrderByCognomeAsc();

    @Query(value= " SELECT c.nome FROM candidato c ", nativeQuery=true)
    List<String> findAllNames();

    @Query(value= " SELECT c.cognome FROM candidato c ", nativeQuery=true)
    List<String> findAllSurnames();

    @Query(value= " SELECT c.residenza FROM candidato c ", nativeQuery=true)
    List<String> findAllCity();

    @Query(value= " SELECT c.disponibilita FROM candidato c ", nativeQuery=true)
    List<String> findAllDisponibilita();

    @Query(value= " SELECT c.modalita FROM candidato c ", nativeQuery=true)
    List<String> findAllModalita();

    @Query(value= " SELECT c.ral FROM candidato c ", nativeQuery=true)
    List<String> findAllRal();

    @Query(value= " SELECT c.email FROM candidato c ", nativeQuery=true)
    List<String> findAllEmail();

    @Query(value= """
         select c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo, fc.id_fornitore,
          fac.id_facolta, co.id_owner, tcc.id_tipo_candidatura, trc.id_tipo_ricerca
            from candidato c
            left join fornitore_candidato fc on (c.id = fc.id_candidato )
            left join facolta_candidato fac on (c.id = fac.id_candidato )
            left join candidato_owner co on (c.id = co.id_candidato )
            left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
            left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
            left join skill_candidato sc on (c.id = sc.id_candidato )
            join stato_candidato scc on (c.id = scc.id_candidato)
            join tipologia_candidato tc on (c.id = tc.id_candidato)
            left join tipo_candidato ttc on (c.id = ttc.id_candidato)
            join livello_candidato lc on (c.id = lc.id_candidato)
            where 1 = 1
            (?1)
        """, nativeQuery=true)
    List<Candidato> findByWhere(String where);

    @Query(value= """
         SELECT c.*, tc.id_tipologia, sc.id_stato, lc.id_livello, ttc.id_tipo, fc.id_fornitore,
          fac.id_facolta, co.id_owner, tcc.id_tipo_candidatura, trc.id_tipo_ricerca
         FROM candidato c
         left join fornitore_candidato fc on (c.id = fc.id_candidato )
         left join facolta_candidato fac on (c.id = fac.id_candidato )
         left join candidato_owner co on (c.id = co.id_candidato )
         join stato_candidato sc on (c.id = sc.id_candidato)
         join tipologia_candidato tc on (c.id = tc.id_candidato)
         left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
         left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
         left join tipo_candidato ttc on (c.id = ttc.id_candidato)
         join livello_candidato lc on (c.id = lc.id_candidato)
         where if(?1 is not null, c.nome LIKE ?1%, 1=1)
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
         SELECT count(*)
         FROM candidato c
         left join fornitore_candidato fc on (c.id = fc.id_candidato )
         left join facolta_candidato fac on (c.id = fac.id_candidato )
         left join candidato_owner co on (c.id = co.id_candidato )
         join stato_candidato sc on (c.id = sc.id_candidato)
         join tipologia_candidato tc on (c.id = tc.id_candidato)
         left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
         left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
         left join tipo_candidato ttc on (c.id = ttc.id_candidato)
         join livello_candidato lc on (c.id = lc.id_candidato)
         where if(?1 is not null, c.nome LIKE ?1%, 1=1)
         and if(?2 is not null, c.cognome LIKE ?2%, 1=1)
         and if(?3 is not null, c.email LIKE ?3%, 1=1)
         and if(?4 is not null, tc.id_tipologia = ?4, 1=1)
         and if(?5 is not null, sc.id_stato=?5, 1=1)
         and if(?6 is not null, ttc.id_tipo=?6, 1=1)
        """, nativeQuery=true)
    Long countRicercaByNomeAndCognomeAndEmailAndTipologia_IdAndStato_IdAndTipo_Id
        (String nome, String cognome, String email,Integer idTipologia, Integer idStato, Integer idTipo);

    List<Candidato> findByEmail(String email);

    @Query(value= """
         select c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo, fc.id_fornitore,
         fac.id_facolta, co.id_owner, tcc.id_tipo_candidatura, trc.id_tipo_ricerca
            from candidato c
            left join fornitore_candidato fc on (c.id = fc.id_candidato )
            left join facolta_candidato fac on (c.id = fac.id_candidato )
            left join candidato_owner co on (c.id = co.id_candidato )
            join stato_candidato scc on (c.id = scc.id_candidato)
            join tipologia_candidato tc on (c.id = tc.id_candidato)
            left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
            left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
            left join tipo_candidato ttc on (c.id = ttc.id_candidato)
            join livello_candidato lc on (c.id = lc.id_candidato)
            where c.id not in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
            order by c.cognome asc
        """, nativeQuery=true)
    Page<Candidato> findCandidatiNonAssociati(Integer idNeed, Pageable p);

    @Query(value= """
         select count(*)
            from candidato c
            left join fornitore_candidato fc on (c.id = fc.id_candidato )
            left join facolta_candidato fac on (c.id = fac.id_candidato )
            left join candidato_owner co on (c.id = co.id_candidato )
            join stato_candidato scc on (c.id = scc.id_candidato)
            left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
            left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
            join tipologia_candidato tc on (c.id = tc.id_candidato)
            left join tipo_candidato ttc on (c.id = ttc.id_candidato)
            join livello_candidato lc on (c.id = lc.id_candidato)
            where c.id not in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
            order by c.cognome asc
        """, nativeQuery=true)
    Long countCandidatiNonAssociati(Integer idNeed);

    @Query(value= """
         select c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo, fc.id_fornitore,
          fac.id_facolta, co.id_owner, tcc.id_tipo_candidatura, trc.id_tipo_ricerca
         from candidato c
         left join fornitore_candidato fc on (c.id = fc.id_candidato )
         left join facolta_candidato fac on (c.id = fac.id_candidato )
         left join candidato_owner co on (c.id = co.id_candidato )
         join stato_candidato scc on (c.id = scc.id_candidato)
         left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
         left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
         join tipologia_candidato tc on (c.id = tc.id_candidato)
         left join tipo_candidato ttc on (c.id = ttc.id_candidato)
         join livello_candidato lc on (c.id = lc.id_candidato)
         where c.id not in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
         and if(?2 is not null, c.nome LIKE ?2%, 1=1)
         and if(?3 is not null, c.cognome LIKE ?3%, 1=1)
         and if(?4 is not null, tc.id_tipologia = ?4, 1=1)
         and if(?5 is not null, ttc.id_tipo = ?5, 1=1)
         and if(?6 is not null, c.anni_esperienza_ruolo >= ?6, 1=1)
         and if(?7 is not null, c.anni_esperienza_ruolo < ?7, 1=1)
         order by c.cognome asc
        """, nativeQuery=true)
    Page<Candidato> ricercaCandidatiNonAssociati(Integer idNeed, String nome, String cognome,
                                                 Integer idTipologia, Integer idTipo, Integer anniMinimi,
                                                 Integer anniMassimi, Pageable p);

    @Query(value= """
         select count(*)
         from candidato c
         left join fornitore_candidato fc on (c.id = fc.id_candidato )
         left join facolta_candidato fac on (c.id = fac.id_candidato )
         left join candidato_owner co on (c.id = co.id_candidato )
         join stato_candidato scc on (c.id = scc.id_candidato)
         left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
         left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
         join tipologia_candidato tc on (c.id = tc.id_candidato)
         left join tipo_candidato ttc on (c.id = ttc.id_candidato)
         join livello_candidato lc on (c.id = lc.id_candidato)
         where c.id not in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
         and if(?2 is not null, c.nome LIKE ?2%, 1=1)
         and if(?3 is not null, c.cognome LIKE ?3%, 1=1)
         and if(?4 is not null, tc.id_tipologia = ?4, 1=1)
         and if(?5 is not null, ttc.id_tipo = ?5, 1=1)
         and if(?6 is not null, c.anni_esperienza_ruolo >= ?6, 1=1)
         and if(?7 is not null, c.anni_esperienza_ruolo < ?7, 1=1)
         order by c.cognome asc
        """, nativeQuery=true)
    Long countRicercaCandidatiNonAssociati(Integer idNeed, String nome, String cognome,
                                                 Integer idTipologia, Integer idTipo, Integer anniMinimi,
                                                 Integer anniMassimi);

    @Query(value= """
         select distinct c.*, tc.id_tipologia, scc.id_stato, lc.id_livello, ttc.id_tipo, fc.id_fornitore,
          fac.id_facolta, co.id_owner, tcc.id_tipo_candidatura, trc.id_tipo_ricerca
         from candidato c
         left join fornitore_candidato fc on (c.id = fc.id_candidato )
         left join facolta_candidato fac on (c.id = fac.id_candidato )
         left join candidato_owner co on (c.id = co.id_candidato )
         left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
         left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
         left join file_candidato ffc on (c.id = ffc.id_candidato )
         left join tipologia_file tfc on (tfc.id_file = ffc.id_file )
         join stato_candidato scc on (c.id = scc.id_candidato)
         join tipologia_candidato tc on (c.id = tc.id_candidato)
         left join tipo_candidato ttc on (c.id = ttc.id_candidato)
         join livello_candidato lc on (c.id = lc.id_candidato)
         where c.id  in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
         and ( tfc.id_tipologia = 1 or tfc.id_tipologia = 2)
         order by c.cognome asc
        """, nativeQuery=true)
    Page<Candidato> findCandidatiAssociati(Integer idNeed, Pageable p);

    @Query(value= """
         select count(*)
         from candidato c
         left join fornitore_candidato fc on (c.id = fc.id_candidato )
         left join facolta_candidato fac on (c.id = fac.id_candidato )
         left join candidato_owner co on (c.id = co.id_candidato )
         left join tipo_candidatura_candidato tcc on (c.id = tcc.id_candidato )
         left join tipo_ricerca_candidato trc on (c.id = trc.id_candidato )
         join stato_candidato scc on (c.id = scc.id_candidato)
         join tipologia_candidato tc on (c.id = tc.id_candidato)
         left join tipo_candidato ttc on (c.id = ttc.id_candidato)
         join livello_candidato lc on (c.id = lc.id_candidato)
         where c.id  in (select id_candidato from need_candidato where id_candidato = c.id and id_need = ?1)
         order by c.cognome asc
        """, nativeQuery=true)
    Long countCandidatiAssociati(Integer idNeed);

}