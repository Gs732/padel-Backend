package com.ephec.padel.rencontre.model;

import java.time.LocalDateTime;

import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.terrain.model.Terrain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rencontre")
@Getter
@Setter
@NoArgsConstructor
public class Rencontre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terrain_id", nullable = false)
    private Terrain terrain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisateur_id", nullable = false)
    private Membre organisateur;

    @Column(nullable = false)
    private LocalDateTime debut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Visibilite visibilite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutRencontre statut;

    @Column(name = "prix_total", nullable = false)
    private double prixTotal = 60;
}