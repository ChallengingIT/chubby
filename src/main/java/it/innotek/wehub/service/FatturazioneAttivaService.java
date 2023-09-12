/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.FatturazioneAttiva;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.FatturazioneAttivaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FatturazioneAttivaService {

    @Autowired
    private FatturazioneAttivaRepository repo;

    public List<FatturazioneAttiva> listAll(){
        return (List<FatturazioneAttiva>) repo.findAll();
    }

    public List<FatturazioneAttiva> listRicerca(
        Integer cliente,
        Integer stato
    ){
        return repo.findRicerca(
            cliente,
            stato
        );
    }

    public void save(FatturazioneAttiva fornitore) {
        repo.save(fornitore);
    }

    public FatturazioneAttiva get(Integer id) throws ElementoNonTrovatoException {
        Optional<FatturazioneAttiva> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovata fattura con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovata fattura con id" + id);

        }
        repo.deleteById(id);
    }
}
