/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Authority;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository repo;

    public List<Authority> listAll(){
        return (List<Authority>) repo.findAll();
    }

    public void save(Authority giorno) {
        repo.save(giorno);
    }

    public Authority get(Integer id) throws ElementoNonTrovatoException {
        Optional<Authority> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException("Non trovata authority con id" + id);
    }

    public void delete(Integer auth) throws ElementoNonTrovatoException {
        Long count = repo.countById(auth);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato giorno con auth" + auth);
        }
        repo.deleteById(auth);
    }
}
