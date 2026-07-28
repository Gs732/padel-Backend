package com.ephec.padel.rencontre.dto;

import java.time.LocalDateTime;

import com.ephec.padel.rencontre.model.StatutRencontre;
import com.ephec.padel.rencontre.model.Visibilite;

public record RencontreResponse(
    Long id,
    String terrainNom,
    String siteNom,
    String organisateurNom,
    LocalDateTime debut,
    Visibilite visibilite,
    StatutRencontre statut,
    double prixTotal,
    long nombreJoueurs
) {}