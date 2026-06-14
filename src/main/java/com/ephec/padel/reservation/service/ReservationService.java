package com.ephec.padel.reservation.service;

import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.service.MembreService;
import com.ephec.padel.reservation.model.Reservation;
import com.ephec.padel.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MembreService membreService;

    public ReservationService(ReservationRepository reservationRepository, MembreService membreService) {
        this.reservationRepository = reservationRepository;
        this.membreService = membreService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByMembreId(Long membreId) {
        return reservationRepository.findByMembreId(membreId);
    }

    public List<Reservation> getReservationsByTerrainId(Long terrainId) {
        return reservationRepository.findByTerrainId(terrainId);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "Réservation introuvable avec id : " + id));
    }

    public Reservation creerReservation(Reservation reservation) {
        // Vérifier le solde du membre
        Membre membre = membreService.getMembreById(reservation.getMembreId());
        if (membre.getSolde() < 0) {
             throw new RuntimeException(
        "Réservation impossible : le membre '" + membre.getMatricule() +
        "' a un solde dû de " + membre.getSolde() + " €. " +
        "Veuillez régulariser le solde avant de réserver.");
}
    // Vérifier le délai de réservation selon le type de membre
    long joursAvantReservation = java.time.Duration.between(
            java.time.LocalDateTime.now(), reservation.getDateDebut()
    ).toDays();

    int delaiRequis = switch (membre.getType()) {
        case GLOBAL -> 21; // 3 semaines
        case SITE -> 14;   // 2 semaines
        case LIBRE -> 5;   // 5 jours
    };

    if (joursAvantReservation < delaiRequis) {
        throw new RuntimeException(
            "Réservation impossible : le membre '" + membre.getMatricule() +
            "' (" + membre.getType() + ") doit réserver au moins " +
            delaiRequis + " jours à l'avance.");
    }
        reservation.setStatut("EN_ATTENTE");
        return reservationRepository.save(reservation);
    }

    public Reservation modifierReservation(Long id, Reservation reservation) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException(
                "Réservation introuvable avec id : " + id);
        }
        reservation.setId(id);
        return reservationRepository.save(reservation);
    }

    public void annulerReservation(Long id) {
        Reservation reservation = getReservationById(id);
        reservation.setStatut("ANNULEE");
        reservationRepository.save(reservation);
    }

    public void supprimerReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException(
                "Réservation introuvable avec id : " + id);
        }
        reservationRepository.deleteById(id);
    }
}