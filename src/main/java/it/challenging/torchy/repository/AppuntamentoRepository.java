package it.challenging.torchy.repository;

import it.challenging.torchy.entity.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Integer> {

}