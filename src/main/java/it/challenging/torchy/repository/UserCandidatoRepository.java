/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.repository;

import it.challenging.torchy.entity.UserCandidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCandidatoRepository extends JpaRepository<UserCandidato, String> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<UserCandidato> findByUsername(String username);

    Optional<UserCandidato> findByEmail(String email);

}