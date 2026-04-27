package com.ephec.padel.site.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.repository.SiteRepository;

@Service
public class SiteService {
    private final SiteRepository siteRepository;

    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
}

public List<Site> getAllSites(){
    return siteRepository.findAll();
}

public Site getSiteById(Long id) {
    return siteRepository.findById(id).orElseThrow(() ->
    new RuntimeException("Site introuvable avec id : " + id));
}

public Site creerSite(Site site) {
    return siteRepository.save(site);
}

public void supprimerSite(Long id) {
    if (!siteRepository.existsById(id)) {
        throw new RuntimeException(new RuntimeException("Site introuvable avec id : " + id));

    }
    siteRepository.deleteById(id);
    }
}