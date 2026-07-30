package com.ephec.padel.membre.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephec.padel.exception.BadRequestException;
import com.ephec.padel.exception.NotFoundException;
import com.ephec.padel.membre.dto.CreerMembreRequest;
import com.ephec.padel.membre.dto.MembreResponse;
import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.model.TypeMembre;
import com.ephec.padel.membre.repository.MembreRepository;
import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.repository.SiteRepository;

@Service
public class MembreService {

    private final MembreRepository membreRepository;
    private final SiteRepository siteRepository;

    public MembreService(MembreRepository membreRepository,
                         SiteRepository siteRepository) {
        this.membreRepository = membreRepository;
        this.siteRepository = siteRepository;
    }

    @Transactional(readOnly = true)
    public List<MembreResponse> getAllResponses() {
        return membreRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MembreResponse getByIdResponse(Long id) {
        Membre membre = membreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Membre introuvable avec id : " + id));
        return toResponse(membre);
    }

    @Transactional
    public MembreResponse creerMembre(CreerMembreRequest req) {
        // --- Validation : cohérence type <-> site ---
        if (req.type() == TypeMembre.SITE && req.siteId() == null) {
            throw new BadRequestException("Un membre SITE doit etre rattache a un site.");
        }
        if (req.type() != TypeMembre.SITE && req.siteId() != null) {
            throw new BadRequestException(
                "Un membre " + req.type() + " ne peut pas etre rattache a un site.");
        }

        Membre membre = new Membre();
        membre.setNom(req.nom());
        membre.setPrenom(req.prenom());
        membre.setEmail(req.email());
        membre.setTelephone(req.telephone());
        membre.setType(req.type());
        membre.setSolde(0);
        membre.setActif(true);

        // Rattacher le site pour un membre SITE
        if (req.type() == TypeMembre.SITE) {
            Site site = siteRepository.findById(req.siteId())
                    .orElseThrow(() -> new NotFoundException("Site introuvable : " + req.siteId()));
            membre.setSite(site);
        }

        // Générer le matricule selon le type
        membre.setMatricule(genererMatricule(req.type()));

        return toResponse(membreRepository.save(membre));
    }

    @Transactional
    public void supprimerMembre(Long id) {
        if (!membreRepository.existsById(id)) {
            throw new NotFoundException("Membre introuvable avec id : " + id);
        }
        membreRepository.deleteById(id);
    }

    // --- Mapping entité -> DTO ---
    private MembreResponse toResponse(Membre m) {
        String siteNom = (m.getSite() != null) ? m.getSite().getNom() : null;
        return new MembreResponse(
            m.getId(),
            m.getMatricule(),
            m.getNom(),
            m.getPrenom(),
            m.getEmail(),
            m.getTelephone(),
            m.getSolde(),
            m.isActif(),
            m.getType(),
            siteNom
        );
    }

    // --- Génération automatique du matricule ---
    private String genererMatricule(TypeMembre type) {
        long count = membreRepository.countByType(type) + 1;
        String numero = String.format("%04d", count);
        return switch (type) {
            case GLOBAL -> "G" + numero;
            case SITE -> "S" + numero;
            case LIBRE -> "L" + numero;
        };
    }
}