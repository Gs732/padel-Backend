package com.ephec.padel.terrain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.repository.TerrainRepository;

@Service
public class TerrainService {
    
    private final TerrainRepository terrainRepository;

    public TerrainService(TerrainRepository terrainRepository){
        this.terrainRepository = terrainRepository;

    }

    public List<Terrain> getAllTerrains() {
        return terrainRepository.findAll();
    }

    public List<Terrain> getTerrainsBySiteId(Long siteId) {
        return terrainRepository.findBySiteId(siteId);
    }

    public Terrain getTerrainById(Long id) {
        return terrainRepository.findById(id). orElseThrow(() -> new RuntimeException("Terrain introuvable avec id " + id));

    }

    public Terrain creerTerrain(Terrain terrain) {
        return terrainRepository.save(terrain);
    }

    public Terrain modifierTerrain(Long id, Terrain terrain) {
        if (!terrainRepository.existsById(id)) {
            throw new RuntimeException("Terrain introuvable avec id" + id);
        }
        terrain.setId(id);
        return terrainRepository.save(terrain);
    }

    public void supprimerTerrain(Long id){
        if (!terrainRepository.existsById(id)){
            throw new RuntimeException("Terrain introuvable avec id : " + id);

        }
        terrainRepository.deleteById(id);
    }
}
