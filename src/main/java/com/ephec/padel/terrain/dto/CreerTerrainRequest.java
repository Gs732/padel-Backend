package com.ephec.padel.terrain.dto;

public record CreerTerrainRequest(
    String nom,
    String type,
    boolean interieur,
    Long siteId
) {}