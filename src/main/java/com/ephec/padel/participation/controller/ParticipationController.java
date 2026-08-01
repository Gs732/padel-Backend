package com.ephec.padel.participation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ephec.padel.participation.dto.ParticipationResponse;
import com.ephec.padel.participation.service.ParticipationService;

@RestController
@RequestMapping("/api/participations")
public class ParticipationController {

    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping
    public ResponseEntity<ParticipationResponse> rejoindre(
            @RequestParam Long rencontreId,
            @RequestParam Long membreId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(participationService.rejoindre(rencontreId, membreId));
    }

    @GetMapping("/rencontre/{rencontreId}")
    public ResponseEntity<List<ParticipationResponse>> getByRencontre(@PathVariable Long rencontreId) {
        return ResponseEntity.ok(participationService.getByRencontre(rencontreId));
    }
}