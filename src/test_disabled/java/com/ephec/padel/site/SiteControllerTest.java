package com.ephec.padel.site;

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

import com.ephec.padel.site.controller.SiteController;
import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.service.SiteService;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class SiteControllerTest {

    @InjectMocks
    private SiteController siteController;

    @Mock
    private SiteService siteService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(siteController).build();
        objectMapper = new ObjectMapper();
    }

    // -------------------------------------------------------
    // GET /api/sites
    // -------------------------------------------------------
    @Test
    void getAllSites_should_return_200_with_list() throws Exception {
        // Arrange
        Site s1 = new Site(1L, "Padel Club", "Rue de la Paix 1", "Bruxelles", "0456123456", true);
        Site s2 = new Site(2L, "Padel Arena", "Avenue Louise 10", "Bruxelles", "0478987654", true);
        when(siteService.getAllSites()).thenReturn(List.of(s1, s2));

        // Act & Assert
        mockMvc.perform(get("/api/sites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nom").value("Padel Club"))
                .andExpect(jsonPath("$[1].nom").value("Padel Arena"));
    }

    // -------------------------------------------------------
    // GET /api/sites/{id}
    // -------------------------------------------------------
    @Test
    void getSiteById_should_return_200_when_exists() throws Exception {
        // Arrange
        Site s = new Site(1L, "Padel Club", "Rue de la Paix 1", "Bruxelles", "0456123456", true);
        when(siteService.getSiteById(1L)).thenReturn(s);

        // Act & Assert
        mockMvc.perform(get("/api/sites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Padel Club"))
                .andExpect(jsonPath("$.ville").value("Bruxelles"));
    }

    // -------------------------------------------------------
    // POST /api/sites
    // -------------------------------------------------------
    @Test
    void creerSite_should_return_201_with_site() throws Exception {
        // Arrange
        Site s = new Site(null, "Padel Club", "Rue de la Paix 1", "Bruxelles", "0456123456", true);
        Site saved = new Site(1L, "Padel Club", "Rue de la Paix 1", "Bruxelles", "0456123456", true);
        when(siteService.creerSite(any(Site.class))).thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(s)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Padel Club"));
    }

    // -------------------------------------------------------
    // DELETE /api/sites/{id}
    // -------------------------------------------------------
    @Test
    void supprimerSite_should_return_204() throws Exception {
        // Arrange
        doNothing().when(siteService).supprimerSite(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/sites/1"))
                .andExpect(status().isNoContent());

        verify(siteService, times(1)).supprimerSite(1L);
    }
}
