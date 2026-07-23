package com.ephec.padel.fermeture.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ephec.padel.fermeture.model.Fermeture;

public interface FermetureRepository extends JpaRepository<Fermeture, Long> {

    boolean existsByDateFermetureAndSite_Id(LocalDate date, Long siteId);

    boolean existsByDateFermetureAndSiteIsNull(LocalDate date);
}