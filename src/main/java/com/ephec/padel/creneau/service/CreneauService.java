package com.ephec.padel.creneau.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ephec.padel.creneau.model.CreneauDTO;
import com.ephec.padel.fermeture.repository.FermetureRepository;
import com.ephec.padel.horaire.model.Horaire;
import com.ephec.padel.horaire.repository.HoraireRepository;
import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.repository.RencontreRepository;
import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.repository.TerrainRepository;

@Service
public class CreneauService {

    private static final int DUREE_MATCH_MINUTES = 90;   // 1h30
    private static final int PAUSE_MINUTES = 15;          // entre deux matchs
    private static final int PAS_MINUTES = DUREE_MATCH_MINUTES + PAUSE_MINUTES; // 105

    private final TerrainRepository terrainRepository;
    private final HoraireRepository horaireRepository;
    private final FermetureRepository fermetureRepository;
    private final RencontreRepository rencontreRepository;

    public CreneauService(TerrainRepository terrainRepository,
                          HoraireRepository horaireRepository,
                          FermetureRepository fermetureRepository,
                          RencontreRepository rencontreRepository) {
        this.terrainRepository = terrainRepository;
        this.horaireRepository = horaireRepository;
        this.fermetureRepository = fermetureRepository;
        this.rencontreRepository = rencontreRepository;
    }

    public List<CreneauDTO> getCreneaux(Long terrainId, LocalDate date) {
        // 1. Récupérer le terrain et son site
        Terrain terrain = terrainRepository.findById(terrainId)
                .orElseThrow(() -> new RuntimeException("Terrain introuvable : " + terrainId));
        Long siteId = terrain.getSite().getId();

        // 2. Récupérer l'horaire du site pour l'année de la date demandée
        Horaire horaire = horaireRepository
                .findBySite_IdAndAnnee(siteId, date.getYear())
                .orElseThrow(() -> new RuntimeException(
                    "Aucun horaire defini pour le site " + siteId + " en " + date.getYear()));

        // 3. Le site est-il fermé ce jour-là ? (fermeture du site OU globale)
        boolean ferme = fermetureRepository.existsByDateFermetureAndSite_Id(date, siteId)
                || fermetureRepository.existsByDateFermetureAndSiteIsNull(date);
        if (ferme) {
            return List.of(); // aucun créneau si le site est fermé
        }

        // 4. Récupérer les rencontres déjà posées sur ce terrain (pour marquer l'occupation)
        List<Rencontre> rencontres = rencontreRepository.findByTerrain_Id(terrainId);

        // 5. Générer les créneaux de 105 min entre l'ouverture et la fermeture
        List<CreneauDTO> creneaux = new ArrayList<>();
        LocalTime curseur = horaire.getHeureDebut();

while (true) {
    LocalTime finCreneau = curseur.plusMinutes(DUREE_MATCH_MINUTES);

    // Arrêt si le créneau dépasse l'heure de fermeture,
    // OU s'il franchit minuit (finCreneau repasse avant le début)
    if (finCreneau.isAfter(horaire.getHeureFin()) || finCreneau.isBefore(curseur)) {
        break;
    }

    LocalDateTime debut = LocalDateTime.of(date, curseur);
    LocalDateTime fin = LocalDateTime.of(date, finCreneau);

    boolean occupe = rencontres.stream()
            .anyMatch(r -> r.getDebut().equals(debut));

    creneaux.add(new CreneauDTO(debut, fin, !occupe));

    LocalTime prochain = curseur.plusMinutes(PAS_MINUTES);
    // Arrêt si le pas suivant franchit minuit
    if (prochain.isBefore(curseur)) {
        break;
    }
    curseur = prochain;
}

        return creneaux;
    }
}