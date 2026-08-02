package com.ephec.padel.paiement.dto;

import java.time.LocalDateTime;

public record PaiementResponse(
    Long id,
    Long participationId,
    Long rencontreId,
    String membreNom,
    double montant,
    LocalDateTime datePaiement
) {}