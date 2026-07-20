package com.ephec.padel.terrain.model;

import com.ephec.padel.site.model.Site;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "terrain")
@Getter
@Setter
@NoArgsConstructor
public class Terrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(length = 50)
    private String type;

    @Column(nullable = false)
    private boolean interieur;

    @Column(nullable = false)
    private boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;
}