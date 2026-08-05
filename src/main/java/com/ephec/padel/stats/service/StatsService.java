package com.ephec.padel.stats.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephec.padel.exception.NotFoundException;
import com.ephec.padel.paiement.model.Paiement;
import com.ephec.padel.paiement.repository.PaiementRepository;
import com.ephec.padel.rencontre.repository.RencontreRepository;
import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.repository.SiteRepository;
import com.ephec.padel.stats.dto.StatsSiteResponse;

@Service
public class StatsService {

    private final PaiementRepository paiementRepository;
    private final RencontreRepository rencontreRepository;
    private final SiteRepository siteRepository;

    public StatsService(PaiementRepository paiementRepository,
                        RencontreRepository rencontreRepository,
                        SiteRepository siteRepository) {
        this.paiementRepository = paiementRepository;
        this.rencontreRepository = rencontreRepository;
        this.siteRepository = siteRepository;
    }

    @Transactional(readOnly = true)
    public StatsSiteResponse getStatsBySite(Long siteId) {
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new NotFoundException("Site introuvable : " + siteId));

        List<Paiement> paiements =
                paiementRepository.findByParticipation_Rencontre_Terrain_Site_Id(siteId);

        double ca = paiements.stream().mapToDouble(Paiement::getMontant).sum();
        long nombreMatchs = rencontreRepository.countByTerrain_Site_Id(siteId);

        return new StatsSiteResponse(
            site.getId(),
            site.getNom(),
            ca,
            nombreMatchs,
            paiements.size()
        );
    }

    @Transactional(readOnly = true)
    public List<StatsSiteResponse> getStatsAllSites() {
        return siteRepository.findAll().stream()
                .map(s -> getStatsBySite(s.getId()))
                .toList();
    }
}