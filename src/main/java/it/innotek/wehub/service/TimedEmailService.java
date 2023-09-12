/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.TimedEmail;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.TimedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimedEmailService {

    @Autowired
    private TimedEmailRepository repo;

    public List<TimedEmail> listAll(){
        return (List<TimedEmail>) repo.findAll();
    }

    public void save(TimedEmail email) {
        repo.save(email);
    }

    public TimedEmail get(Integer id) throws ElementoNonTrovatoException {
        Optional<TimedEmail> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException("Non trovata email con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException("Non trovata email  con id" + id);
        }
        repo.deleteById(id);
    }

    public Long getUltimoId(){
        return repo.findUltimoId();
    }

    public void updateEmailTemporizzata(
        Long idEmail,
        Integer inviata
    ){
        repo.update(
            idEmail,
            inviata
        );
    }
}
