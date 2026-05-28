package com.ephec.padel.reservation.repository;

import com.ephec.padel.reservation.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private long nextId = 1;

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> findByMembreId(Long membreId) {
        return reservations.values().stream()
                .filter(r -> r.getMembreId().equals(membreId))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByTerrainId(Long terrainId) {
        return reservations.values().stream()
                .filter(r -> r.getTerrainId().equals(terrainId))
                .collect(Collectors.toList());
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(nextId++);
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    public void deleteById(Long id) {
        reservations.remove(id);
    }

    public boolean existsById(Long id) {
        return reservations.containsKey(id);
    }
}