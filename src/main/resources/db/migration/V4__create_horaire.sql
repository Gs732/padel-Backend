CREATE TABLE horaire (
    id          BIGSERIAL PRIMARY KEY,
    site_id     BIGINT NOT NULL,
    annee       INTEGER NOT NULL,
    heure_debut TIME NOT NULL,
    heure_fin   TIME NOT NULL,
    CONSTRAINT fk_horaire_site FOREIGN KEY (site_id) REFERENCES site (id),
    CONSTRAINT uq_horaire_site_annee UNIQUE (site_id, annee),
    CONSTRAINT ck_horaire_plage CHECK (heure_fin > heure_debut)
);