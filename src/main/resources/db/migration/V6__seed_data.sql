-- ========== SITES ==========
INSERT INTO site (nom, adresse, ville, telephone, actif) VALUES
    ('Padel Anderlecht', 'Rue du Sport 12', 'Anderlecht', '024561234', TRUE),
    ('Padel Uccle',      'Avenue Brugmann 250', 'Uccle',    '023751122', TRUE),
    ('Padel Namur',      'Chaussée de Liège 40', 'Namur',   '081223344', FALSE);

-- ========== HORAIRES (annee civile en cours) ==========
INSERT INTO horaire (site_id, annee, heure_debut, heure_fin) VALUES
    (1, EXTRACT(YEAR FROM CURRENT_DATE), '08:00', '22:00'),
    (2, EXTRACT(YEAR FROM CURRENT_DATE), '09:00', '23:00'),
    (3, EXTRACT(YEAR FROM CURRENT_DATE), '10:00', '20:00');

-- ========== TERRAINS ==========
INSERT INTO terrain (nom, type, interieur, actif, site_id) VALUES
    ('Court 1', 'Panoramique', TRUE,  TRUE, 1),
    ('Court 2', 'Mur plein',   FALSE, TRUE, 1),
    ('Court 3', 'Panoramique', TRUE,  TRUE, 1),
    ('Court A', 'Panoramique', TRUE,  TRUE, 2),
    ('Court B', 'Mur plein',   FALSE, TRUE, 2),
    ('Court Central', 'Panoramique', TRUE, TRUE, 3);

-- ========== MEMBRES ==========
-- GLOBAL (Gxxxx) : reservent partout, pas de site
INSERT INTO membre (matricule, nom, prenom, email, telephone, solde, actif, type, site_id) VALUES
    ('G0001', 'Dupont',  'Jean',   'jean.dupont@mail.be',  '0470111222', 0,  TRUE, 'GLOBAL', NULL),
    ('G0002', 'Lambert', 'Marie',  'marie.lambert@mail.be','0470333444', 15, TRUE, 'GLOBAL', NULL);

-- SITE (Sxxxx) : rattaches a un site
INSERT INTO membre (matricule, nom, prenom, email, telephone, solde, actif, type, site_id) VALUES
    ('S0001', 'Martin',  'Luc',    'luc.martin@mail.be',   '0470555666', 0,  TRUE, 'SITE', 1),
    ('S0002', 'Petit',   'Sophie', 'sophie.petit@mail.be', '0470777888', 0,  TRUE, 'SITE', 2);

-- LIBRE (Lxxxx) : reservent partout, pas de site
INSERT INTO membre (matricule, nom, prenom, email, telephone, solde, actif, type, site_id) VALUES
    ('L0001', 'Bernard', 'Paul',   'paul.bernard@mail.be', '0470999000', 0,  TRUE, 'LIBRE', NULL);