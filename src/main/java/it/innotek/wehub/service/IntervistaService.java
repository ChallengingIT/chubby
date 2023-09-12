/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Intervista;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.IntervistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IntervistaService {

    @Autowired
    private IntervistaRepository repo;

    public List<Intervista> listAll(){
        return (List<Intervista>) repo.findAll();
    }

    public List<Intervista> listRicerca(
        Integer idStato,
        Integer idOwner,
        Date dataColloquio,
        Integer idCandidato
    ){
        return repo.findRicerca(
            idStato,
            idOwner,
            dataColloquio,
            idCandidato
        );
    }

    public List<Intervista> listAllByCandidato(Integer idCandidato){
        return repo.findAllByCandidati(idCandidato);
    }

    public Intervista  getUltimaByIdCandidato(Integer idCandidato){
        return repo.findByCandidato(idCandidato);
    }

    public List<Intervista> listIntervisteImminenti(){
        return repo.findIntervisteImminenti();
    }

    public List<Intervista> getIntervisteSettimanaCur(){
        return repo.findIntervisteSettimanaCur();
    }
    public List<Intervista> getIntervisteSettimanaCurMeno(){
        return repo.findIntervisteSettimanaCurMeno();
    }
    public List<Intervista> getIntervisteSettimanaCurPiu(){
        return repo.findIntervisteSettimanaCurPiu();
    }

    public void save(Intervista intervista) {
        repo.save(intervista);
    }

    public Intervista get(Integer id) throws ElementoNonTrovatoException {
        Optional<Intervista> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovata intervista con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovata intervista  con id" + id);

        }
        repo.deleteById(id);
    }
}
