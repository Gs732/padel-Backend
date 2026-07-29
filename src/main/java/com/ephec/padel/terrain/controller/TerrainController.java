package com.ephec.padel.terrain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ephec.padel.terrain.dto.CreerTerrainRequest;
import com.ephec.padel.terrain.dto.TerrainResponse;
import com.ephec.padel.terrain.service.TerrainService;

@RestController
@RequestMapping("/api/terrains")
public class TerrainController {

    private final TerrainService terrainService;

    public TerrainController(TerrainService terrainService) {
        this.terrainService = terrainService;
    }

    @GetMapping
    public ResponseEntity<List<TerrainResponse>> getAllTerrains() {
        return ResponseEntity.ok(terrainService.getAllResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerrainResponse> getTerrainById(@PathVariable Long id) {
        return ResponseEntity.ok(terrainService.getByIdResponse(id));
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<TerrainResponse>> getTerrainsBySiteId(@PathVariable Long siteId) {
        return ResponseEntity.ok(terrainService.getBySiteIdResponses(siteId));
    }

    @PostMapping
    public ResponseEntity<TerrainResponse> creerTerrain(@RequestBody CreerTerrainRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(terrainService.creerTerrain(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTerrain(@PathVariable Long id) {
        terrainService.supprimerTerrain(id);
        return ResponseEntity.noContent().build();
    }
}