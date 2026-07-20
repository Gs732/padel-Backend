package com.ephec.padel.terrain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ephec.padel.terrain.model.Terrain;

public interface TerrainRepository extends JpaRepository<Terrain, Long> {

    List<Terrain> findBySite_Id(Long siteId);
}