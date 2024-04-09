/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Fornitore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornitoreRepository extends JpaRepository<Fornitore, Integer> {

    @Query(value= """
         SELECT f.*
         FROM fornitore f
         where if(?1 is not null, f.denominazione like %?1%, 1=1)
         and if(?2 is not null, f.referente like %?2%, 1=1)
         and if(?3 is not null, f.email like %?3%, 1=1)
        """, nativeQuery=true)
    List<Fornitore> ricercaByDenominazioneAndReferenteAndEmail(String denominazione, String referente, String email);

    List<Fornitore> findByDenominazione(String denominazione);

    List<Fornitore> findAllByDenominazioneContainingIgnoreCase(String term);


}