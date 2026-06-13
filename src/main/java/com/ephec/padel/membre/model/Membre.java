package com.ephec.padel.membre.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Membre {
    
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private double solde;
    private boolean actif;
    private TypeMembre type;

    public enum TypeMembre {
        GLOBAL,
        SITE,
        LIBRE
    }
}
