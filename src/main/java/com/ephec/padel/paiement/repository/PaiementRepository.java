package com.ephec.padel.paiement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ephec.padel.paiement.model.Paiement;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByParticipation_Rencontre_Terrain_Site_Id(Long siteId);
}