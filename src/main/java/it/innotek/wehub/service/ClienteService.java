/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Cliente;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.ClienteRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class ClienteService {

    @Autowired private ClienteRepository repo;

    public List<Cliente> listAll(){
        List<Cliente> toReturn = (List<Cliente>) repo.findAll();
        toReturn.sort(Comparator.comparing(Cliente::getDenominazione));
        return toReturn;
    }

    public List<Cliente> listRicerca(
        String denominazione,
        String citta
    ){
        return repo.findRicerca(
            denominazione,
            citta
        );
    }

    public List<Cliente> listRicercaAziende(
        Integer status,
        Integer owner,
        String tipologia,
        String denominazione
    ){
        return repo.findRicercaAzienda(
            status,
            owner,
            tipologia,
            denominazione
        );
    }

    public Integer controllaDenominazione(String denominazione){
        return repo.checkDenominazione(denominazione);
    }

    public void save(Cliente cliente) {repo.save(cliente);}

    public Optional<Cliente> getById(Integer id) {return repo.findById(id);}

    public Cliente get(Integer id) throws ElementoNonTrovatoException {
        Optional<Cliente> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato cliente con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato cliente con id" + id);

        }
        repo.deleteById(id);
    }

    public Integer controllaEmail(String email) {
        return repo.checkEmail(email);
    }
}
