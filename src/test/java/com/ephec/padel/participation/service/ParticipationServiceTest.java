package com.ephec.padel.participation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ephec.padel.AbstractUnitTest;
import com.ephec.padel.exception.BadRequestException;
import com.ephec.padel.exception.NotFoundException;
import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.repository.MembreRepository;
import com.ephec.padel.participation.dto.ParticipationResponse;
import com.ephec.padel.participation.model.Participation;
import com.ephec.padel.participation.repository.ParticipationRepository;
import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.repository.RencontreRepository;

class ParticipationServiceTest extends AbstractUnitTest {

    @Mock private ParticipationRepository participationRepository;
    @Mock private RencontreRepository rencontreRepository;
    @Mock private MembreRepository membreRepository;

    @InjectMocks private ParticipationService participationService;

    private Rencontre rencontre;
    private Membre membre;

    @BeforeEach
    void setUp() {
        rencontre = new Rencontre();
        rencontre.setId(1L);

        membre = new Membre();
        membre.setId(2L);
        membre.setMatricule("G0002");
        membre.setNom("Lambert");
        membre.setPrenom("Marie");
        membre.setSolde(0);
    }

    @Nested
    class Reussite {

        @Test
        void rejoindre_shouldSucceed_whenMatchNotFullAndMembreNotAlreadyIn() {
            // Arrange
            when(rencontreRepository.findById(1L)).thenReturn(Optional.of(rencontre));
            when(membreRepository.findById(2L)).thenReturn(Optional.of(membre));
            when(participationRepository.countByRencontre_Id(1L)).thenReturn(2L); // 2/4
            when(participationRepository.existsByRencontre_IdAndMembre_Id(1L, 2L)).thenReturn(false);
            when(participationRepository.save(any(Participation.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            // Act
            ParticipationResponse result = participationService.rejoindre(1L, 2L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.membreNom()).contains("Lambert");
            assertThat(result.paye()).isFalse();
        }
    }

    @Nested
    class ReglesMetier {

        @Test
        void rejoindre_shouldThrow_whenMatchIsFull() {
            // Arrange : déjà 4 joueurs
            when(rencontreRepository.findById(1L)).thenReturn(Optional.of(rencontre));
            when(membreRepository.findById(2L)).thenReturn(Optional.of(membre));
            when(participationRepository.countByRencontre_Id(1L)).thenReturn(4L);

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> participationService.rejoindre(1L, 2L))
                    .withMessageContaining("complet");
        }

        @Test
        void rejoindre_shouldThrow_whenMembreAlreadyParticipates() {
            // Arrange : le membre est déjà inscrit
            when(rencontreRepository.findById(1L)).thenReturn(Optional.of(rencontre));
            when(membreRepository.findById(2L)).thenReturn(Optional.of(membre));
            when(participationRepository.countByRencontre_Id(1L)).thenReturn(2L);
            when(participationRepository.existsByRencontre_IdAndMembre_Id(1L, 2L)).thenReturn(true);

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> participationService.rejoindre(1L, 2L))
                    .withMessageContaining("participe deja");
        }

        @Test
        void rejoindre_shouldThrow_whenMembreHasNegativeSolde() {
            // Arrange : solde dû
            membre.setSolde(-15);
            when(rencontreRepository.findById(1L)).thenReturn(Optional.of(rencontre));
            when(membreRepository.findById(2L)).thenReturn(Optional.of(membre));
            when(participationRepository.countByRencontre_Id(1L)).thenReturn(2L);
            when(participationRepository.existsByRencontre_IdAndMembre_Id(1L, 2L)).thenReturn(false);

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> participationService.rejoindre(1L, 2L))
                    .withMessageContaining("solde");
        }
    }

    @Nested
    class RessourceIntrouvable {

        @Test
        void rejoindre_shouldThrow_whenRencontreIntrouvable() {
            // Arrange
            when(rencontreRepository.findById(99L)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> participationService.rejoindre(99L, 2L))
                    .withMessageContaining("Rencontre introuvable");
        }
    }
}