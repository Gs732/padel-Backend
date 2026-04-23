package com.ephec.padel.site.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ephec.padel.site.model.Site;

@Repository
public class SiteRepository {
    private final Map<Long, Site> sites = new HashMap<>();
    private long nextId = 1;

    public List<Site> findAll() {
        return new ArrayList<>(sites.values());
    }

    public Optional<Site> findById(Long id){
        return Optional.ofNullable(sites.get(id));
    }

    public Site save(Site site) {
        if (site.getId() == null) {
            site.setId(nextId++);
        }
        sites.put(site.getId(), site);
        return site;
    }

    public void deleteById(Long id) {
        sites.remove(id);
    }

    public boolean existsById(Long id) {
        return sites.containsKey(id);
    }

}
