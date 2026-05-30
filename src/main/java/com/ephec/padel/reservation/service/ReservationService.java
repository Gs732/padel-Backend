package com.ephec.padel.reservation.service;

import com.ephec.padel.reservation.model.Reservation;
import com.ephec.padel.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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