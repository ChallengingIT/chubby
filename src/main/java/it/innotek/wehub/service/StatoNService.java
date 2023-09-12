/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.StatoN;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.StatoNRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatoNService {

    @Autowired private StatoNRepository repo;

    public List<StatoN> listAll(){
        return (List<StatoN>) repo.findAll();
    }

    public void save(StatoN stato) {
        repo.save(stato);
    }

    public StatoN get(Integer id) throws ElementoNonTrovatoException {
        Optional<StatoN> result =  repo.findById(id);

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
