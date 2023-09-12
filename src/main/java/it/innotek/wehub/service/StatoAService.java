/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.StatoA;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.StatoARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatoAService {

    @Autowired
    private StatoARepository repo;

    public List<StatoA> listAll(){
        return (List<StatoA>) repo.findAll();
    }

    public void save(StatoA statoC) {
        repo.save(statoC);
    }

    public StatoA get(Integer id) throws ElementoNonTrovatoException {
        Optional<StatoA> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato stato con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato stato con id" + id);

        }
        repo.deleteById(id);
    }
}
