package com.ephec.padel.fermeture.model;

import java.time.LocalDate;

import com.ephec.padel.site.model.Site;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fermeture")
@Getter
@Setter
@NoArgsConstructor
public class Fermeture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;   // null = fermeture globale

    @Column(name = "date_fermeture", nullable = false)
    private LocalDate dateFermeture;

    @Column(length = 200)
    private String motif;
}