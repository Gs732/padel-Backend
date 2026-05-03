package com.ephec.padel.terrain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.service.TerrainService;

@RestController
@RequestMapping("/api/terrains")
public class TerrainController {
    
    private final TerrainService terrainService;

    public TerrainController(TerrainService terrainService) {
        this.terrainService = terrainService;
    }

    @GetMapping
    public ResponseEntity<List<Terrain>> getAllTerrains() {
        return ResponseEntity.ok(terrainService.getAllTerrains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terrain> getTerrainById(@PathVariable Long id) {
        return ResponseEntity.ok(terrainService.getTerrainById(id));
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<Terrain>> getTerrainsBySiteId(@PathVariable Long siteId) {
        return ResponseEntity.ok(terrainService.getTerrainsBySiteId(siteId));

    }

    @PostMapping
    public ResponseEntity<Terrain> creerTerrain(@RequestBody Terrain terrain) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(terrainService.creerTerrain(terrain));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terrain> modifierTerrain(@PathVariable Long id, @RequestBody Terrain terrain) {
        return ResponseEntity.ok(terrainService.modifierTerrain(id, terrain));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTerrain(@PathVariable Long id){
        terrainService.supprimerTerrain(id);
        return ResponseEntity.noContent().build();
    }
}
