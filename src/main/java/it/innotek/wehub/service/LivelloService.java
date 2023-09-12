/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.LivelloScolastico;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.LivelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivelloService {

    @Autowired
    private LivelloRepository repo;

    public List<LivelloScolastico> listAll(){
        return (List<LivelloScolastico>) repo.findAll();
    }

    public void save(LivelloScolastico livello) {
        repo.save(livello);
    }

    public LivelloScolastico get(Integer id) throws ElementoNonTrovatoException {
        Optional<LivelloScolastico> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato livello scolastico con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato livello scolastico  con id" + id);

        }
        repo.deleteById(id);
    }
}
