/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Attivita;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.AttivitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttivitaService {

    @Autowired
    private AttivitaRepository repo;

    public List<Attivita> listAll() {
      return (List<Attivita>) repo.findAll();
    }

    public void save(Attivita attivita){
      repo.save(attivita);
    }

    public Attivita get(Integer id) throws ElementoNonTrovatoException {
        Optional<Attivita> result =  repo.findById(id);

        if(result.isPresent()){
          return result.get();
        }

        throw new ElementoNonTrovatoException("Non trovata attività con id: " + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
          throw new ElementoNonTrovatoException("Non trovata attività con id:" + id);

        }
        repo.deleteById(id);
    }

    public List<Attivita> ricerca(
        Integer idTipologia,
        Integer idOwner
    ){
        return repo.findRicerca(
            idTipologia,
            idOwner
        );
    }
}