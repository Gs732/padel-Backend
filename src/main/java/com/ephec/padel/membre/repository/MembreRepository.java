package com.ephec.padel.membre.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ephec.padel.membre.model.Membre;

@Repository
public class MembreRepository {
    private final Map<Long, Membre> membres = new HashMap<>();
    private long nextId = 1;

    public List<Membre> findAll() {
        return new ArrayList<>(membres.values());
    }

    public Optional<Membre> findById(Long id) {
        return Optional.ofNullable(membres.get(id));
    }

    public Membre save(Membre membre) {
        if (membre.getId() == null) {
            membre.setId(nextId++);
        }
        membres.put(membre.getId(), membre); // On le sauvegard
        return membre;
    }

    public void deleteById(Long id) {
        membres.remove(id);
    }

    public boolean existsById (Long id) {
        return membres.containsKey(id);
    }
}
