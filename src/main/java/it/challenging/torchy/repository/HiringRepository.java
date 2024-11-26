/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Hiring;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HiringRepository extends JpaRepository<Hiring, Integer> {

    @Query(value= """
           select distinct h.*, th.id_tipo_servizio, sh.id_scheda_candidato
           from need n
              left join need_cliente ncc on ncc.id_need = n.id
              left join hiring h on ncc.id_cliente = h.id_cliente
              left join tipo_servizio_hiring th on th.id_hiring = h.id
              left join schede_candidato_hiring sh on h.id = sh.id_hiring
              left join need_cliente nc on n.id = nc.id_need
              left join need_keypeople nk on n.id = nk.id_need
              left join tipologia_need tn on n.id = tn.id_need
              left join need_owner non on n.id = non.id_need
              left join stato_need sn on n.id = sn.id_need
           where  n.id in (select id_need from need_candidato where id_need = n.id and id_candidato = ?1)
           and sh.id_candidato <> ?1
           and th.id_tipo_servizio = ?2
          """, nativeQuery=true)
    Hiring findHiringByNuovoStatoCandidato(Integer idCandidato, Integer idTipoServizio);

    Page<Hiring> findByIdCliente(Integer idCliente, Pageable p);

    @Query(value= """
           select distinct h.*, th.id_tipo_servizio, sh.id_scheda_candidato
           from  hiring h
              left join tipo_servizio_hiring th on th.id_hiring = h.id
              left join schede_candidato_hiring sh on h.id = sh.id_hiring
           where h.id_cliente = ?1
           and th.id_tipo_servizio = ?2
          """, nativeQuery=true)
    Page<Hiring> findAllByIdClienteAndTipoServizio_Id(Integer idCliente, Integer idTipoServizio, Pageable p);
    
    Hiring findByIdCliente(Integer idCliente);

    @Query(value= """
           select distinct h.*, th.id_tipo_servizio, sh.id_scheda_candidato
           from  hiring h
              left join tipo_servizio_hiring th on th.id_hiring = h.id
              left join schede_candidato_hiring sh on h.id = sh.id_hiring
           where th.id_tipo_servizio = ?1
          """, nativeQuery=true)
    Page<Hiring> findAllByTipoServizio_Id(Integer idTipoServizio, Pageable p);

    //@Transactional
    //void deleteAllByIdClienteAndTipoServizio_Id(Integer idCliente, Integer idTipoServizio);
    @Transactional
    void deleteAllByIdCliente(Integer idCliente);
}