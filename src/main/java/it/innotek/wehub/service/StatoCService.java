/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.StatoC;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.StatoCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatoCService {

    @Autowired
    private StatoCRepository repo;

    public List<StatoC> listAll(){
        return (List<StatoC>) repo.findAll();
    }

    public List<StatoC> listAllOrdered(){
        return repo.findAllOrdered();
    }

    public void save(StatoC statoC) {
        repo.save(statoC);
    }

    public StatoC get(Integer id) throws ElementoNonTrovatoException {
        Optional<StatoC> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException("Non trovato stato con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException("Non trovato stato con id" + id);
        }
        repo.deleteById(id);
    }
}