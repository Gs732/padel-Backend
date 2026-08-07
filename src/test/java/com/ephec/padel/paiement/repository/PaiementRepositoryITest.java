package com.ephec.padel.paiement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ephec.padel.membre.model.Membre;
import com.ephec.padel.membre.model.TypeMembre;
import com.ephec.padel.membre.repository.MembreRepository;
import com.ephec.padel.paiement.model.Paiement;
import com.ephec.padel.participation.model.Participation;
import com.ephec.padel.participation.repository.ParticipationRepository;
import com.ephec.padel.rencontre.model.Rencontre;
import com.ephec.padel.rencontre.model.StatutRencontre;
import com.ephec.padel.rencontre.model.Visibilite;
import com.ephec.padel.rencontre.repository.RencontreRepository;
import com.ephec.padel.site.model.Site;
import com.ephec.padel.site.repository.SiteRepository;
import com.ephec.padel.terrain.model.Terrain;
import com.ephec.padel.terrain.repository.TerrainRepository;

@SpringBootTest
@Transactional
class PaiementRepositoryITest {

    @Autowired private PaiementRepository paiementRepository;
    @Autowired private ParticipationRepository participationRepository;
    @Autowired private RencontreRepository rencontreRepository;
    @Autowired private MembreRepository membreRepository;
    @Autowired private TerrainRepository terrainRepository;
    @Autowired private SiteRepository siteRepository;

    @Test
    void findByParticipationRencontreTerrainSiteId_shouldReturnPaiements_forGivenSite() {
        // Arrange : on construit toute la chaîne via les repositories
        Site site = new Site();
        site.setNom("Site Test CA");
        site.setActif(true);
        site = siteRepository.save(site);

        Terrain terrain = new Terrain();
        terrain.setNom("Court Test");
        terrain.setInterieur(true);
        terrain.setActif(true);
        terrain.setSite(site);
        terrain = terrainRepository.save(terrain);

        Membre membre = new Membre();
        membre.setMatricule("G9001");
        membre.setNom("Test");
        membre.setPrenom("Joueur");
        membre.setType(TypeMembre.GLOBAL);
        membre.setSolde(0);
        membre.setActif(true);
        membre = membreRepository.save(membre);

        Rencontre rencontre = new Rencontre();
        rencontre.setTerrain(terrain);
        rencontre.setOrganisateur(membre);
        rencontre.setDebut(LocalDateTime.now().plusDays(30));
        rencontre.setVisibilite(Visibilite.PRIVE);
        rencontre.setStatut(StatutRencontre.OUVERTE);
        rencontre.setPrixTotal(60);
        rencontre = rencontreRepository.save(rencontre);

        Participation participation = new Participation();
        participation.setRencontre(rencontre);
        participation.setMembre(membre);
        participation.setPaye(true);
        participation.setDateInscription(LocalDateTime.now());
        participation = participationRepository.save(participation);

        Paiement paiement = new Paiement();
        paiement.setParticipation(participation);
        paiement.setMontant(15);
        paiement.setDatePaiement(LocalDateTime.now());
        paiementRepository.save(paiement);

        // Act : la requête à 4 relations
        List<Paiement> resultats =
                paiementRepository.findByParticipation_Rencontre_Terrain_Site_Id(site.getId());

        // Assert
        assertThat(resultats).hasSize(1);
        assertThat(resultats.get(0).getMontant()).isEqualTo(15.0);
    }
}