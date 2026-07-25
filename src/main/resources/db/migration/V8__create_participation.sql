CREATE TABLE participation (
    id           BIGSERIAL PRIMARY KEY,
    rencontre_id BIGINT NOT NULL,
    membre_id    BIGINT NOT NULL,
    paye         BOOLEAN NOT NULL DEFAULT FALSE,
    date_inscription TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_participation_rencontre FOREIGN KEY (rencontre_id) REFERENCES rencontre (id),
    CONSTRAINT fk_participation_membre FOREIGN KEY (membre_id) REFERENCES membre (id),
    CONSTRAINT uq_participation UNIQUE (rencontre_id, membre_id)
);