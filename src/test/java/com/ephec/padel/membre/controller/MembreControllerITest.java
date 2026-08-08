package com.ephec.padel.membre.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ephec.padel.membre.dto.MembreResponse;
import com.ephec.padel.membre.model.TypeMembre;
import com.ephec.padel.membre.service.MembreService;

@SpringBootTest
class MembreControllerITest {

    @Autowired private WebApplicationContext context;

    @MockitoBean private MembreService membreService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getAllMembres_shouldReturn200AndList_whenAuthenticated() throws Exception {
        MembreResponse membre = new MembreResponse(
                1L, "G0001", "Dupont", "Jean", "jean@mail.be",
                "0470111222", 0, true, TypeMembre.GLOBAL, null);
        when(membreService.getAllResponses()).thenReturn(List.of(membre));

        mockMvc.perform(get("/api/membres").with(user("admin").roles("ADMIN_GLOBAL")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matricule").value("G0001"))
                .andExpect(jsonPath("$[0].nom").value("Dupont"));
    }

    @Test
    void getAllMembres_shouldReturn403_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/membres"))
                .andExpect(status().isForbidden());
    }
}