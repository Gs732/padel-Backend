package com.ephec.padel.membre.controller;

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

import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.service.MembreService;

@RestController
@RequestMapping("/api/membres")

public class MembreController {
    
    private final MembreService membreService;

    public MembreController(MembreService membreService) {
        this. membreService = membreService;
    }

@GetMapping
public ResponseEntity<List<Membre>> getAllMembres() {
    return ResponseEntity.ok(membreService.getAllMembres());
}

@GetMapping("/{id}")
public ResponseEntity<Membre> getMembreById(@PathVariable Long id) {
    return ResponseEntity.ok (membreService.getMembreById(id));
}

@PostMapping
    public ResponseEntity<Membre> creerMembre(@RequestBody Membre membre) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membreService.creerMembre(membre));
    }

@PutMapping ("/{id}")
public ResponseEntity<Membre> modifierMembre(@PathVariable Long id, @RequestBody Membre membre) {
    return ResponseEntity.ok(membreService.modifierMembre(id, membre));
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> supprimerMembre(@PathVariable Long id) {
    membreService.supprimerMembre(id);
    return ResponseEntity.noContent(). build();
}

}