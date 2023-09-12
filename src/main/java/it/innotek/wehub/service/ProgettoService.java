/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.timesheet.Progetto;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.ProgettoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgettoService {

    @Autowired
    private ProgettoRepository repo;

    public List<Progetto> findAll(){
        return (List<Progetto>) repo.findAll();
    }

    public List<Progetto> listAll(){
        return repo.listAll();
    }

    public List<Progetto> listRicerca(
        String denominazione,
        Integer idCliente
    ){
        return repo.findRicerca(
            denominazione,
            idCliente
        );
    }

    public Integer getOreEffettive(
        Integer idProgetto,
        Integer idStaff
    ){
        Integer oreEffettive = repo.getOreEffettive(
            idProgetto,
            idStaff
        );

        return (null != oreEffettive) ? oreEffettive : 0;
    }

    public void save(Progetto progetto) {
        repo.save(progetto);
    }

    public List<Progetto> getByIdStaff(Integer idStaff){
        return repo.findByIdStaff(idStaff);
    }

    public Progetto get(Integer id) throws ElementoNonTrovatoException {
        Optional<Progetto> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato progetto con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato progetto con id" + id);

        }
        repo.deleteById(id);
    }
}