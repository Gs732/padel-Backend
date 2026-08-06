package com.ephec.padel.paiement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
import com.ephec.padel.paiement.dto.PaiementResponse;
import com.ephec.padel.paiement.model.Paiement;
import com.ephec.padel.paiement.repository.PaiementRepository;
import com.ephec.padel.participation.model.Participation;
import com.ephec.padel.participation.repository.ParticipationRepository;
import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.model.StatutRencontre;
import com.ephec.padel.rencontre.repository.RencontreRepository;

class PaiementServiceTest extends AbstractUnitTest {

    @Mock private PaiementRepository paiementRepository;
    @Mock private ParticipationRepository participationRepository;
    @Mock private RencontreRepository rencontreRepository;

    @InjectMocks private PaiementService paiementService;

    private Rencontre rencontre;
    private Membre membre;
    private Participation participation;

    @BeforeEach
    void setUp() {
        rencontre = new Rencontre();
        rencontre.setId(1L);
        rencontre.setPrixTotal(60);
        rencontre.setStatut(StatutRencontre.OUVERTE);

        membre = new Membre();
        membre.setId(2L);
        membre.setNom("Lambert");
        membre.setPrenom("Marie");

        participation = new Participation();
        participation.setId(10L);
        participation.setRencontre(rencontre);
        participation.setMembre(membre);
        participation.setPaye(false);
    }

    @Nested
    class Reussite {

        @Test
        void payer_shouldCreatePaiementOf15_whenParticipationValid() {
            // Arrange
            when(participationRepository.findById(10L)).thenReturn(Optional.of(participation));
            when(paiementRepository.save(any(Paiement.class))).thenAnswer(inv -> inv.getArgument(0));
            when(participationRepository.countByRencontre_IdAndPayeTrue(1L)).thenReturn(1L); // pas encore complet

            // Act
            PaiementResponse result = paiementService.payer(10L);

            // Assert : 60 / 4 = 15 EUR
            assertThat(result).isNotNull();
            assertThat(result.montant()).isEqualTo(15.0);
            assertThat(participation.isPaye()).isTrue();
        }

        @Test
        void payer_shouldNotSwitchToComplete_whenLessThan4Payes() {
            // Arrange : seulement 2 payés après ce paiement
            when(participationRepository.findById(10L)).thenReturn(Optional.of(participation));
            when(paiementRepository.save(any(Paiement.class))).thenAnswer(inv -> inv.getArgument(0));
            when(participationRepository.countByRencontre_IdAndPayeTrue(1L)).thenReturn(2L);

            // Act
            paiementService.payer(10L);

            // Assert : le statut ne bascule PAS, donc rencontreRepository.save n'est jamais appelé
            assertThat(rencontre.getStatut()).isEqualTo(StatutRencontre.OUVERTE);
            verify(rencontreRepository, never()).save(any(Rencontre.class));
        }

        @Test
        void payer_shouldSwitchToComplete_when4Payes() {
            // Arrange : ce paiement est le 4e
            when(participationRepository.findById(10L)).thenReturn(Optional.of(participation));
            when(paiementRepository.save(any(Paiement.class))).thenAnswer(inv -> inv.getArgument(0));
            when(participationRepository.countByRencontre_IdAndPayeTrue(1L)).thenReturn(4L);

            // Act
            paiementService.payer(10L);

            // Assert : le match bascule en COMPLETE et est sauvegardé
            assertThat(rencontre.getStatut()).isEqualTo(StatutRencontre.COMPLETE);
            verify(rencontreRepository).save(rencontre);
        }
    }

    @Nested
    class ReglesMetier {

        @Test
        void payer_shouldThrow_whenParticipationDejaPayee() {
            // Arrange
            participation.setPaye(true);
            when(participationRepository.findById(10L)).thenReturn(Optional.of(participation));

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> paiementService.payer(10L))
                    .withMessageContaining("deja payee");
        }
    }

    @Nested
    class RessourceIntrouvable {

        @Test
        void payer_shouldThrow_whenParticipationIntrouvable() {
            // Arrange
            when(participationRepository.findById(99L)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> paiementService.payer(99L))
                    .withMessageContaining("Participation introuvable");
        }
    }
}