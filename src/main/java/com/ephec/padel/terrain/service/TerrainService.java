package com.ephec.padel.terrain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephec.padel.exception.NotFoundException;
import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.repository.SiteRepository;
import com.ephec.padel.terrain.dto.CreerTerrainRequest;
import com.ephec.padel.terrain.dto.TerrainResponse;
import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.repository.TerrainRepository;

@Service
public class TerrainService {

    private final TerrainRepository terrainRepository;
    private final SiteRepository siteRepository;

    public TerrainService(TerrainRepository terrainRepository,
                          SiteRepository siteRepository) {
        this.terrainRepository = terrainRepository;
        this.siteRepository = siteRepository;
    }

    @Transactional(readOnly = true)
    public List<TerrainResponse> getAllResponses() {
        return terrainRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TerrainResponse getByIdResponse(Long id) {
        Terrain terrain = terrainRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Terrain introuvable : " + id));
        return toResponse(terrain);
    }

    @Transactional(readOnly = true)
    public List<TerrainResponse> getBySiteIdResponses(Long siteId) {
        return terrainRepository.findBySite_Id(siteId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TerrainResponse creerTerrain(CreerTerrainRequest req) {
        Site site = siteRepository.findById(req.siteId())
                .orElseThrow(() -> new NotFoundException("Site introuvable : " + req.siteId()));

        Terrain terrain = new Terrain();
        terrain.setNom(req.nom());
        terrain.setType(req.type());
        terrain.setInterieur(req.interieur());
        terrain.setActif(true);
        terrain.setSite(site);

        return toResponse(terrainRepository.save(terrain));
    }

    @Transactional
    public void supprimerTerrain(Long id) {
        if (!terrainRepository.existsById(id)) {
            throw new NotFoundException("Terrain introuvable : " + id);
        }
        terrainRepository.deleteById(id);
    }

    // --- Mapping entité -> DTO ---
    private TerrainResponse toResponse(Terrain t) {
        return new TerrainResponse(
            t.getId(),
            t.getNom(),
            t.getType(),
            t.isInterieur(),
            t.isActif(),
            t.getSite().getId(),
            t.getSite().getNom()
        );
    }
}