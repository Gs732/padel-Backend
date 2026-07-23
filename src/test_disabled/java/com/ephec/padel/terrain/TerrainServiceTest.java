package com.ephec.padel.terrain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.repository.TerrainRepository;
import com.ephec.padel.terrain.service.TerrainService;

@ExtendWith(MockitoExtension.class)
class TerrainServiceTest {

    @Mock
    private TerrainRepository terrainRepository;

    @InjectMocks
    private TerrainService terrainService;

    // -------------------------------------------------------
    // getAllTerrains
    // -------------------------------------------------------
    @Test
    void getAllTerrains_should_return_all_terrains() {
        // Arrange
        Terrain t1 = new Terrain(1L, "Terrain 1", "INDOOR", true, true, 1L);
        Terrain t2 = new Terrain(2L, "Terrain 2", "OUTDOOR", false, true, 1L);
        when(terrainRepository.findAll()).thenReturn(List.of(t1, t2));

        // Act
        List<Terrain> result = terrainService.getAllTerrains();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(t1, t2);
        verify(terrainRepository, times(1)).findAll();
    }

    // -------------------------------------------------------
    // getTerrainById
    // -------------------------------------------------------
    @Test
    void getTerrainById_should_return_terrain_when_exists() {
        // Arrange
        Terrain t = new Terrain(1L, "Terrain 1", "INDOOR", true, true, 1L);
        when(terrainRepository.findById(1L)).thenReturn(Optional.of(t));

        // Act
        Terrain result = terrainService.getTerrainById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Terrain 1");
        verify(terrainRepository, times(1)).findById(1L);
    }

    @Test
    void getTerrainById_should_throw_exception_when_not_exists() {
        // Arrange
        when(terrainRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> terrainService.getTerrainById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
        verify(terrainRepository, times(1)).findById(99L);
    }

    // -------------------------------------------------------
    // getTerrainsBySiteId
    // -------------------------------------------------------
    @Test
    void getTerrainsBySiteId_should_return_terrains_for_site() {
        // Arrange
        Terrain t1 = new Terrain(1L, "Terrain 1", "INDOOR", true, true, 1L);
        Terrain t2 = new Terrain(2L, "Terrain 2", "OUTDOOR", false, true, 1L);
        when(terrainRepository.findBySiteId(1L)).thenReturn(List.of(t1, t2));

        // Act
        List<Terrain> result = terrainService.getTerrainsBySiteId(1L);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(t -> t.getSiteId().equals(1L));
        verify(terrainRepository, times(1)).findBySiteId(1L);
    }

    // -------------------------------------------------------
    // creerTerrain
    // -------------------------------------------------------
    @Test
    void creerTerrain_should_save_and_return_terrain() {
        // Arrange
        Terrain t = new Terrain(null, "Terrain 1", "INDOOR", true, true, 1L);
        Terrain saved = new Terrain(1L, "Terrain 1", "INDOOR", true, true, 1L);
        when(terrainRepository.save(t)).thenReturn(saved);

        // Act
        Terrain result = terrainService.creerTerrain(t);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(terrainRepository, times(1)).save(t);
    }

    // -------------------------------------------------------
    // supprimerTerrain
    // -------------------------------------------------------
    @Test
    void supprimerTerrain_should_delete_when_exists() {
        // Arrange
        when(terrainRepository.existsById(1L)).thenReturn(true);

        // Act
        terrainService.supprimerTerrain(1L);

        // Assert
        verify(terrainRepository, times(1)).deleteById(1L);
    }

    @Test
    void supprimerTerrain_should_throw_exception_when_not_exists() {
        // Arrange
        when(terrainRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> terrainService.supprimerTerrain(99L))
                .isInstanceOf(RuntimeException.class);
        verify(terrainRepository, never()).deleteById(any());
    }
}
