/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.staff.Staff;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffRepository repo;

    public List<Staff> listAll(){
        return (List<Staff>) repo.findAll();
    }

    public List<Staff> listRicerca(
        String nome,
        String cognome,
        String email
    ){
        return repo.findRicerca(
            nome,
            cognome,
            email
        );
    }

    public List<Staff> listByCalendario(
        Integer anno,
        Integer mese
    ){
        return repo.findByCalendario(
            anno,
            mese
        );
    }

    public Staff getByUsername(String username){
        return repo.findByUsername(username);
    }

    public Staff getStaffByIdProgetto(Integer idProgetto){
        return repo.findStaffByIdProgetto(idProgetto);
    }

    public Integer controllaEmail(String email){
        return repo.checkEmail(email);
    }

    public List<Staff> getStaffNonAssociati(Integer idProgetto){
        return repo.findStaffNonAssociati(idProgetto);
    }

    public List<Staff> getRicercaStaffNonAssociati(
        Integer idProgetto,
        String nome,
        String cognome,
        String email
    ){
        return repo.findRicercaStaffNonAssociati(
            idProgetto,
            nome,
            cognome,
            email
        );
    }

    public List<Staff> getStaffAssociati(Integer idProgetto){
        return repo.findStaffAssociati(idProgetto);
    }


    public void save(Staff utente) {
        repo.save(utente);
    }

    public Staff get(Integer id) throws ElementoNonTrovatoException {
        Optional<Staff> result =  repo.findById(id);

        if(result.isPresent()){
            return result.get();
        }

        throw new ElementoNonTrovatoException(" Non trovato staff con id" + id);
    }

    public void delete(Integer id) throws ElementoNonTrovatoException {
        Long count = repo.countById(id);

        if ((null == count) || count == 0){
            throw new ElementoNonTrovatoException(" Non trovato staff con id" + id);

        }
        repo.deleteById(id);
    }
}
