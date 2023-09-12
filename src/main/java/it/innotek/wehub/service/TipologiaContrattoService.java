/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.TipologiaContratto;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.TipologiaContrattoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipologiaContrattoService {

    @Autowired
    private TipologiaContrattoRepository repo;

    public List<TipologiaContratto> listAll(){
        return (List<TipologiaContratto>) repo.findAll();
    }

    public void save(TipologiaContratto tipologia) {
        repo.save(tipologia);
    }

    public TipologiaContratto get(Integer id) throws ElementoNonTrovatoException {
        Optional<TipologiaContratto> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException("Non trovata tipologia con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException("Non trovata tipologia con id" + id);
        }
        repo.deleteById(id);
    }
}
