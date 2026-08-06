package com.ephec.padel.membre.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ephec.padel.AbstractUnitTest;
import com.ephec.padel.exception.BadRequestException;
import com.ephec.padel.membre.dto.CreerMembreRequest;
import com.ephec.padel.membre.dto.MembreResponse;
import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.model.TypeMembre;
import com.ephec.padel.membre.repository.MembreRepository;
import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.repository.SiteRepository;

class MembreServiceTest extends AbstractUnitTest {

    @Mock private MembreRepository membreRepository;
    @Mock private SiteRepository siteRepository;

    @InjectMocks private MembreService membreService;

    @Nested
    class CreationReussie {

        @Test
        void creerMembre_shouldGenerateMatriculeG_whenTypeGlobal() {
            // Arrange
            CreerMembreRequest req = new CreerMembreRequest(
                    "Dupont", "Jean", "jean@mail.be", "0470111222", TypeMembre.GLOBAL, null);
            when(membreRepository.countByType(TypeMembre.GLOBAL)).thenReturn(0L);
            when(membreRepository.save(any(Membre.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            MembreResponse result = membreService.creerMembre(req);

            // Assert : matricule G0001, pas de site
            assertThat(result.matricule()).isEqualTo("G0001");
            assertThat(result.type()).isEqualTo(TypeMembre.GLOBAL);
            assertThat(result.siteNom()).isNull();
        }

        @Test
        void creerMembre_shouldAttachSite_whenTypeSite() {
            // Arrange
            Site site = new Site();
            site.setId(1L);
            site.setNom("Padel Anderlecht");
            CreerMembreRequest req = new CreerMembreRequest(
                    "Martin", "Luc", "luc@mail.be", "0470555666", TypeMembre.SITE, 1L);
            when(siteRepository.findById(1L)).thenReturn(Optional.of(site));
            when(membreRepository.countByType(TypeMembre.SITE)).thenReturn(0L);
            when(membreRepository.save(any(Membre.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            MembreResponse result = membreService.creerMembre(req);

            // Assert : matricule S0001, rattaché au site
            assertThat(result.matricule()).isEqualTo("S0001");
            assertThat(result.siteNom()).isEqualTo("Padel Anderlecht");
        }
    }

    @Nested
    class ValidationTypeSite {

        @Test
        void creerMembre_shouldThrow_whenTypeSiteWithoutSite() {
            // Arrange : SITE sans siteId
            CreerMembreRequest req = new CreerMembreRequest(
                    "Martin", "Luc", "luc@mail.be", "0470555666", TypeMembre.SITE, null);

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> membreService.creerMembre(req))
                    .withMessageContaining("SITE doit etre rattache");
        }

        @Test
        void creerMembre_shouldThrow_whenTypeGlobalWithSite() {
            // Arrange : GLOBAL avec un siteId (interdit)
            CreerMembreRequest req = new CreerMembreRequest(
                    "Dupont", "Jean", "jean@mail.be", "0470111222", TypeMembre.GLOBAL, 1L);

            // Act + Assert
            assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> membreService.creerMembre(req))
                    .withMessageContaining("ne peut pas etre rattache");
        }
    }
}