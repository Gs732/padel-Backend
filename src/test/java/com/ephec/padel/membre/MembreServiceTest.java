package com.ephec.padel.membre;

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

import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.repository.MembreRepository;
import com.ephec.padel.membre.service.MembreService;

@ExtendWith(MockitoExtension.class)
class MembreServiceTest {

    @Mock
    private MembreRepository membreRepository;

    @InjectMocks
    private MembreService membreService;

    // -------------------------------------------------------
    // getAllMembres
    // -------------------------------------------------------
    @Test
    void getAllMembres_should_return_all_membres() {
        // Arrange
        Membre m1 = new Membre(1L, "Sako", "Georges", "g@gmail.com", "0467", 10.0, true);
        Membre m2 = new Membre(2L, "Doe", "John", "j@gmail.com", "0477", 5.0, true);
        when(membreRepository.findAll()).thenReturn(List.of(m1, m2));

        // Act
        List<Membre> result = membreService.getAllMembres();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(m1, m2);
        verify(membreRepository, times(1)).findAll();
    }

    // -------------------------------------------------------
    // getMembreById
    // -------------------------------------------------------
    @Test
    void getMembreById_should_return_membre_when_exists() {
        // Arrange
        Membre m = new Membre(1L, "Sako", "Georges", "g@gmail.com", "0467", 10.0, true);
        when(membreRepository.findById(1L)).thenReturn(Optional.of(m));

        // Act
        Membre result = membreService.getMembreById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Sako");
        verify(membreRepository, times(1)).findById(1L);
    }

    @Test
    void getMembreById_should_throw_exception_when_not_exists() {
        // Arrange
        when(membreRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> membreService.getMembreById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
        verify(membreRepository, times(1)).findById(99L);
    }

    // -------------------------------------------------------
    // creerMembre
    // -------------------------------------------------------
    @Test
    void creerMembre_should_save_and_return_membre() {
        // Arrange
        Membre m = new Membre(null, "Sako", "Georges", "g@gmail.com", "0467", 10.0, true);
        Membre saved = new Membre(1L, "Sako", "Georges", "g@gmail.com", "0467", 10.0, true);
        when(membreRepository.save(m)).thenReturn(saved);

        // Act
        Membre result = membreService.creerMembre(m);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(membreRepository, times(1)).save(m);
    }

    // -------------------------------------------------------
    // supprimerMembre
    // -------------------------------------------------------
    @Test
    void supprimerMembre_should_delete_when_exists() {
        // Arrange
        when(membreRepository.existsById(1L)).thenReturn(true);

        // Act
        membreService.supprimerMembre(1L);

        // Assert
        verify(membreRepository, times(1)).deleteById(1L);
    }

    @Test
    void supprimerMembre_should_throw_exception_when_not_exists() {
        // Arrange
        when(membreRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> membreService.supprimerMembre(99L))
                .isInstanceOf(RuntimeException.class);
        verify(membreRepository, never()).deleteById(any());
    }
}