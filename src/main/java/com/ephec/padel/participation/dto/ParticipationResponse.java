package com.ephec.padel.participation.dto;

import java.time.LocalDateTime;

public record ParticipationResponse(
    Long id,
    Long rencontreId,
    Long membreId,
    String membreNom,
    boolean paye,
    LocalDateTime dateInscription
    
) {}