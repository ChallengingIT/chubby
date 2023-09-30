/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.timesheet.Progetto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgettoRepository extends JpaRepository<Progetto, Integer> {

    List<Progetto> findByStaff_Id(Integer id);

    @Query(value= """
         SELECT distinct p.*, pc.id_cliente, ps.id_staff, tp.id_tipologia
         FROM progetto p, progetto_cliente pc, progetto_staff ps, tipologia_progetto_progetto tp
         where p.id = pc.id_progetto
         and p.id = ps.id_progetto
         and p.id = tp.id_progetto
         and if(?1 is not null, p.description like %?1%, 1=1)
         and if(?2 is not null, pc.id_cliente = ?2, 1=1)\
        """, nativeQuery=true)
    List<Progetto> ricercaByDenominazioneAndIdCliente(String denominazione, Integer idCliente);

    @Query(value= """
         select sum(g.ore_totali)
         from staff s , progetto_staff ps, giorno g, anno_mese am, mese_giorno mg, anno a, mese m,
          progetto p, calendario_anno ca,  calendario_staff cs, progetto_giorno pg
         where s.id = ps.id_staff
         and g.id = mg.id_giorno
         and p.id = ps.id_progetto
         and ca.id_anno = a.id
         and cs.id_calendario = ca.id_calendario
         and cs.id_staff = s.id
         and g.id = pg.id_giorno
         and pg.id_progetto = p.id
         and mg.id_mese = am.id_mese
         and a.id = am.id_anno
         and m.id = mg.id_mese
         and p.id = ?1
         and s.id = ?2
        """, nativeQuery=true)
    Integer getOreEffettive(Integer idProgetto, Integer idStaff);

    @Query("SELECT coalesce(max(p.id), 0) FROM Progetto p")
    Long findMaxId();
}