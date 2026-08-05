package com.ephec.padel.rencontre.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ephec.padel.AbstractUnitTest;
import com.ephec.padel.creneau.model.CreneauDTO;
import com.ephec.padel.creneau.service.CreneauService;
import com.ephec.padel.exception.BadRequestException;
import com.ephec.padel.exception.NotFoundException;
import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.model.TypeMembre;
import com.ephec.padel.membre.repository.MembreRepository;
import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.model.Visibilite;
import com.ephec.padel.rencontre.repository.RencontreRepository;
import com.ephec.padel.site.model.Site;
import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.repository.TerrainRepository;

class RencontreServiceTest extends AbstractUnitTest {

    @Mock private RencontreRepository rencontreRepository;
    @Mock private MembreRepository membreRepository;
    @Mock private TerrainRepository terrainRepository;
    @Mock private CreneauService creneauService;
    @Mock private com.ephec.padel.participation.repository.ParticipationRepository participationRepository;

    @InjectMocks private RencontreService rencontreService;

    private Membre organisateur;
    private Terrain terrain;
    private Site site;
    private LocalDateTime creneauFutur;

    @BeforeEach
    void setUp() {
        // Arrange commun : un site, un terrain, un membre GLOBAL, un créneau loin dans le futur
        site = new Site();
        site.setId(1L);
        site.setNom("Padel Anderlecht");

        terrain = new Terrain();
        terrain.setId(1L);
        terrain.setNom("Court 1");
        terrain.setSite(site);

        organisateur = new Membre();
        organisateur.setId(1L);
        organisateur.setMatricule("G0001");
        organisateur.setType(TypeMembre.GLOBAL);
        organisateur.setSolde(0);

        // 60 jours dans le futur -> au-delà des 21 jours du GLOBAL
        creneauFutur = LocalDateTime.now().plusDays(60).withHour(8).withMinute(0).withSecond(0).withNano(0);
    }

    @Nested
    class CreationReussie {

        @Test
        void creerRencontre_shouldSucceed_whenAllRulesPass() {
            // Arrange
            when(membreRepository.findById(1L)).thenReturn(Optional.of(organisateur));
            when(terrainRepository.findById(1L)).thenReturn(Optional.of(terrain));
            when(creneauService.getCreneaux(1L, creneauFutur.toLocalDate()))
                    .thenReturn(List.of(new CreneauDTO(creneauFutur, creneauFutur.plusMinutes(90), true)));
            when(rencontreRepository.save(any(Rencontre.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Rencontre result = rencontreService.creerRencontre(1L, 1L, creneauFutur, Visibilite.PRIVE);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getOrganisateur()).isEqualTo(organisateur);
            assertThat(result.getTerrain()).isEqualTo(terrain);
            assertThat(result.getVisibilite()).isEqualTo(Visibilite.PRIVE);
        }
    }

    @Nested
    class ReglesMetier {

        @Test
        void creerRencontre_shouldThrow_whenDelaiTropCourt() {
            // Arrange : un créneau dans 5 jours seulement (< 21 pour un GLOBAL)
            LocalDateTime creneauProche = LocalDateTime.now().plusDays(5).withHour(8).withMinute(0);
            when(membreRepository.findById(1L)).thenReturn(Optional.of(organisateur));
            when(terrainRepository.findById(1L)).thenReturn(Optional.of(terrain));

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> rencontreService.creerRencontre(1L, 1L, creneauProche, Visibilite.PRIVE))
                    .withMessageContaining("21 jours");
        }

        @Test
        void creerRencontre_shouldThrow_whenSoldeNegatif() {
            // Arrange : membre avec solde dû
            organisateur.setSolde(-15);
            when(membreRepository.findById(1L)).thenReturn(Optional.of(organisateur));
            when(terrainRepository.findById(1L)).thenReturn(Optional.of(terrain));

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> rencontreService.creerRencontre(1L, 1L, creneauFutur, Visibilite.PRIVE))
                    .withMessageContaining("solde");
        }

        @Test
        void creerRencontre_shouldThrow_whenMembreSiteReserveHorsSonSite() {
            // Arrange : un membre SITE rattaché au site 2, qui tente de réserver sur le site 1
            Site siteDuMembre = new Site();
            siteDuMembre.setId(2L);
            organisateur.setType(TypeMembre.SITE);
            organisateur.setMatricule("S0001");
            organisateur.setSite(siteDuMembre);

            when(membreRepository.findById(1L)).thenReturn(Optional.of(organisateur));
            when(terrainRepository.findById(1L)).thenReturn(Optional.of(terrain)); // terrain sur site 1

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> rencontreService.creerRencontre(1L, 1L, creneauFutur, Visibilite.PRIVE))
                    .withMessageContaining("son site");
        }

        @Test
        void creerRencontre_shouldThrow_whenCreneauNonDisponible() {
            // Arrange : le créneau demandé est occupé (disponible = false)
            when(membreRepository.findById(1L)).thenReturn(Optional.of(organisateur));
            when(terrainRepository.findById(1L)).thenReturn(Optional.of(terrain));
            when(creneauService.getCreneaux(1L, creneauFutur.toLocalDate()))
                    .thenReturn(List.of(new CreneauDTO(creneauFutur, creneauFutur.plusMinutes(90), false)));

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> rencontreService.creerRencontre(1L, 1L, creneauFutur, Visibilite.PRIVE))
                    .withMessageContaining("creneau");
        }

        @Test
        void creerRencontre_shouldThrow_whenMembrePenalise() {
            // Arrange : membre pénalisé jusqu'à demain
            organisateur.setPenaliseJusquAu(java.time.LocalDate.now().plusDays(1));
            when(membreRepository.findById(1L)).thenReturn(Optional.of(organisateur));
            when(terrainRepository.findById(1L)).thenReturn(Optional.of(terrain));

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> rencontreService.creerRencontre(1L, 1L, creneauFutur, Visibilite.PRIVE))
                    .withMessageContaining("penalise");
        }
    }

    @Nested
    class RessourceIntrouvable {

        @Test
        void creerRencontre_shouldThrow_whenMembreIntrouvable() {
            // Arrange
            when(membreRepository.findById(99L)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> rencontreService.creerRencontre(99L, 1L, creneauFutur, Visibilite.PRIVE))
                    .withMessageContaining("Membre introuvable");
        }
    }
}