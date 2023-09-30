/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.staff.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    @Query("SELECT coalesce(max(s.id), 0) FROM Staff s")
    Long findMaxId();

    List<Staff> findByEmail(String email);

    @Query(value= """
         SELECT distinct s.*,
          (ifnull ((select id_calendario from calendario_staff where id_staff = s.id),null)) id_calendario,
          (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia,
          (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello,
          (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto,
          (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta
         FROM staff s
         where SUBSTRING_INDEX(s.email, '@', 1) = ?1
        """, nativeQuery=true)
    Staff findByUsername(String username);

    @Query(value= """
         SELECT s.*,
           (ifnull ((select id_calendario from calendario_staff where id_staff = s.id),null)) id_calendario,
           (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia,
           (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello,
           (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto,
           (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta
         FROM staff s
         where if(?1 is not null, s.nome like ?1%, 1=1)
         and if(?2 is not null, s.cognome like ?2%, 1=1)
         and if(?3 is not null, s.email like ?3%, 1=1)
        """, nativeQuery=true)
    List<Staff> ricercaByNomeAndCognomeAndEmail(String nome, String cognome, String email);


    @Query(value= """
         SELECT s.*, cs.id_calendario,
           (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia,
           (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello,
           (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto,
           (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta
         FROM staff s, calendario_staff cs, calendario_anno ca, anno a, anno_mese am, mese m
         where cs.id_staff = s.id
         and ca.id_calendario = cs.id_calendario
         and ca.id_anno = a.id
         and a.id = am.id_anno
         and am.id_mese = m.id
         and a.anno = ?1
         and m.value = ?2
        """, nativeQuery=true)
    List<Staff> findByAnnoAndMese(Integer anno, Integer mese);


    @Query(value= """
         SELECT distinct s.*,
           (ifnull ((select id_calendario from calendario_staff where id_staff = s.id),null)) id_calendario,
           (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia,
           (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello,
           (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto,
           (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta
         FROM staff s, progetto_staff ps
         where s.id = ps.id_staff
         and ps.id_progetto = ?1
        """, nativeQuery=true)
    Staff findByProgetto_Id(Integer idProgetto);

    @Query(value= """
         select distinct s.*, cs.id_calendario,
           (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia,
           (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello,
           (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto,
           (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta
         from staff s, progetto p, progetto_staff ps, calendario_staff cs
         where s.id = ps.id_staff
         and s.id =  cs.id_staff
         and p.id = ?1
         and s.id not in (select id_staff from progetto_staff where id_staff = s.id and id_progetto = ?1)
        """, nativeQuery=true)
    List<Staff> findStaffNonAssociati(Integer idProgetto);

    @Query(value= """
         select distinct s.*, cs.id_calendario,
           (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia,
           (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello,
           (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto,
           (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta
         from staff s, progetto p, progetto_staff ps, calendario_staff cs
         where s.id = ps.id_staff
         and s.id =  cs.id_staff
         and p.id = ?1
         and s.id not in (select id_staff from progetto_staff where id_staff = s.id and id_progetto = ?1)
         and if(?2 is not null, s.nome like ?2%, 1=1)
         and if(?3 is not null, s.cognome like ?3%, 1=1)
         and if(?4 is not null, s.email like ?4%, 1=1)
        """, nativeQuery=true)
    List<Staff> ricercaStaffNonAssociati(Integer idProgetto, String nome, String cognome, String email);

    @Query(value= """
         select distinct s.*, cs.id_calendario,
           (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia,
           (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello,
           (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto,
           (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta
         from staff s, progetto p, progetto_staff ps, calendario_staff cs
         where s.id = ps.id_staff
         and s.id =  cs.id_staff
         and p.id = ?1
         and s.id in (select id_staff from progetto_staff where id_staff = s.id and id_progetto = ?1)
        """, nativeQuery=true)
    List<Staff> findStaffAssociati(Integer idProgetto);
}