package com.ephec.padel.membre;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ephec.padel.membre.controller.MembreController;
import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.service.MembreService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(MembreController.class)
class MembreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MembreService membreService;

    // -------------------------------------------------------
    // GET /api/membres
    // -------------------------------------------------------
    @Test
    void getAllMembres_should_return_200_with_list() throws Exception {
        // Arrange
        Membre m1 = new Membre(1L, "Sako", "Georges", "g@gmail.com", "0467", 10.0, true);
        Membre m2 = new Membre(2L, "Doe", "John", "j@gmail.com", "0477", 5.0, true);
        when(membreService.getAllMembres()).thenReturn(List.of(m1, m2));

        // Act & Assert
        mockMvc.perform(get("/api/membres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nom").value("Sako"))
                .andExpect(jsonPath("$[1].nom").value("Doe"));
    }

    // -------------------------------------------------------
    // GET /api/membres/{id}
    // -------------------------------------------------------
    @Test
    void getMembreById_should_return_200_when_exists() throws Exception {
        // Arrange
        Membre m = new Membre(1L, "Sako", "Georges", "g@gmail.com", "0467", 10.0, true);
        when(membreService.getMembreById(1L)).thenReturn(m);

        // Act & Assert
        mockMvc.perform(get("/api/membres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Sako"))
                .andExpect(jsonPath("$.prenom").value("Georges"));
    }

    // -------------------------------------------------------
    // POST /api/membres
    // -------------------------------------------------------
    @Test
    void creerMembre_should_return_201_with_membre() throws Exception {
        // Arrange
        Membre m = new Membre(null, "Sako", "Georges", "g@gmail.com", "0467", 10.0, true);
        Membre saved = new Membre(1L, "Sako", "Georges", "g@gmail.com", "0467", 10.0, true);
        when(membreService.creerMembre(any(Membre.class))).thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/membres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(m)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Sako"));
    }

    // -------------------------------------------------------
    // DELETE /api/membres/{id}
    // -------------------------------------------------------
    @Test
    void supprimerMembre_should_return_204() throws Exception {
        // Arrange
        doNothing().when(membreService).supprimerMembre(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/membres/1"))
                .andExpect(status().isNoContent());

        verify(membreService, times(1)).supprimerMembre(1L);
    }
}