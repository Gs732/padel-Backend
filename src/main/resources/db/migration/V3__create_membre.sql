CREATE TABLE membre (
    id        BIGSERIAL PRIMARY KEY,
    matricule VARCHAR(20) NOT NULL UNIQUE,
    nom       VARCHAR(100) NOT NULL,
    prenom    VARCHAR(100) NOT NULL,
    email     VARCHAR(150),
    telephone VARCHAR(30),
    solde     DOUBLE PRECISION NOT NULL DEFAULT 0,
    actif     BOOLEAN NOT NULL DEFAULT TRUE,
    type      VARCHAR(20) NOT NULL,
    site_id   BIGINT,
    CONSTRAINT fk_membre_site FOREIGN KEY (site_id) REFERENCES site (id)
);