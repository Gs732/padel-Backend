package com.ephec.padel.horaire.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ephec.padel.horaire.model.Horaire;

public interface HoraireRepository extends JpaRepository<Horaire, Long> {

    Optional<Horaire> findBySite_IdAndAnnee(Long siteId, int annee);
}