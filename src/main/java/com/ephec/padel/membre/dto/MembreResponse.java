package com.ephec.padel.membre.dto;

import com.ephec.padel.membre.model.TypeMembre;

public record MembreResponse(
    Long id,
    String matricule,
    String nom,
    String prenom,
    String email,
    String telephone,
    double solde,
    boolean actif,
    TypeMembre type,
    String siteNom
) {}