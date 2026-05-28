package com.ephec.padel.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    private Long id;
    private Long membreId;
    private Long terrainId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private double prix;
    private String statut; // CONFIRMEE, ANNULEE, EN_ATTENTE
}