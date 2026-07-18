package com.ephec.padel.site.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "site")
@Getter
@Setter
@NoArgsConstructor
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(length = 200)
    private String adresse;

    @Column(length = 100)
    private String ville;

    @Column(length = 30)
    private String telephone;

    @Column(nullable = false)
    private boolean actif = true;
}