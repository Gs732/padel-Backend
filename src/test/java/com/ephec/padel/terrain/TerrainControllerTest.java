package com.ephec.padel.terrain;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ephec.padel.terrain.controller.TerrainController;
import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.service.TerrainService;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TerrainControllerTest {

    @InjectMocks
    private TerrainController terrainController;

    @Mock
    private TerrainService terrainService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(terrainController).build();
        objectMapper = new ObjectMapper();
    }

    // -------------------------------------------------------
    // GET /api/terrains
    // -------------------------------------------------------
    @Test
    void getAllTerrains_should_return_200_with_list() throws Exception {
        // Arrange
        Terrain t1 = new Terrain(1L, "Terrain 1", "INDOOR", true, true, 1L);
        Terrain t2 = new Terrain(2L, "Terrain 2", "OUTDOOR", false, true, 1L);
        when(terrainService.getAllTerrains()).thenReturn(List.of(t1, t2));

        // Act & Assert
        mockMvc.perform(get("/api/terrains"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nom").value("Terrain 1"))
                .andExpect(jsonPath("$[1].nom").value("Terrain 2"));
    }

    // -------------------------------------------------------
    // GET /api/terrains/{id}
    // -------------------------------------------------------
    @Test
    void getTerrainById_should_return_200_when_exists() throws Exception {
        // Arrange
        Terrain t = new Terrain(1L, "Terrain 1", "INDOOR", true, true, 1L);
        when(terrainService.getTerrainById(1L)).thenReturn(t);

        // Act & Assert
        mockMvc.perform(get("/api/terrains/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Terrain 1"))
                .andExpect(jsonPath("$.type").value("INDOOR"));
    }

    // -------------------------------------------------------
    // GET /api/terrains/site/{siteId}
    // -------------------------------------------------------
    @Test
    void getTerrainsBySiteId_should_return_200_with_list() throws Exception {
        // Arrange
        Terrain t1 = new Terrain(1L, "Terrain 1", "INDOOR", true, true, 1L);
        Terrain t2 = new Terrain(2L, "Terrain 2", "OUTDOOR", false, true, 1L);
        when(terrainService.getTerrainsBySiteId(1L)).thenReturn(List.of(t1, t2));

        // Act & Assert
        mockMvc.perform(get("/api/terrains/site/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].siteId").value(1));
    }

    // -------------------------------------------------------
    // POST /api/terrains
    // -------------------------------------------------------
    @Test
    void creerTerrain_should_return_201_with_terrain() throws Exception {
        // Arrange
        Terrain t = new Terrain(null, "Terrain 1", "INDOOR", true, true, 1L);
        Terrain saved = new Terrain(1L, "Terrain 1", "INDOOR", true, true, 1L);
        when(terrainService.creerTerrain(any(Terrain.class))).thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/terrains")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Terrain 1"));
    }

    // -------------------------------------------------------
    // DELETE /api/terrains/{id}
    // -------------------------------------------------------
    @Test
    void supprimerTerrain_should_return_204() throws Exception {
        // Arrange
        doNothing().when(terrainService).supprimerTerrain(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/terrains/1"))
                .andExpect(status().isNoContent());

        verify(terrainService, times(1)).supprimerTerrain(1L);
    }
}
