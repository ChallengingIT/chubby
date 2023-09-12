/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Province;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinceService {

    @Autowired
    private ProvinceRepository repo;

    public List<Province> listAll(){
        return (List<Province>) repo.findAll();
    }

    public void save(Province provincia) {
        repo.save(provincia);
    }

    public Province get(Integer id) throws ElementoNonTrovatoException {
        Optional<Province> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovata provincia con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovata provincia con id" + id);

        }
        repo.deleteById(id);
    }
}
