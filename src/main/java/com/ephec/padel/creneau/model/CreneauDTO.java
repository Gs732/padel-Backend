package com.ephec.padel.creneau.model;

import java.time.LocalDateTime;

public record CreneauDTO(
    LocalDateTime debut,
    LocalDateTime fin,
    boolean disponible
) {}