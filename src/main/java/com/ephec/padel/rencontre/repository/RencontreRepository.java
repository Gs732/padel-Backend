package com.ephec.padel.rencontre.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.model.Visibilite;
import java.time.LocalDateTime;

public interface RencontreRepository extends JpaRepository<Rencontre, Long> {

    List<Rencontre> findByVisibilite(Visibilite visibilite);

    List<Rencontre> findByTerrain_Site_Id(Long siteId);

    List<Rencontre> findByTerrain_Id(Long terrainId);

    boolean existsByTerrain_IdAndDebut(Long terrainId, LocalDateTime debut);
}