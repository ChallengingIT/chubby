/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT c.* \n" +
            "             FROM cliente c\n" +
            "             where if(:denominazione is not null, c.denominazione like %:denominazione%, 1=1)\n" +
            "             and if(:citta is not null, c.citta=:citta, 1=1) ", nativeQuery=true)
    List<Cliente> findRicerca(@Param("denominazione") String denominazione, @Param("citta") String citta);


    @Query(value=" SELECT if(count(*)=1,1,0)\n" +
            "FROM cliente c\n" +
            "where denominazione = :denominazione ", nativeQuery=true)
    Integer checkDenominazione(@Param("denominazione") String denominazione);

    @Query(value=" SELECT if(count(*)=1,1,0)\n" +
            "FROM cliente c\n" +
            "where email = :email ", nativeQuery=true)
    Integer checkEmail(@Param("email") String email);

    @Query(value=" SELECT c.*, co.id_owner, \n" +
        "           (ifnull ((select id_prospection from cliente_prospection where id_cliente = c.id),null)) id_prospection, " +
        "           (ifnull ((select id_qm from cliente_qm where id_cliente = c.id),null)) id_qm " +
        "             FROM cliente c, cliente_owner co \n" +
        "             where c.id = co.id_cliente " +
        "             and if(:status is not null, c.status = :status, 1=1)\n " +
        "             and if(:owner is not null, co.id_owner = :owner, 1=1)\n " +
        "             and if(:tipologia is not null, c.tipologia = :tipologia, 1=1)\n " +
        "             and if(:denominazione is not null, c.denominazione like %:denominazione%, 1=1) " ,nativeQuery=true)
    List<Cliente> findRicercaAzienda(
        @Param("status") Integer status,
        @Param("owner") Integer owner,
        @Param("tipologia") String tipologia,
        @Param("denominazione") String denominazione);
}