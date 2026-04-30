package com.ephec.padel.site.controller;

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

import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.service.SiteService;

@RestController
@RequestMapping("/api/sites")

public class SiteController {
    private final SiteService siteService;

    public SiteController(SiteService siteService){
        this.siteService = siteService;
    }

    @GetMapping
    public ResponseEntity<List<Site>> getAllSites() {
        return ResponseEntity.ok(siteService.getAllSites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> getSiteById(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.getSiteById(id));
    }

    @PostMapping
    public ResponseEntity<Site> creerSite(@RequestBody Site site) {
        return ResponseEntity.status(HttpStatus.CREATED).body(siteService.creerSite(site));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Site>modifierSite(@PathVariable Long id,
    @RequestBody Site site){
        return ResponseEntity.ok(siteService.modifierSite(id, site));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerSite(@PathVariable Long id) {
        siteService.supprimerSite(id);
        return ResponseEntity.noContent().build();
    }

}
