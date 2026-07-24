CREATE TABLE rencontre (
    id              BIGSERIAL PRIMARY KEY,
    terrain_id      BIGINT NOT NULL,
    organisateur_id BIGINT NOT NULL,
    debut           TIMESTAMP NOT NULL,
    visibilite      VARCHAR(20) NOT NULL,
    statut          VARCHAR(20) NOT NULL,
    prix_total      DOUBLE PRECISION NOT NULL DEFAULT 60,
    CONSTRAINT fk_rencontre_terrain FOREIGN KEY (terrain_id) REFERENCES terrain (id),
    CONSTRAINT fk_rencontre_organisateur FOREIGN KEY (organisateur_id) REFERENCES membre (id),
    CONSTRAINT uq_rencontre_terrain_debut UNIQUE (terrain_id, debut)
);