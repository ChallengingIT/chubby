/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.service;

import it.innotek.wehub.entity.KeyPeople;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.KeyPeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeyPeopleService {

  @Autowired
  private KeyPeopleRepository repo;

  public List<KeyPeople> listAll() {
    return (List<KeyPeople>) repo.findAll();
  }

  public void save(KeyPeople keyPeople){
    repo.save(keyPeople);
  }

  public KeyPeople get(Integer id) throws ElementoNonTrovatoException {
    Optional<KeyPeople> result =  repo.findById(id);

    if(result.isPresent()){
      return result.get();
    }

    throw new ElementoNonTrovatoException("Non trovato contatto con id: " + id);
  }

  public void delete(Integer id) throws ElementoNonTrovatoException {
    Long count = repo.countById(id);

    if ((null == count) || count == 0){
      throw new ElementoNonTrovatoException("Non trovato contatto con id" + id);

    }
    repo.deleteById(id);
  }

  public List<KeyPeople> listRicercaKeyPeople(
      String status,
      Integer owner,
      Integer azienda
  ){
    return repo.findRicercaKeyPeople(
        status,
        owner,
        azienda
    );
  }

  public Integer controllaNome(String nome) {
    return repo.checkNome(nome);
  }

  public void deleteByCliente(Integer idCliente){
    repo.deleteByIdCliente(idCliente);
  }
}
