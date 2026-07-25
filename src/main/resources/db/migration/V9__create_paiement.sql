CREATE TABLE paiement (
    id               BIGSERIAL PRIMARY KEY,
    participation_id BIGINT NOT NULL UNIQUE,
    montant          DOUBLE PRECISION NOT NULL,
    date_paiement    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_paiement_participation FOREIGN KEY (participation_id) REFERENCES participation (id)
);