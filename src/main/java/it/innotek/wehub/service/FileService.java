/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.File;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository repo;

    public List<File> listAll(){
        return (List<File>) repo.findAll();
    }

    public void save(File file) {
        repo.save(file);
    }

    public File get(Integer id) throws ElementoNonTrovatoException {
        Optional<File> result =  repo.findById(id);

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
}
