/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.timesheet.Giorno;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.GiornoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GiornoService {

    @Autowired
    private GiornoRepository repo;

    public List<Giorno> listAll(){
        return (List<Giorno>) repo.findAll();
    }

    public void save(Giorno giorno) {
        repo.save(giorno);
    }

    public List<Giorno> listAllByIdProgetto(Integer id){
        return (List<Giorno>) repo.findAllByIdProgetto(id);
    }

    public void deleteByIdProgetto(Integer id){ repo.deleteByIdProgetto(id);}

    public void deleteMeseByIdGiorno(Integer id){ repo.deleteMeseByIdProgetto(id);}

    public Giorno get(Integer id) throws ElementoNonTrovatoException {
        Optional<Giorno> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato giorno con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato giorno con id" + id);

        }
        repo.deleteById(id);
    }
}
