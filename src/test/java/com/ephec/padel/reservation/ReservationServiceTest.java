package com.ephec.padel.reservation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.service.MembreService;
import com.ephec.padel.reservation.model.Reservation;
import com.ephec.padel.reservation.repository.ReservationRepository;
import com.ephec.padel.reservation.service.ReservationService;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MembreService membreService;

    @InjectMocks
    private ReservationService reservationService;

    private final LocalDateTime debut = LocalDateTime.of(2026, 6, 1, 10, 0);
    private final LocalDateTime fin   = LocalDateTime.of(2026, 6, 1, 11, 0);

    // -------------------------------------------------------
    // getAllReservations
    // -------------------------------------------------------
    @Test
    void getAllReservations_should_return_all_reservations() {
        // Arrange
        Reservation r1 = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        Reservation r2 = new Reservation(2L, 2L, 1L, debut, fin, 20.0, "EN_ATTENTE");
        when(reservationRepository.findAll()).thenReturn(List.of(r1, r2));

        // Act
        List<Reservation> result = reservationService.getAllReservations();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(r1, r2);
        verify(reservationRepository, times(1)).findAll();
    }

    // -------------------------------------------------------
    // getReservationById
    // -------------------------------------------------------
    @Test
    void getReservationById_should_return_reservation_when_exists() {
        // Arrange
        Reservation r = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));

        // Act
        Reservation result = reservationService.getReservationById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatut()).isEqualTo("CONFIRMEE");
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void getReservationById_should_throw_exception_when_not_exists() {
        // Arrange
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reservationService.getReservationById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
        verify(reservationRepository, times(1)).findById(99L);
    }

    // -------------------------------------------------------
    // getReservationsByMembreId
    // -------------------------------------------------------
    @Test
    void getReservationsByMembreId_should_return_reservations_for_membre() {
        // Arrange
        Reservation r1 = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        Reservation r2 = new Reservation(2L, 1L, 2L, debut, fin, 20.0, "EN_ATTENTE");
        when(reservationRepository.findByMembreId(1L)).thenReturn(List.of(r1, r2));

        // Act
        List<Reservation> result = reservationService.getReservationsByMembreId(1L);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getMembreId().equals(1L));
        verify(reservationRepository, times(1)).findByMembreId(1L);
    }

    // -------------------------------------------------------
    // getReservationsByTerrainId
    // -------------------------------------------------------
    @Test
    void getReservationsByTerrainId_should_return_reservations_for_terrain() {
        // Arrange
        Reservation r1 = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        Reservation r2 = new Reservation(2L, 2L, 1L, debut, fin, 20.0, "EN_ATTENTE");
        when(reservationRepository.findByTerrainId(1L)).thenReturn(List.of(r1, r2));

        // Act
        List<Reservation> result = reservationService.getReservationsByTerrainId(1L);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getTerrainId().equals(1L));
        verify(reservationRepository, times(1)).findByTerrainId(1L);
    }

    // -------------------------------------------------------
    // creerReservation
    // -------------------------------------------------------
    @Test
    void creerReservation_should_set_statut_EN_ATTENTE_and_save_when_solde_positif_and_delai_respecte() {
    // Arrange
    LocalDateTime debutFutur = LocalDateTime.now().plusDays(25); // > 21 jours pour GLOBAL
    LocalDateTime finFutur = debutFutur.plusHours(1);

    Reservation r = new Reservation(null, 1L, 1L, debutFutur, finFutur, 20.0, null);
    Reservation saved = new Reservation(1L, 1L, 1L, debutFutur, finFutur, 20.0, "EN_ATTENTE");
    Membre membre = new Membre(1L, "G0001", "Sako", "Georges", "g@gmail.com", "0467", 10.0, true, Membre.TypeMembre.GLOBAL);

    when(membreService.getMembreById(1L)).thenReturn(membre);
    when(reservationRepository.save(any(Reservation.class))).thenReturn(saved);

    // Act
    Reservation result = reservationService.creerReservation(r);

    // Assert
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getStatut()).isEqualTo("EN_ATTENTE");
    verify(reservationRepository, times(1)).save(any(Reservation.class));
}


    @Test
    void creerReservation_should_throw_exception_when_solde_negatif() {
    // Arrange
    LocalDateTime debutFutur = LocalDateTime.now().plusDays(25);
    LocalDateTime finFutur = debutFutur.plusHours(1);

    Reservation r = new Reservation(null, 1L, 1L, debutFutur, finFutur, 20.0, null);
    Membre membre = new Membre(1L, "G0001", "Sako", "Georges", "g@gmail.com", "0467", -15.0, true, Membre.TypeMembre.GLOBAL);

    when(membreService.getMembreById(1L)).thenReturn(membre);

    // Act & Assert
    assertThatThrownBy(() -> reservationService.creerReservation(r))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("solde dû");

    verify(reservationRepository, never()).save(any(Reservation.class));
}
    @Test
    void creerReservation_should_throw_exception_when_delai_non_respecte_global() {
    // Arrange : membre GLOBAL doit réserver 21 jours avant, ici seulement 5 jours
    LocalDateTime debutProche = LocalDateTime.now().plusDays(5);
    LocalDateTime finProche = debutProche.plusHours(1);

    Reservation r = new Reservation(null, 1L, 1L, debutProche, finProche, 20.0, null);
    Membre membre = new Membre(1L, "G0001", "Sako", "Georges", "g@gmail.com", "0467", 10.0, true, Membre.TypeMembre.GLOBAL);

    when(membreService.getMembreById(1L)).thenReturn(membre);

    // Act & Assert
    assertThatThrownBy(() -> reservationService.creerReservation(r))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("jours à l'avance");

    verify(reservationRepository, never()).save(any(Reservation.class));
}

    @Test
    void creerReservation_should_throw_exception_when_delai_non_respecte_libre() {
    // Arrange : membre LIBRE doit réserver 5 jours avant, ici seulement 2 jours
    LocalDateTime debutProche = LocalDateTime.now().plusDays(2);
    LocalDateTime finProche = debutProche.plusHours(1);

    Reservation r = new Reservation(null, 1L, 1L, debutProche, finProche, 20.0, null);
    Membre membre = new Membre(1L, "L0001", "Sako", "Georges", "g@gmail.com", "0467", 10.0, true, Membre.TypeMembre.LIBRE);

    when(membreService.getMembreById(1L)).thenReturn(membre);

    // Act & Assert
    assertThatThrownBy(() -> reservationService.creerReservation(r))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("jours à l'avance");

    verify(reservationRepository, never()).save(any(Reservation.class));
}
    // -------------------------------------------------------
    // annulerReservation
    // -------------------------------------------------------
    @Test
    void annulerReservation_should_set_statut_ANNULEE_and_save() {
        // Arrange
        Reservation r = new Reservation(1L, 1L, 1L, debut, fin, 20.0, "CONFIRMEE");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(r);

        // Act
        reservationService.annulerReservation(1L);

        // Assert
        assertThat(r.getStatut()).isEqualTo("ANNULEE");
        verify(reservationRepository, times(1)).save(r);
    }

    // -------------------------------------------------------
    // supprimerReservation
    // -------------------------------------------------------
    @Test
    void supprimerReservation_should_delete_when_exists() {
        // Arrange
        when(reservationRepository.existsById(1L)).thenReturn(true);

        // Act
        reservationService.supprimerReservation(1L);

        // Assert
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void supprimerReservation_should_throw_exception_when_not_exists() {
        // Arrange
        when(reservationRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> reservationService.supprimerReservation(99L))
                .isInstanceOf(RuntimeException.class);
        verify(reservationRepository, never()).deleteById(any());
    }
}