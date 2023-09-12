/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.Need;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.NeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NeedService {

    @Autowired
    private NeedRepository repo;

    public List<Need> listAll(){
        return (List<Need>) repo.findAll();
    }

    public List<Need> getNeedSettimanaCur(){
        return repo.findNeedSettimanaCur();
    }
    public List<Need> getNeedSettimanaCurMeno(){
        return repo.findNeedSettimanaCurMeno();
    }
    public List<Need> getNeedSettimanaCurPiu(){
        return repo.findNeedSettimanaCurPiu();
    }

    public Integer getWeek(){
        return repo.findWeek();
    }

    public Integer getWeekPre(){
        return repo.findWeekPre();
    }

    public Integer getWeekSuc(){
        return repo.findWeekSuc();
    }

    public List<Need> listRicerca(
        Integer idCliente,
        Integer idStato,
        Integer priorita,
        Integer idTipologia,
        String week,
        Integer idOwner
    ){
        return repo.findRicerca(
            idCliente,
            idStato,
            priorita,
            idTipologia,
            week,
            idOwner
        );
    }

    public List<Need> listNeedAssociabiliCandidato(Integer idCandidato){
        return repo.findNeedAssociabiliCandidato(idCandidato);
    }

    public Integer getNeedAssociati(){
        return repo.findNeedAssociati();
    }

    public List<Need> getNeedOrdinati(){
        return repo.findNeedOrdinati();
    }

    public List<Need> listAllByCLiente(Integer idCliente){
        return repo.findAllByClienti(idCliente);
    }

    public void save(Need need) {
        repo.save(need);
    }

    public Need get(Integer id) throws ElementoNonTrovatoException {
        Optional<Need> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovata need con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovata need con id" + id);

        }
        repo.deleteById(id);
    }
}
