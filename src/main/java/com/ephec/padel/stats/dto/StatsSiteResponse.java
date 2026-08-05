package com.ephec.padel.stats.dto;

public record StatsSiteResponse(
    Long siteId,
    String siteNom,
    double chiffreAffaires,
    long nombreMatchs,
    long nombrePaiements
) {}