package com.ephec.padel.reservation.controller;

import com.ephec.padel.reservation.model.Reservation;
import com.ephec.padel.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping("/membre/{membreId}")
    public ResponseEntity<List<Reservation>> getReservationsByMembreId(
            @PathVariable Long membreId) {
        return ResponseEntity.ok(
            reservationService.getReservationsByMembreId(membreId));
    }

    @GetMapping("/terrain/{terrainId}")
    public ResponseEntity<List<Reservation>> getReservationsByTerrainId(
            @PathVariable Long terrainId) {
        return ResponseEntity.ok(
            reservationService.getReservationsByTerrainId(terrainId));
    }

    @PostMapping
    public ResponseEntity<Reservation> creerReservation(
            @RequestBody Reservation reservation) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.creerReservation(reservation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> modifierReservation(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {
        return ResponseEntity.ok(
            reservationService.modifierReservation(id, reservation));
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<Void> annulerReservation(@PathVariable Long id) {
        reservationService.annulerReservation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerReservation(@PathVariable Long id) {
        reservationService.supprimerReservation(id);
        return ResponseEntity.noContent().build();
    }
}