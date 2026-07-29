package com.ephec.padel.terrain.dto;

public record TerrainResponse(
    Long id,
    String nom,
    String type,
    boolean interieur,
    boolean actif,
    Long siteId,
    String siteNom
) {}