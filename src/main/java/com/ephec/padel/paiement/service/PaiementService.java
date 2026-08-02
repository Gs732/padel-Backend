package com.ephec.padel.paiement.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephec.padel.exception.BadRequestException;
import com.ephec.padel.exception.NotFoundException;
import com.ephec.padel.paiement.dto.PaiementResponse;
import com.ephec.padel.paiement.model.Paiement;
import com.ephec.padel.paiement.repository.PaiementRepository;
import com.ephec.padel.participation.model.Participation;
import com.ephec.padel.participation.repository.ParticipationRepository;
import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.model.StatutRencontre;
import com.ephec.padel.rencontre.repository.RencontreRepository;

@Service
public class PaiementService {

    private static final int JOUEURS_COMPLET = 4;

    private final PaiementRepository paiementRepository;
    private final ParticipationRepository participationRepository;
    private final RencontreRepository rencontreRepository;

    public PaiementService(PaiementRepository paiementRepository,
                           ParticipationRepository participationRepository,
                           RencontreRepository rencontreRepository) {
        this.paiementRepository = paiementRepository;
        this.participationRepository = participationRepository;
        this.rencontreRepository = rencontreRepository;
    }

    @Transactional
    public PaiementResponse payer(Long participationId) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new NotFoundException(
                    "Participation introuvable : " + participationId));

        // --- Règle : pas de double paiement ---
        if (participation.isPaye()) {
            throw new BadRequestException("Cette participation est deja payee.");
        }

        Rencontre rencontre = participation.getRencontre();

        // Montant = prix total / 4 joueurs
        double montant = rencontre.getPrixTotal() / JOUEURS_COMPLET;

        // Créer le paiement
        Paiement paiement = new Paiement();
        paiement.setParticipation(participation);
        paiement.setMontant(montant);
        paiement = paiementRepository.save(paiement);

        // Marquer la participation comme payée
        participation.setPaye(true);
        participationRepository.save(participation);

        // --- Bascule en COMPLETE si 4 places payées ---
        long nbPayes = participationRepository.countByRencontre_IdAndPayeTrue(rencontre.getId());
        if (nbPayes >= JOUEURS_COMPLET) {
            rencontre.setStatut(StatutRencontre.COMPLETE);
            rencontreRepository.save(rencontre);
        }

        return toResponse(paiement);
    }

    // --- Mapping entité -> DTO ---
    private PaiementResponse toResponse(Paiement p) {
        Participation part = p.getParticipation();
        return new PaiementResponse(
            p.getId(),
            part.getId(),
            part.getRencontre().getId(),
            part.getMembre().getNom() + " " + part.getMembre().getPrenom(),
            p.getMontant(),
            p.getDatePaiement()
        );
    }
}