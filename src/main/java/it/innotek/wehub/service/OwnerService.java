/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Owner;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository repo;

    public List<Owner> listAll(){
        return (List<Owner>) repo.findAll();
    }

    public void save(Owner owner) {
        repo.save(owner);
    }

    public Owner get(Integer id) throws ElementoNonTrovatoException {
        Optional<Owner> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato owner con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato owner con id" + id);

        }
        repo.deleteById(id);
    }
}
