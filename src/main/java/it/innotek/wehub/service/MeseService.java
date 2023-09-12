/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.timesheet.Mese;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.MeseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeseService {

    @Autowired
    private MeseRepository repo;

    public List<Mese> listAll(){
        return (List<Mese>) repo.findAll();
    }

    public void save(Mese facolta) {
        repo.save(facolta);
    }

    public Mese get(Integer id) throws ElementoNonTrovatoException {
        Optional<Mese> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato mese con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato mese con id" + id);

        }
        repo.deleteById(id);
    }
}
