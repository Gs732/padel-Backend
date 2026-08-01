package com.ephec.padel.participation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephec.padel.exception.BadRequestException;
import com.ephec.padel.exception.NotFoundException;
import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.repository.MembreRepository;
import com.ephec.padel.participation.dto.ParticipationResponse;
import com.ephec.padel.participation.model.Participation;
import com.ephec.padel.participation.repository.ParticipationRepository;
import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.repository.RencontreRepository;

@Service
public class ParticipationService {

    private static final int MAX_JOUEURS = 4;

    private final ParticipationRepository participationRepository;
    private final RencontreRepository rencontreRepository;
    private final MembreRepository membreRepository;

    public ParticipationService(ParticipationRepository participationRepository,
                                RencontreRepository rencontreRepository,
                                MembreRepository membreRepository) {
        this.participationRepository = participationRepository;
        this.rencontreRepository = rencontreRepository;
        this.membreRepository = membreRepository;
    }

    @Transactional
    public ParticipationResponse rejoindre(Long rencontreId, Long membreId) {
        Rencontre rencontre = rencontreRepository.findById(rencontreId)
                .orElseThrow(() -> new NotFoundException("Rencontre introuvable : " + rencontreId));

        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new NotFoundException("Membre introuvable : " + membreId));

        // --- Règle 1 : le match ne doit pas être complet ---
        long nbJoueurs = participationRepository.countByRencontre_Id(rencontreId);
        if (nbJoueurs >= MAX_JOUEURS) {
            throw new BadRequestException("Ce match est complet (" + MAX_JOUEURS + " joueurs).");
        }

        // --- Règle 2 : le membre ne doit pas déjà participer ---
        if (participationRepository.existsByRencontre_IdAndMembre_Id(rencontreId, membreId)) {
            throw new BadRequestException(
                "Le membre " + membre.getMatricule() + " participe deja a ce match.");
        }

        // --- Règle 3 : solde à jour ---
        if (membre.getSolde() < 0) {
            throw new BadRequestException(
                "Le membre " + membre.getMatricule() + " a un solde du.");
        }

        // Créer la participation (non payée au départ)
        Participation participation = new Participation();
        participation.setRencontre(rencontre);
        participation.setMembre(membre);
        participation.setPaye(false);

        return toResponse(participationRepository.save(participation));
    }

    @Transactional(readOnly = true)
    public List<ParticipationResponse> getByRencontre(Long rencontreId) {
        return participationRepository
                .findByRencontre_IdOrderByDateInscriptionAsc(rencontreId).stream()
                .map(this::toResponse)
                .toList();
    }

    // --- Mapping entité -> DTO ---
    private ParticipationResponse toResponse(Participation p) {
        return new ParticipationResponse(
            p.getId(),
            p.getRencontre().getId(),
            p.getMembre().getId(),
            p.getMembre().getNom() + " " + p.getMembre().getPrenom(),
            p.isPaye(),
            p.getDateInscription()
        );
    }
}