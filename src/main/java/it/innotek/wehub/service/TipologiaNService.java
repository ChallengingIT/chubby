/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.TipologiaN;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.TipologiaNRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipologiaNService {

    @Autowired
    private TipologiaNRepository repo;

    public List<TipologiaN> listAll(){
        return (List<TipologiaN>) repo.findAll();
    }

    public void save(TipologiaN tipologia) {
        repo.save(tipologia);
    }

    public TipologiaN get(Integer id) throws ElementoNonTrovatoException {
        Optional<TipologiaN> result =  repo.findById(id);

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
