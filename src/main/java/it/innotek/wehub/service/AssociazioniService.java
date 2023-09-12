/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.AssociazioneCandidatoNeed;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.AssociazioniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AssociazioniService {

    @Autowired
    private AssociazioniRepository repo;

    public List<AssociazioneCandidatoNeed> listAll(){
        return (List<AssociazioneCandidatoNeed>) repo.findAll();
    }

    public List<AssociazioneCandidatoNeed> listRicerca(
        Integer idCandidato,
        Integer idCliente,
        Integer idStato,
        Date dataModifica
    ){
        return repo.findRicerca(
            idCandidato,
            idCliente,
            idStato,
            dataModifica
        );
    }

    public List<AssociazioneCandidatoNeed> listAllByCandidato(Integer idCandidato){
        return (List<AssociazioneCandidatoNeed>) repo.findAllByCandidato(idCandidato);
    }
    public List<AssociazioneCandidatoNeed> getAssociazione(
        Integer idNeed,
        Integer idCandidato,
        Integer idStato,
        Date dataModifica
    ){
        return repo.findAssociazione(
            idNeed,
            idCandidato,
            idStato,
            dataModifica
        );
    }

    public List<AssociazioneCandidatoNeed> listAllByNeed(Integer idNeed){
        return (List<AssociazioneCandidatoNeed>) repo.findAllByNeed(idNeed);
    }

    public void save(AssociazioneCandidatoNeed utente) {
        repo.save(utente);
    }

    public AssociazioneCandidatoNeed get(Integer id) throws ElementoNonTrovatoException {
        Optional<AssociazioneCandidatoNeed> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovata associazione con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovata associazione con id" + id);

        }
        repo.deleteById(id);
    }
}
