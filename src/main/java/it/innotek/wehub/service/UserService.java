/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.User;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public List<User> listAll(){
        return (List<User>) repo.findAll();
    }

    public void save(User giorno) {
        repo.save(giorno);
    }

    public User get(String id) throws ElementoNonTrovatoException {
        Optional<User> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException("Non trovato user con id" + id);
    }

    public void delete(String username) throws ElementoNonTrovatoException {
        Long count = repo.countByUsername(username);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException("Non trovato giorno con username" + username);
        }
        repo.deleteById(username);
    }
}
