package com.ephec.padel.creneau.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ephec.padel.creneau.model.CreneauDTO;
import com.ephec.padel.creneau.service.CreneauService;

@RestController
@RequestMapping("/api/creneaux")
public class CreneauController {

    private final CreneauService creneauService;

    public CreneauController(CreneauService creneauService) {
        this.creneauService = creneauService;
    }

    @GetMapping
    public ResponseEntity<List<CreneauDTO>> getCreneaux(
            @RequestParam Long terrainId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<CreneauDTO> creneaux = creneauService.getCreneaux(terrainId, date);
        return ResponseEntity.ok(creneaux);
    }
}