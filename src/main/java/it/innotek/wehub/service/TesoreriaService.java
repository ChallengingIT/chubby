/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Tesoreria;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.TesoreriaRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class TesoreriaService {

    @Autowired private TesoreriaRepository repo;

    public List<Tesoreria> listAll(){
        return (List<Tesoreria>) repo.findAll();
    }

    public void save(Tesoreria tesoreria) {
        repo.save(tesoreria);
    }

    public Tesoreria get(int id) throws ElementoNonTrovatoException {
        Optional<Tesoreria> result =  repo.findById(id);

        if(result.isPresent()){
          return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato staff con id" + id);
    }

    public Tesoreria getSelected(
        String mese,
        int anno
    ) {
        return repo.findSelected(
            mese,
            anno
        );
    }

    public Integer getMaxId() {
        return repo.findMaxId();
    }
}