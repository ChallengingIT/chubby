/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.staff.FileStaff;
import it.innotek.wehub.entity.staff.FileStaffId;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.FileStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileStaffService {

    @Autowired
    private FileStaffRepository repo;

    public List<FileStaff> listAll(){
        return (List<FileStaff>) repo.findAll();
    }

    public void save(FileStaff file) {
        repo.save(file);
    }

    public FileStaff get(FileStaffId id) throws ElementoNonTrovatoException {
        Optional<FileStaff> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato file con id" + id);
    }

    public void delete(FileStaffId id) throws ElementoNonTrovatoException {
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
