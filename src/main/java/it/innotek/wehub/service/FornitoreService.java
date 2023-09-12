/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Fornitore;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.FornitoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornitoreService {

    @Autowired
    private FornitoreRepository repo;

    public List<Fornitore> listAll(){
        return (List<Fornitore>) repo.findAll();
    }

    public List<Fornitore> listRicerca(
        String denominazione,
        String referente,
        String email
    ){
        return repo.findRicerca(
            denominazione,
            referente,
            email
        );
    }

    public Integer controllaDenominazione(String denominazione){
        return repo.checkDenominazione(denominazione);
    }

    public void save(Fornitore fornitore) {
        repo.save(fornitore);
    }

    public Fornitore get(Integer id) throws ElementoNonTrovatoException {
        Optional<Fornitore> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato fornitore con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato fornitore con id" + id);

        }
        repo.deleteById(id);
    }
}
