package com.ephec.padel.membre.dto;

import com.ephec.padel.membre.model.TypeMembre;

public record CreerMembreRequest(
    String nom,
    String prenom,
    String email,
    String telephone,
    TypeMembre type,
    Long siteId
) {}