CREATE TABLE Utilisateur (
     id INT(10) PRIMARY KEY,
     nom VARCHAR(50),
     prenom VARCHAR(50),
     email VARCHAR(100),
     mdp VARCHAR(255),
     role ENUM('ADMIN', 'TECHNICIEN', 'CONDUCTEUR', 'OPERATEUR')
);

CREATE TABLE Technicien (
    id INT(10) PRIMARY KEY,
    specialite ENUM('MECANIQUE', 'ELECTRONIQUE', 'ELECTRIQUE', 'INFORMATIQUE', 'INFRASTRUCTURE'),
    FOREIGN KEY (id) REFERENCES Utilisateur(id)
);

CREATE TABLE Train (
   immatriculation VARCHAR(50) PRIMARY KEY,
   marque VARCHAR(50),
   modele VARCHAR(50)
);

CREATE TABLE Trajet (
    id INT(10) PRIMARY KEY,
    heureDepart DATETIME,
    heureArrivee DATETIME,
    arretDepart ENUM('PARIS', 'LYON', 'MARSEILLE', 'BORDEAUX', 'TOULOUSE', 'LILLE', 'NANTES', 'STRASBOURG', 'NICE', 'RENNES', 'MONTPELLIER'),
    arretArrivee ENUM('PARIS', 'LYON', 'MARSEILLE', 'BORDEAUX', 'TOULOUSE', 'LILLE', 'NANTES', 'STRASBOURG', 'NICE', 'RENNES', 'MONTPELLIER'),
    trainId INT(10),
    conducteurId INT(10),
    FOREIGN KEY (trainId) REFERENCES Train(immatriculation),
    FOREIGN KEY (conducteurId) REFERENCES Utilisateur(id)
);

CREATE TABLE Incident (
      id INT(10) PRIMARY KEY,
      description VARCHAR(255),
      typeIncident ENUM('PANNE_TECHNIQUE', 'RETARD_TRAIN', 'VOIE_ENDOMMAGEE', 'INCIDENT_A_BORD'),
      gravite ENUM('MINEUR', 'MODERE', 'MAJEUR', 'CRITIQUE'),
      trainImmat VARCHAR(50),
      FOREIGN KEY (trainImmat) REFERENCES Train(immatriculation)
);

CREATE TABLE Maintenance (
     dateMaintenance DATETIME,
     description VARCHAR(255),
     etat ENUM('PANNE', 'MAINTENANCE', 'OPERATIONNEL'),
     incidentId INT(10),
     technicienId INT(10),
     FOREIGN KEY (incidentId) REFERENCES Incident(id),
     FOREIGN KEY (technicienId) REFERENCES Utilisateur(id)
);

--JEU DE DONNEES

-- Utilisateur
INSERT INTO Utilisateur (id, nom, prenom, email, mdp, role) VALUES
    (1, 'Dupont', 'Jean', 'jean.dupont@example.com', 'mdp1', 'ADMIN'),
    (2, 'Martin', 'Lucie', 'lucie.martin@example.com', 'mdp2', 'TECHNICIEN'),
    (3, 'Durand', 'Pierre', 'pierre.durand@example.com', 'mdp3', 'CONDUCTEUR'),
    (4, 'Bernard', 'Marie', 'marie.bernard@example.com', 'mdp4', 'OPERATEUR');


-- Technicien
INSERT INTO Technicien (id, specialite) VALUES
    (2, 'ELECTRONIQUE');


-- Train
INSERT INTO Train (immatriculation, marque, modele) VALUES
    ('T12345', 'Alstom', 'TGV'),
    ('T67890', 'Siemens', 'ICE');


-- Trajet
INSERT INTO Trajet (id, heureDepart, heureArrivee, arretDepart, arretArrivee, trainId, conducteurId) VALUES
     (1, '2025-01-21 08:00:00', '2025-01-21 10:00:00', 'PARIS', 'LYON', 'T12345', 3),
     (2, '2025-01-21 14:00:00', '2025-01-21 16:00:00', 'LYON', 'MARSEILLE', 'T67890', 3);


-- Incident
INSERT INTO Incident (id, description, typeIncident, gravite, trainImmat) VALUES
      (1, 'Panne de moteur', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
      (2, 'Retard dû à un obstacle sur la voie', 'RETARD_TRAIN', 'MODERE', 'T67890');

-- Maintenance
INSERT INTO Maintenance (dateMaintenance, description, etat, incidentId, technicienId) VALUES
       ('2025-01-22 10:00:00', 'Réparation du moteur', 'MAINTENANCE', 1, 2),
       ('2025-01-23 09:00:00', 'Vérification de la voie', 'OPERATIONNEL', 2, 2);

