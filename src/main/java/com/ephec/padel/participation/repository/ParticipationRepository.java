package com.ephec.padel.participation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ephec.padel.participation.model.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    long countByRencontre_Id(Long rencontreId);

    List<Participation> findByRencontre_IdOrderByDateInscriptionAsc(Long rencontreId);

    boolean existsByRencontre_IdAndMembre_Id(Long rencontreId, Long membreId);
}