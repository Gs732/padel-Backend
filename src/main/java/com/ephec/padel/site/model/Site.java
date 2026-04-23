package com.ephec.padel.site.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Site {

    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    private String telephone;
    private boolean actif;
}