package com.ephec.padel.membre.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.model.TypeMembre;

public interface MembreRepository extends JpaRepository<Membre, Long> {

    Optional<Membre> findByMatricule(String matricule);

    long countByType(TypeMembre type);
}