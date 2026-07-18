CREATE TABLE site (
    id        BIGSERIAL PRIMARY KEY,
    nom       VARCHAR(100) NOT NULL,
    adresse   VARCHAR(200),
    ville     VARCHAR(100),
    telephone VARCHAR(30),
    actif     BOOLEAN NOT NULL DEFAULT TRUE
);