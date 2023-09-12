/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.timesheet.Progetto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProgettoRepository extends CrudRepository<Progetto, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT distinct p.*, pc.id_cliente, ps.id_staff, tp.id_tipologia \n" +
            "             FROM progetto p, progetto_cliente pc, progetto_staff ps, tipologia_progetto_progetto tp\n" +
            "             where p.id = pc.id_progetto " +
            "             and p.id = ps.id_progetto" +
            "             and p.id = tp.id_progetto" +
            "             and if(:denominazione is not null, p.description like %:denominazione%, 1=1)\n" +
            "             and if(:idCliente is not null, pc.id_cliente = :idCliente, 1=1)", nativeQuery=true)
    List<Progetto> findRicerca(@Param("denominazione") String denominazione, @Param("idCliente") Integer idCliente);

    @Query(value=" SELECT distinct p.*, pc.id_cliente, ps.id_staff, tp.id_tipologia \n" +
    "             FROM progetto p, progetto_cliente pc, progetto_staff ps, tipologia_progetto_progetto tp\n" +
    "             where p.id = pc.id_progetto " +
    "             and p.id = tp.id_progetto" +
    "             and p.id = ps.id_progetto", nativeQuery=true)
    List<Progetto> listAll();


    @Query(value="select distinct p.*, pc.id_cliente, ps.id_staff, tp.id_tipologia\n"
    +    "    from progetto p\n"
    +    "    left outer join progetto_cliente pc on p.id = pc.id_progetto\n"
    +    "    left outer join progetto_staff ps on p.id = ps.id_progetto\n"
    +    "    left outer join tipologia_progetto_progetto tp on p.id = tp.id_progetto\n"
    +    "    where p.id= :id\n"
    +    "    and ps.id_staff = :idStaff", nativeQuery=true)
    Progetto findByIdStaff(@Param("id") Integer id, @Param("idStaff") Integer idStaff);

    @Query(value="select distinct p.*, pc.id_cliente, ps.id_staff, tp.id_tipologia\n"
        +    "    from progetto p\n"
        +    "    left outer join progetto_cliente pc on p.id = pc.id_progetto\n"
        +    "    left outer join progetto_staff ps on p.id = ps.id_progetto\n"
        +    "    left outer join tipologia_progetto_progetto tp on p.id = tp.id_progetto\n"
        +    "    where ps.id_staff = :idStaff", nativeQuery=true)
    List<Progetto> findByIdStaff(@Param("idStaff") Integer idStaff);

    @Query(value="SELECT distinct p.*, pc.id_cliente, ps.id_staff, tp.id_tipologia\n" +
            "    from progetto p, progetto_cliente pc, progetto_staff ps, tipologia_progetto_progetto tp\n" +
            "    where p.id = pc.id_progetto\n" +
            "    and p.id = ps.id_progetto " +
            "    and p.id = tp.id_progetto " +
            "    and pc.id_cliente = :id", nativeQuery=true)
    List<Progetto> findByIdCliente(@Param("id") Integer id);

    @Query(value="select sum(g.ore_totali)\n" +
        " from staff s , progetto_staff ps, giorno g, anno_mese am," +
        " mese_giorno mg, anno a, mese m, progetto p, calendario_anno ca, " +
        " calendario_staff cs, progetto_giorno pg\n" +
        "where s.id = ps.id_staff\n" +
        " and g.id = mg.id_giorno\n" +
        "and p.id = ps.id_progetto\n" +
        " and ca.id_anno = a.id\n" +
        " and cs.id_calendario = ca.id_calendario\n" +
        " and cs.id_staff = s.id\n" +
        " and g.id = pg.id_giorno\n" +
        " and pg.id_progetto = p.id\n" +
        " and mg.id_mese = am.id_mese\n" +
        " and a.id = am.id_anno\n" +
        " and m.id = mg.id_mese\n" +
        " and s.id = :idStaff\n" +
        " and p.id = :idProgetto\n", nativeQuery=true)
    Integer getOreEffettive(@Param("idProgetto") Integer idProgetto, @Param("idStaff") Integer idStaff);

    @Query(value="select max(id) from wehub.progetto", nativeQuery=true)
    Integer findMaxId();

    @Transactional
    @Modifying
    @Query(value=" DELETE \n" +
        "FROM progetto_cliente \n" +
        "where id_cliente = :idCliente ", nativeQuery=true)
    void deleteByIdCliente(@Param("idCliente") Integer idCliente);
}