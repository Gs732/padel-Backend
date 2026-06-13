package com.ephec.padel.membre.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ephec.padel.membre.model.Membre;
import com. ephec.padel.membre.repository.MembreRepository;


@Service
public class MembreService {
    private final MembreRepository membreRepository;

    public MembreService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    public List<Membre> getAllMembres() {
        return membreRepository.findAll();
    }

     public Membre getMembreById(Long id) {
        return membreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "Membre introuvable avec id : " + id));
    }

    public Membre creerMembre(Membre membre){
        // Générer le matricule automatiquement selon le type 
        String matricule = genererMatricule(membre.getType());
        membre.setMatricule(matricule);
        return membreRepository.save(membre);
    }

    public Membre modifierMembre(Long id, Membre membre) {
        if (!membreRepository.existsById(id)) {
            throw new RuntimeException("Membre introuvable avec id : " + id);
        }
        membre.setId(id);
        return membreRepository.save(membre);
    }

    public void supprimerMembre(Long id) {
        if (!membreRepository.existsById(id)){
            throw new RuntimeException("Membre introuvable avec id : " + id);
        }
        membreRepository.deleteById(id);
    }

    // Génération automatique du matricule
    private String genererMatricule(Membre.TypeMembre type) {
        long count = membreRepository.countByType(type) +1; // On compte le nombre de membres du même type pour générer un matricule unique
        String numero = String.format("%04d", count);

       return switch (type) {
         case GLOBAL -> "G" + numero;  
         case SITE -> "S" + numero;
         case LIBRE -> "L" + numero;
};
    }

}
