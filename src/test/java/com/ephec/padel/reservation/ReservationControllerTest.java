package com.ephec.padel.reservation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
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

import com.ephec.padel.reservation.controller.ReservationController;
import com.ephec.padel.reservation.model.Reservation;
import com.ephec.padel.reservation.service.ReservationService;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @InjectMocks
    private ReservationController reservationController;

    @Mock
    private ReservationService reservationService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
        objectMapper = new ObjectMapper();
    }

    private final LocalDateTime debut = LocalDateTime.of(2026, 6, 1, 10, 0);
    private final LocalDateTime fin   = LocalDateTime.of(2026, 6, 1, 11, 0);

    // -------------------------------------------------------
    // GET /api/reservations
    // -------------------------------------------------------
    @Test
    void getAllReservations_should_return_200_with_list() throws Exception {
        // Arrange
        Reservation r1 = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        Reservation r2 = new Reservation(2L, 2L, 1L, debut, fin, 20.0, "EN_ATTENTE");
        when(reservationService.getAllReservations()).thenReturn(List.of(r1, r2));

        // Act & Assert
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].statut").value("CONFIRMEE"))
                .andExpect(jsonPath("$[1].statut").value("EN_ATTENTE"));
    }

    // -------------------------------------------------------
    // GET /api/reservations/{id}
    // -------------------------------------------------------
    @Test
    void getReservationById_should_return_200_when_exists() throws Exception {
        // Arrange
        Reservation r = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        when(reservationService.getReservationById(1L)).thenReturn(r);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"));
    }

    // -------------------------------------------------------
    // GET /api/reservations/membre/{membreId}
    // -------------------------------------------------------
    @Test
    void getReservationsByMembreId_should_return_200_with_list() throws Exception {
        // Arrange
        Reservation r1 = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        Reservation r2 = new Reservation(2L, 1L, 2L, debut, fin, 20.0, "EN_ATTENTE");
        when(reservationService.getReservationsByMembreId(1L)).thenReturn(List.of(r1, r2));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/membre/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].membreId").value(1));
    }

    // -------------------------------------------------------
    // GET /api/reservations/terrain/{terrainId}
    // -------------------------------------------------------
    @Test
    void getReservationsByTerrainId_should_return_200_with_list() throws Exception {
        // Arrange
        Reservation r1 = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        Reservation r2 = new Reservation(2L, 2L, 1L, debut, fin, 20.0, "EN_ATTENTE");
        when(reservationService.getReservationsByTerrainId(1L)).thenReturn(List.of(r1, r2));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/terrain/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].terrainId").value(1));
    }

    // -------------------------------------------------------
    // POST /api/reservations
    // -------------------------------------------------------
    @Test
    void creerReservation_should_return_201_with_reservation() throws Exception {
        // Arrange
        Reservation r = new Reservation(null, 1L, 1L, debut, fin, 20.0, null);
        Reservation saved = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "EN_ATTENTE");
        when(reservationService.creerReservation(any(Reservation.class))).thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE"));
    }

    // -------------------------------------------------------
    // PUT /api/reservations/{id}/annuler
    // -------------------------------------------------------
    @Test
    void annulerReservation_should_return_204() throws Exception {
        // Arrange
        doNothing().when(reservationService).annulerReservation(1L);

        // Act & Assert
        mockMvc.perform(put("/api/reservations/1/annuler"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).annulerReservation(1L);
    }

    // -------------------------------------------------------
    // DELETE /api/reservations/{id}
    // -------------------------------------------------------
    @Test
    void supprimerReservation_should_return_204() throws Exception {
        // Arrange
        doNothing().when(reservationService).supprimerReservation(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).supprimerReservation(1L);
    }
}
