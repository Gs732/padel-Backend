CREATE TABLE fermeture (
    id             BIGSERIAL PRIMARY KEY,
    site_id        BIGINT,
    date_fermeture DATE NOT NULL,
    motif          VARCHAR(200),
    CONSTRAINT fk_fermeture_site FOREIGN KEY (site_id) REFERENCES site (id),
    CONSTRAINT uq_fermeture UNIQUE NULLS NOT DISTINCT (site_id, date_fermeture)
);