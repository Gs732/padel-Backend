package com.ephec.padel.terrain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terrain {

    private Long id;
    private String nom;
    private String type;
    private boolean interieur;
    private boolean actif;
    private Long siteId;
}

