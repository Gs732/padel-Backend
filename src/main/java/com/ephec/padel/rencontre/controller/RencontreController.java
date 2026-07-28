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

import com.ephec.padel.rencontre.dto.RencontreResponse;
import com.ephec.padel.rencontre.model.Visibilite;
import com.ephec.padel.rencontre.service.RencontreService;
import com.ephec.padel.rencontre.dto.RencontreResponse;

@RestController
@RequestMapping("/api/rencontres")
public class RencontreController {

    private final RencontreService rencontreService;

    public RencontreController(RencontreService rencontreService) {
        this.rencontreService = rencontreService;
    }

   @GetMapping
    public ResponseEntity<List<RencontreResponse>> getAll() {
        return ResponseEntity.ok(rencontreService.getAllResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RencontreResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rencontreService.getByIdResponse(id));
    }

    @PostMapping
    public ResponseEntity<RencontreResponse> creer(@RequestBody CreerRencontreRequest req) {
        return ResponseEntity.ok(rencontreService.creerRencontreResponse(
                req.organisateurId(),
                req.terrainId(),
                req.debut(),
                req.visibilite()));
    }
    // Petit record pour recevoir les données du POST
    public record CreerRencontreRequest(
        Long organisateurId,
        Long terrainId,
        LocalDateTime debut,
        Visibilite visibilite
    ) {}
}