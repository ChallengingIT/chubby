/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.FileCandidato;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.FileCandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileCandidatoService {

    @Autowired
    private FileCandidatoRepository repo;

    public List<FileCandidato> listAll(){
        return (List<FileCandidato>) repo.findAll();
    }

    public void save(FileCandidato file) {
        repo.save(file);
    }

    public FileCandidato get(Integer id) throws ElementoNonTrovatoException {
        Optional<FileCandidato> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato file con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato file  con id" + id);

        }
        repo.deleteById(id);
    }

    public void deleteFileVecchi(
        String listId,
        Integer tipologia
    ){
         repo.removeFileDoppione(
             listId,
             tipologia
         );
    }

}
