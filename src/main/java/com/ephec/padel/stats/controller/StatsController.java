package com.ephec.padel.stats.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ephec.padel.stats.dto.StatsSiteResponse;
import com.ephec.padel.stats.service.StatsService;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/site/{siteId}")
    @PreAuthorize("hasAnyRole('ADMIN_GLOBAL','ADMIN_SITE')")
    public ResponseEntity<StatsSiteResponse> getStatsBySite(@PathVariable Long siteId) {
        return ResponseEntity.ok(statsService.getStatsBySite(siteId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN_GLOBAL')")
    public ResponseEntity<List<StatsSiteResponse>> getStatsAllSites() {
        return ResponseEntity.ok(statsService.getStatsAllSites());
    }
}