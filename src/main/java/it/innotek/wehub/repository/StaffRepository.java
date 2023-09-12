/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.staff.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaffRepository extends CrudRepository<Staff, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT max(id) from staff", nativeQuery=true)
    Integer findUltimoId();

    @Query(value=" SELECT if(count(*)=1,1,0)\n" +
            "FROM staff s\n" +
            "where email = :email ", nativeQuery=true)
    Integer checkEmail(@Param("email") String email);

    @Query(value=" SELECT distinct s.*, (ifnull ((select id_calendario from calendario_staff where id_staff = s.id),null)) id_calendario, (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia, (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello, (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto, (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta \n" +
            " FROM staff s\n" +
            " where SUBSTRING_INDEX(s.email, '@', 1) = :username ", nativeQuery=true)
    Staff findByUsername(@Param("username") String username);

    @Query(value=" SELECT s.*, (ifnull ((select id_calendario from calendario_staff where id_staff = s.id),null)) id_calendario, (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia, (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello, (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto, (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta   \n" +
            "             FROM staff s\n" +
            "             where if(:nome is not null, s.nome=:nome, 1=1)\n" +
            "             and if(:cognome is not null, s.cognome=:cognome, 1=1)\n" +
            "             and if(:email is not null, s.email=:email, 1=1) ", nativeQuery=true)
    List<Staff> findRicerca(@Param("nome") String nome, @Param("cognome") String cognome, @Param("email") String email);


    @Query(value=" SELECT s.*, cs.id_calendario, (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia, (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello, (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto, (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta \n" +
            "                         FROM staff s, calendario_staff cs, calendario_anno ca, anno a, anno_mese am, mese m\n" +
            "                         where cs.id_staff = s.id\n" +
            "                         and ca.id_calendario = cs.id_calendario\n" +
            "                         and ca.id_anno = a.id\n" +
            "                         and a.id = am.id_anno\n" +
            "                         and am.id_mese = m.id\n" +
            "                         and a.anno = :anno\n" +
            "                         and m.value = :mese ", nativeQuery=true)
    List<Staff> findByCalendario(@Param("anno") Integer anno, @Param("mese") Integer mese);


    @Query(value=" SELECT distinct s.*, (ifnull ((select id_calendario from calendario_staff where id_staff = s.id),null)) id_calendario, (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia, (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello, (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto, (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta \n" +
                 " FROM staff s, progetto_staff ps\n" +
                 " where s.id = ps.id_staff\n" +
                 " and ps.id_progetto = :idProgetto ", nativeQuery=true)
    Staff findStaffByIdProgetto(@Param("idProgetto") Integer idProgetto);

    @Query(value=" select distinct s.*, cs.id_calendario, (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia, (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello, (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto, (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta \n" +
            "   from staff s, progetto p, progetto_staff ps, calendario_staff cs\n" +
            "   where s.id = ps.id_staff\n" +
            "   and s.id =  cs.id_staff\n" +
            "   and p.id = :idProgetto\n" +
            "   and s.id not in (select id_staff from progetto_staff where id_staff = s.id and id_progetto = :idProgetto) ", nativeQuery=true)
    List<Staff> findStaffNonAssociati(@Param("idProgetto") Integer idProgetto);

    @Query(value=" select distinct s.*, cs.id_calendario, (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia, (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello, (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto, (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta \n" +
            "   from staff s, progetto p, progetto_staff ps, calendario_staff cs\n" +
            "   where s.id = ps.id_staff\n" +
            "   and s.id =  cs.id_staff\n" +
            "   and p.id = :idProgetto\n" +
            "   and s.id not in (select id_staff from progetto_staff where id_staff = s.id and id_progetto = :idProgetto)\n" +
            "   and if(:nome is not null, s.nome=:nome, 1=1)\n" +
            "   and if(:cognome is not null, s.cognome=:cognome, 1=1)\n" +
            "   and if(:email is not null, s.email=:email, 1=1) ", nativeQuery=true)
    List<Staff> findRicercaStaffNonAssociati(@Param("idProgetto") Integer idProgetto, @Param("nome") String nome, @Param("cognome") String cognome, @Param("email") String email);

    @Query(value=" select distinct s.*, cs.id_calendario, (ifnull ((select id_tipologia from tipologia_staff where id_staff = s.id),null)) id_tipologia, (ifnull ((select id_livello from livello_staff where id_staff = s.id),null)) id_livello, (ifnull ((select id_tipo_contratto from tipo_contratto_staff where id_staff = s.id),null)) id_tipo_contratto, (ifnull ((select id_facolta from facolta_staff where id_staff = s.id),null)) id_facolta \n" +
            "   from staff s, progetto p, progetto_staff ps, calendario_staff cs\n" +
            "   where s.id = ps.id_staff\n" +
            "   and s.id =  cs.id_staff\n" +
            "   and p.id = :idProgetto\n" +
            "   and s.id in (select id_staff from progetto_staff where id_staff = s.id and id_progetto = :idProgetto) ", nativeQuery=true)
    List<Staff> findStaffAssociati(@Param("idProgetto") Integer idProgetto);
}