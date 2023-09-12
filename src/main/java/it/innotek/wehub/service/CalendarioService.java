/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.timesheet.Calendario;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.CalendarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalendarioService {

    @Autowired
    private CalendarioRepository repo;

    public List<Calendario> listAll(){
        return (List<Calendario>) repo.findAll();
    }

    public void save(Calendario livello) {
        repo.save(livello);
    }

    public Calendario get(Integer id) throws ElementoNonTrovatoException {
        Optional<Calendario> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException("Non trovato calendario con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato calendario con id" + id);

        }
        repo.deleteById(id);
    }
}