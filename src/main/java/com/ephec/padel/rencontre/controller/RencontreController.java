package com.ephec.padel.rencontre.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.model.Visibilite;
import com.ephec.padel.rencontre.service.RencontreService;

@RestController
@RequestMapping("/api/rencontres")
public class RencontreController {

    private final RencontreService rencontreService;

    public RencontreController(RencontreService rencontreService) {
        this.rencontreService = rencontreService;
    }

    @GetMapping
    public ResponseEntity<List<Rencontre>> getAll() {
        return ResponseEntity.ok(rencontreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rencontre> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rencontreService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Rencontre> creer(@RequestBody CreerRencontreRequest req) {
        Rencontre rencontre = rencontreService.creerRencontre(
                req.organisateurId(),
                req.terrainId(),
                req.debut(),
                req.visibilite());
        return ResponseEntity.ok(rencontre);
    }

    // Petit record pour recevoir les données du POST
    public record CreerRencontreRequest(
        Long organisateurId,
        Long terrainId,
        LocalDateTime debut,
        Visibilite visibilite
    ) {}
}