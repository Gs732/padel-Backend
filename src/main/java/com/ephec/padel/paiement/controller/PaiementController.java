package com.ephec.padel.paiement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ephec.padel.paiement.dto.PaiementResponse;
import com.ephec.padel.paiement.service.PaiementService;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @PostMapping
    public ResponseEntity<PaiementResponse> payer(@RequestParam Long participationId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paiementService.payer(participationId));
    }
}