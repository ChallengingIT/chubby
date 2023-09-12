/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Candidato;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository repo;

    public List<Candidato> listAll(){
        return (List<Candidato>) repo.findAll();
    }

    public List<Candidato> listRicerca(
        String nome,
        String cognome,
        String email,
        Integer idTipologia,
        Integer idStato,
        Integer idTipo
    ){
        return repo.findRicerca(
            nome,
            cognome,
            email,
            idTipologia,
            idStato,
            idTipo
        );
    }

    public List<Candidato> listAllByNeed(Integer idNeed){
        return repo.findAllByNeed(idNeed);
    }

    public Integer controllaEmail(String email){
        return repo.checkEmail(email);
    }

    public Integer getAssociatiFornitori(Integer idFornitore){
        return repo.findAssociatiFornitori(idFornitore);
    }

    public List<Candidato> getCandidatiNonAssociati(Integer idNeed){
        return repo.findCandidatiNonAssociati(idNeed);
    }

    public List<Candidato> getRicercaCandidatiNonAssociati(
        Integer idNeed,
        String nome,
        String cognome,
        Integer idTipologia,
        Integer idTipo,
        Integer anniMinimi,
        Integer anniMassimi
    ){
        return repo.findRicercaCandidatiNonAssociati(
            idNeed,
            nome,
            cognome,
            idTipologia,
            idTipo,
            anniMinimi,
            anniMassimi
        );
    }

    public List<Candidato> getCandidatiAssociati(Integer idNeed){
        return repo.findCandidatiAssociati(idNeed);
    }

    public Integer getUltimoId(){
        return repo.findUltimoId();
    }

    public Integer getUltimoIdIntervista(Integer idCandidato){
        return repo.findUltimoIdIntervista(idCandidato);
    }

    public void save(Candidato utente) {
        repo.save(utente);
    }

    public Candidato get(Integer id) throws ElementoNonTrovatoException {
        Optional<Candidato> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato candidato con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato candidato con id" + id);

        }
        repo.deleteById(id);
    }
}
