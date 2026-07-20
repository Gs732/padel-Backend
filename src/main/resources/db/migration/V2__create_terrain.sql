CREATE TABLE terrain (
    id        BIGSERIAL PRIMARY KEY,
    nom       VARCHAR(100) NOT NULL,
    type      VARCHAR(50),
    interieur BOOLEAN NOT NULL DEFAULT FALSE,
    actif     BOOLEAN NOT NULL DEFAULT TRUE,
    site_id   BIGINT NOT NULL,
    CONSTRAINT fk_terrain_site FOREIGN KEY (site_id) REFERENCES site (id)
);