/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Skill;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    private SkillRepository repo;

    public List<Skill> listAll(){
        return (List<Skill>) repo.findAll();
    }

    public void save(Skill skill) {
        repo.save(skill);
    }

    public Skill get(Integer id) throws ElementoNonTrovatoException {
        Optional<Skill> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovata skill con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovata skill  con id" + id);

        }
        repo.deleteById(id);
    }
}
