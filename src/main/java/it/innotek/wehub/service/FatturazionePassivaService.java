/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.FatturazionePassiva;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.FatturazionePassivaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FatturazionePassivaService {

    @Autowired
    private FatturazionePassivaRepository repo;

    public List<FatturazionePassiva> listAll(){
        return (List<FatturazionePassiva>) repo.findAll();
    }

    public List<FatturazionePassiva> getFattureDaPagare(LocalDate date){
        return repo.findFattureDaPagare(date);
    }

    public List<FatturazionePassiva> listRicerca(
        Integer fornitore,
        Integer stato
    ){
        return repo.findRicerca(
            fornitore,
            stato
        );
    }

    public void save(FatturazionePassiva fornitore) {
        repo.save(fornitore);
    }

    public FatturazionePassiva get(Integer id) throws ElementoNonTrovatoException {
        Optional<FatturazionePassiva> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato fornitore con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato fornitore con id" + id);

        }
        repo.deleteById(id);
    }
}
