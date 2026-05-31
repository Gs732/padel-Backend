package com.ephec.padel.site;

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

import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.repository.SiteRepository;
import com.ephec.padel.site.service.SiteService;

@ExtendWith(MockitoExtension.class)
class SiteServiceTest {

    @Mock
    private SiteRepository siteRepository;

    @InjectMocks
    private SiteService siteService;

    // -------------------------------------------------------
    // getAllSites
    // -------------------------------------------------------
    @Test
    void getAllSites_should_return_all_sites() {
        // Arrange
        Site s1 = new Site(1L, "Padel Club", "Rue de la Paix 1", "Bruxelles", "0456123456", true);
        Site s2 = new Site(2L, "Padel Arena", "Avenue Louise 10", "Bruxelles", "0478987654", true);
        when(siteRepository.findAll()).thenReturn(List.of(s1, s2));

        // Act
        List<Site> result = siteService.getAllSites();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(s1, s2);
        verify(siteRepository, times(1)).findAll();
    }

    // -------------------------------------------------------
    // getSiteById
    // -------------------------------------------------------
    @Test
    void getSiteById_should_return_site_when_exists() {
        // Arrange
        Site s = new Site(1L, "Padel Club", "Rue de la Paix 1", "Bruxelles", "0456123456", true);
        when(siteRepository.findById(1L)).thenReturn(Optional.of(s));

        // Act
        Site result = siteService.getSiteById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Padel Club");
        verify(siteRepository, times(1)).findById(1L);
    }

    @Test
    void getSiteById_should_throw_exception_when_not_exists() {
        // Arrange
        when(siteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> siteService.getSiteById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
        verify(siteRepository, times(1)).findById(99L);
    }

    // -------------------------------------------------------
    // creerSite
    // -------------------------------------------------------
    @Test
    void creerSite_should_save_and_return_site() {
        // Arrange
        Site s = new Site(null, "Padel Club", "Rue de la Paix 1", "Bruxelles", "0456123456", true);
        Site saved = new Site(1L, "Padel Club", "Rue de la Paix 1", "Bruxelles", "0456123456", true);
        when(siteRepository.save(s)).thenReturn(saved);

        // Act
        Site result = siteService.creerSite(s);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(siteRepository, times(1)).save(s);
    }

    // -------------------------------------------------------
    // supprimerSite
    // -------------------------------------------------------
    @Test
    void supprimerSite_should_delete_when_exists() {
        // Arrange
        when(siteRepository.existsById(1L)).thenReturn(true);

        // Act
        siteService.supprimerSite(1L);

        // Assert
        verify(siteRepository, times(1)).deleteById(1L);
    }

    @Test
    void supprimerSite_should_throw_exception_when_not_exists() {
        // Arrange
        when(siteRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> siteService.supprimerSite(99L))
                .isInstanceOf(RuntimeException.class);
        verify(siteRepository, never()).deleteById(any());
    }
}
