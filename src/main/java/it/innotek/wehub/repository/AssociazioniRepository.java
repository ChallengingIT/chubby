/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.AssociazioneCandidatoNeed;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface AssociazioniRepository extends CrudRepository<AssociazioneCandidatoNeed, Integer> {

    Long countById(Integer id);

    @Query(value=" SELECT acn.*, ca.id_candidato, na.id_need, sa.id_stato \n" +
            "      FROM associazione_candidato_need acn, candidato_associazione ca, need_associazione na, stato_associazione sa\n" +
            "      where acn.id = ca.id_associazione\n" +
            "      and acn.id = na.id_associazione\n" +
            "      and acn.id = sa.id_associazione\n" +
            "      and ca.id_candidato = :idCandidato\n" +
            "      order by acn.data_modifica ", nativeQuery=true)
    List<AssociazioneCandidatoNeed> findAllByCandidato(@Param("idCandidato") Integer idCandidato);

    @Query(value=" SELECT acn.*, ca.id_candidato, na.id_need, sa.id_stato \n" +
            "      FROM associazione_candidato_need acn, candidato_associazione ca, need_associazione na, stato_associazione sa\n" +
            "      where acn.id = ca.id_associazione\n" +
            "      and acn.id = na.id_associazione\n" +
            "      and acn.id = sa.id_associazione\n" +
            "      and na.id_need = :idNeed\n" +
            "      order by acn.data_modifica ", nativeQuery=true)
    List<AssociazioneCandidatoNeed> findAllByNeed(@Param("idNeed") Integer idNeed);

    @Query(value=" SELECT acn.*, ca.id_candidato, na.id_need, sa.id_stato \n" +
            "      FROM associazione_candidato_need acn, candidato_associazione ca, need_associazione na, stato_associazione sa, need_cliente nc\n" +
            "      where acn.id = ca.id_associazione\n" +
            "      and acn.id = na.id_associazione\n" +
            "      and acn.id = sa.id_associazione\n" +
            "      and na.id_need = nc.id_need\n" +
            "      and ca.id_candidato = :idCandidato\n" +
            "      and if(:idStato is not null, sa.id_stato =:idStato, 1=1) " +
            "      and if(:idCliente is not null, nc.id_cliente =:idCliente, 1=1) " +
            "      and if(:dataModifica is not null, acn.data_modifica<=:dataModifica, 1=1)" +
            "      order by acn.data_modifica ", nativeQuery=true)
    List<AssociazioneCandidatoNeed> findRicerca(@Param("idCandidato") Integer idCandidato, @Param("idCliente") Integer idCliente, @Param("idStato") Integer idStato, @Param("dataModifica") Date dataModifica);



    @Query(value=" SELECT acn.*, ca.id_candidato, na.id_need, sa.id_stato \n" +
            "      FROM associazione_candidato_need acn, candidato_associazione ca, need_associazione na, stato_associazione sa\n" +
            "      where acn.id = ca.id_associazione\n" +
            "      and acn.id = na.id_associazione\n" +
            "      and acn.id = sa.id_associazione\n" +
            "      and ca.id_candidato = :idCandidato\n"+
            "      and na.id_need = :idNeed\n"+
            "      and sa.id_stato = :idStato\n"+
            "      and acn.data_modifica = :dataModifica\n", nativeQuery=true)
    List<AssociazioneCandidatoNeed> findAssociazione(@Param("idNeed") Integer idNeed,@Param("idCandidato") Integer idCandidato, @Param("idStato") Integer idStato, @Param("dataModifica") Date dataModifica);
}
