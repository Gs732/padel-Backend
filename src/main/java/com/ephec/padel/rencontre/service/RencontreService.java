package com.ephec.padel.rencontre.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephec.padel.creneau.model.CreneauDTO;
import com.ephec.padel.creneau.service.CreneauService;
import com.ephec.padel.exception.BadRequestException;
import com.ephec.padel.exception.NotFoundException;
import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.model.TypeMembre;
import com.ephec.padel.membre.repository.MembreRepository;
import com.ephec.padel.participation.repository.ParticipationRepository;
import com.ephec.padel.rencontre.dto.RencontreResponse;
import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.model.StatutRencontre;
import com.ephec.padel.rencontre.model.Visibilite;
import com.ephec.padel.rencontre.repository.RencontreRepository;
import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.repository.TerrainRepository;

@Service
public class RencontreService {

    private final RencontreRepository rencontreRepository;
    private final MembreRepository membreRepository;
    private final TerrainRepository terrainRepository;
    private final CreneauService creneauService;
    private final ParticipationRepository participationRepository;

    public RencontreService(RencontreRepository rencontreRepository,
                            MembreRepository membreRepository,
                            TerrainRepository terrainRepository,
                            CreneauService creneauService,
                            ParticipationRepository participationRepository) {
        this.rencontreRepository = rencontreRepository;
        this.membreRepository = membreRepository;
        this.terrainRepository = terrainRepository;
        this.creneauService = creneauService;
        this.participationRepository = participationRepository;
    }

    public List<Rencontre> getAll() {
        return rencontreRepository.findAll();
    }

    public Rencontre getById(Long id) {
        return rencontreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rencontre introuvable : " + id));
    }

    /**
     * Crée une rencontre en vérifiant les 5 règles métier.
     */
    public Rencontre creerRencontre(Long organisateurId, Long terrainId,
                                    LocalDateTime debut, Visibilite visibilite) {

        Membre organisateur = membreRepository.findById(organisateurId)
                .orElseThrow(() -> new NotFoundException("Membre introuvable : " + organisateurId));

        Terrain terrain = terrainRepository.findById(terrainId)
                .orElseThrow(() -> new NotFoundException("Terrain introuvable : " + terrainId));

        // --- Règle 1 : solde à jour ---
        if (organisateur.getSolde() < 0) {
            throw new BadRequestException(
                "Reservation impossible : le membre " + organisateur.getMatricule() +
                " a un solde du de " + organisateur.getSolde() + " EUR.");
        }

        // --- Règle 2 : pas de pénalité en cours ---
        LocalDate penalise = organisateur.getPenaliseJusquAu();
        if (penalise != null && penalise.isAfter(LocalDate.now())) {
            throw new BadRequestException(
                "Reservation impossible : le membre " + organisateur.getMatricule() +
                " est penalise jusqu'au " + penalise + ".");
        }

        // --- Règle 3 : délai selon le type de membre ---
        long joursAvant = ChronoUnit.DAYS.between(LocalDate.now(), debut.toLocalDate());
        int delaiRequis = switch (organisateur.getType()) {
            case GLOBAL -> 21;
            case SITE -> 14;
            case LIBRE -> 5;
        };
        if (joursAvant < delaiRequis) {
            throw new BadRequestException(
                "Reservation impossible : un membre " + organisateur.getType() +
                " doit reserver au moins " + delaiRequis + " jours a l'avance.");
        }

        // --- Règle 4 : membre SITE uniquement sur son site ---
        if (organisateur.getType() == TypeMembre.SITE) {
            Long siteMembre = organisateur.getSite() != null ? organisateur.getSite().getId() : null;
            Long siteTerrain = terrain.getSite().getId();
            if (!siteTerrain.equals(siteMembre)) {
                throw new BadRequestException(
                    "Reservation impossible : le membre SITE " + organisateur.getMatricule() +
                    " ne peut reserver que sur son site.");
            }
        }

        // --- Règle 5 : le créneau doit exister et être libre ---
        List<CreneauDTO> creneaux = creneauService.getCreneaux(terrainId, debut.toLocalDate());
        boolean creneauValide = creneaux.stream()
                .anyMatch(c -> c.debut().equals(debut) && c.disponible());
        if (!creneauValide) {
            throw new BadRequestException(
                "Reservation impossible : aucun creneau libre a " + debut + " sur ce terrain.");
        }

        // --- Toutes les règles passent : on crée la rencontre ---
        Rencontre rencontre = new Rencontre();
        rencontre.setOrganisateur(organisateur);
        rencontre.setTerrain(terrain);
        rencontre.setDebut(debut);
        rencontre.setVisibilite(visibilite);
        rencontre.setStatut(StatutRencontre.OUVERTE);
        rencontre.setPrixTotal(60);

        return rencontreRepository.save(rencontre);
    }

    @Transactional
    public RencontreResponse creerRencontreResponse(Long organisateurId, Long terrainId,
                                                     LocalDateTime debut, Visibilite visibilite) {
        Rencontre rencontre = creerRencontre(organisateurId, terrainId, debut, visibilite);
        return toResponse(rencontre);
    }

    // --- Mapping entité -> DTO ---
    public RencontreResponse toResponse(Rencontre r) {
        long nbJoueurs = participationRepository.countByRencontre_Id(r.getId());
        return new RencontreResponse(
            r.getId(),
            r.getTerrain().getNom(),
            r.getTerrain().getSite().getNom(),
            r.getOrganisateur().getNom() + " " + r.getOrganisateur().getPrenom(),
            r.getDebut(),
            r.getVisibilite(),
            r.getStatut(),
            r.getPrixTotal(),
            nbJoueurs
        );
    }

    @Transactional(readOnly = true)
    public List<RencontreResponse> getAllResponses() {
        return rencontreRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RencontreResponse getByIdResponse(Long id) {
        return toResponse(getById(id));
    }
}