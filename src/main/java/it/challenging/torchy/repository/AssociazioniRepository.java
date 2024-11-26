/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.AssociazioneCandidatoNeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AssociazioniRepository extends JpaRepository<AssociazioneCandidatoNeed, Integer> {

    @Query("select count(a) from AssociazioneCandidatoNeed a where a.need.id = ?1")
    long countByNeed_Id(Integer id);

    void deleteByCandidato_Id(Integer IdCandidato);

    List<AssociazioneCandidatoNeed> findByNeed_IdAndCandidato_IdAndStato_IdAndDataModifica(Integer id, Integer id1, Integer id2, Date dataModifica);

    List<AssociazioneCandidatoNeed> findByCandidato_Id(Integer id);

    Page<AssociazioneCandidatoNeed> findByNeed_IdOrderByDataModificaDesc(Integer idNeed, Pageable p);

    List<AssociazioneCandidatoNeed> findByNeed_Id(Integer idNeed);

    @Query(value= """
         SELECT acn.*, ca.id_candidato, na.id_need, sa.id_stato, oa.id_owner
              FROM associazione_candidato_need acn, candidato_associazione ca, owner_associazione oa, need_associazione na, stato_associazione sa, need_cliente nc
              where acn.id = ca.id_associazione
              and acn.id = na.id_associazione
              and acn.id = oa.id_associazione
              and acn.id = sa.id_associazione
              and na.id_need = nc.id_need
              and ca.id_candidato = ?1
              and if(?3 is not null, sa.id_stato = ?3, 1=1)
              and if(?2 is not null, nc.id_cliente = ?2, 1=1)
              and if(?4 is not null, acn.data_modifica <= ?4, 1=1)
              order by acn.data_modifica
        """, nativeQuery=true)
    List<AssociazioneCandidatoNeed> ricercaByCandidato_IdAndCliente_IdAndStato_IdAndDataModifica(Integer idCandidato, Integer idCliente, Integer idStato, Date dataModifica);

    @Query(value= """
         select count(*)
           from associazione_candidato_need acn
           join need_associazione na on (acn.id = na.id_associazione)
           join need n on (n.id = na.id_need)
           join stato_associazione sa on (sa.id_associazione = acn.id)
           where id_need = ?1
           and sa.id_stato = ?2
        """, nativeQuery=true)
    Integer countAssociazioniByStato(Integer idNeed, Integer stato);
}
