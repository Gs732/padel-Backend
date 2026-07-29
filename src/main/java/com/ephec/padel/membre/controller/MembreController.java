package com.ephec.padel.membre.controller;

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

import com.ephec.padel.membre.dto.CreerMembreRequest;
import com.ephec.padel.membre.dto.MembreResponse;
import com.ephec.padel.membre.service.MembreService;

@RestController
@RequestMapping("/api/membres")
public class MembreController {

    private final MembreService membreService;

    public MembreController(MembreService membreService) {
        this.membreService = membreService;
    }

    @GetMapping
    public ResponseEntity<List<MembreResponse>> getAllMembres() {
        return ResponseEntity.ok(membreService.getAllResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembreResponse> getMembreById(@PathVariable Long id) {
        return ResponseEntity.ok(membreService.getByIdResponse(id));
    }

    @PostMapping
    public ResponseEntity<MembreResponse> creerMembre(@RequestBody CreerMembreRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membreService.creerMembre(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerMembre(@PathVariable Long id) {
        membreService.supprimerMembre(id);
        return ResponseEntity.noContent().build();
    }
}