/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.AssociazioneCandidatoNeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AssociazioniRepository extends JpaRepository<AssociazioneCandidatoNeed, Integer> {

    void deleteByCandidato_Id(Integer IdCandidato);

    List<AssociazioneCandidatoNeed> findByNeed_IdAndCandidato_IdAndStato_IdAndDataModifica(Integer id, Integer id1, Integer id2, Date dataModifica);

    List<AssociazioneCandidatoNeed> findByCandidato_Id(Integer id);

    List<AssociazioneCandidatoNeed> findByNeed_IdOrderByDataModificaDesc(Integer idNeed);

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
}
