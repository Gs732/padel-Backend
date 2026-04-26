package com.ephec.padel.terrain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.ephec.padel.terrain.model.Terrain;

@Repository
public class TerrainRepository {

    private final Map<Long, Terrain> terrains = new HashMap<>();
    private long nextId = 1;

    public List <Terrain> findAll(){
        return new ArrayList<>(terrains.values());
    }

    public List<Terrain> findBySiteId(Long siteId) {
        return terrains.values().stream()
            .filter(t -> t.getSiteId().equals(siteId))
            .collect(Collectors.toList());
    }

    public Optional<Terrain> findById(Long id){
        return Optional.ofNullable(terrains.get(id));
    }

    public Terrain save(Terrain terrain) {
        if (terrain.getId() == null) {
            terrain.setId(nextId++);
        }
        terrains.put(terrain.getId(), terrain);
        return terrain;
    }

    public void deleteById(Long id) {
        terrains.remove(id);
    }

    public boolean existsById(Long id) {
        return terrains.containsKey(id);
    }

}
