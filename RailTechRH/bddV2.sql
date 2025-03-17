
-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : lun. 17 mars 2025 à 07:57
-- Version du serveur : 11.5.2-MariaDB
-- Version de PHP : 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `railtechrh`
--

-- --------------------------------------------------------

--
-- Structure de la table `incident`
--

DROP TABLE IF EXISTS `incident`;
CREATE TABLE IF NOT EXISTS `incident` (
                                          `id` int(11) NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    `typeIncident` enum('PANNE_TECHNIQUE','RETARD_TRAIN','VOIE_ENDOMMAGEE','INCIDENT_A_BORD') DEFAULT NULL,
    `gravite` enum('MINEUR','MODERE','MAJEUR','CRITIQUE') DEFAULT NULL,
    `trainImmat` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `trainImmat` (`trainImmat`)
    ) ENGINE=MyISAM AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `incident`
--

INSERT INTO `incident` (`id`, `description`, `typeIncident`, `gravite`, `trainImmat`) VALUES
                                                                                          (1, 'Panne de moteur', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (2, 'Retard dû à un obstacle sur la voie', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (3, '2h ', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (4, 'Panne de moteur', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (5, 'Retard dû à un obstacle sur la voie', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (6, 'Voie endommagée par des intempéries', 'VOIE_ENDOMMAGEE', 'MAJEUR', 'T12345'),
                                                                                          (7, 'Incident à bord avec un passager', 'INCIDENT_A_BORD', 'MODERE', 'T67890'),
                                                                                          (8, 'Problème de frein', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (9, 'Retard dû à la météo', 'RETARD_TRAIN', 'MAJEUR', 'T67890'),
                                                                                          (10, 'Voie endommagée par un glissement de terrain', 'VOIE_ENDOMMAGEE', 'MAJEUR', 'T12345'),
                                                                                          (11, 'Incident à bord avec un bagage', 'INCIDENT_A_BORD', 'MODERE', 'T67890'),
                                                                                          (12, 'Défaillance électrique', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (13, 'Retard dû à un passager', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (14, 'Voie endommagée par des travaux', 'VOIE_ENDOMMAGEE', 'MODERE', 'T12345'),
                                                                                          (15, 'Incident à bord avec un animal', 'INCIDENT_A_BORD', 'MAJEUR', 'T67890'),
                                                                                          (16, 'Problème de signalisation', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (17, 'Retard dû à un incident sur la voie', 'RETARD_TRAIN', 'MAJEUR', 'T67890'),
                                                                                          (18, 'Voie endommagée par un accident', 'VOIE_ENDOMMAGEE', 'MAJEUR', 'T12345'),
                                                                                          (19, 'Incident à bord avec un objet dangereux', 'INCIDENT_A_BORD', 'MODERE', 'T67890'),
                                                                                          (20, 'Panne de climatisation', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (21, 'Retard dû à un problème technique', 'RETARD_TRAIN', 'MAJEUR', 'T67890'),
                                                                                          (22, 'Voie endommagée par des conditions météorologiques', 'VOIE_ENDOMMAGEE', 'MODERE', 'T12345'),
                                                                                          (23, 'Incident à bord avec un passager malade', 'INCIDENT_A_BORD', 'MAJEUR', 'T67890'),
                                                                                          (24, 'Problème de frein', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (25, 'Retard de 30 minutes', 'RETARD_TRAIN', 'MINEUR', 'T67890'),
                                                                                          (26, 'Voie endommagée par un arbre', 'VOIE_ENDOMMAGEE', 'MAJEUR', 'T12345'),
                                                                                          (27, 'Incident à bord avec passager', 'INCIDENT_A_BORD', 'CRITIQUE', 'T67890'),
                                                                                          (28, 'Problème électrique', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (29, 'Retard de 15 minutes', 'RETARD_TRAIN', 'MINEUR', 'T67890'),
                                                                                          (30, 'Voie inondée', 'VOIE_ENDOMMAGEE', 'MAJEUR', 'T12345'),
                                                                                          (31, 'Incident mineur à bord', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (32, 'Problème de signalisation', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (33, 'Retard de 45 minutes', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (34, 'Problème de freinage', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (35, 'Retard dû à des travaux sur la voie', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (36, 'Voie ferrée endommagée par inondation', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (37, 'Problème de climatisation', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (38, 'Signalisation défectueuse', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (39, 'Retard dû à un incident précédent', 'RETARD_TRAIN', 'MAJEUR', 'T67890'),
                                                                                          (40, 'Voie obstruée par un arbre', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (41, 'Bagages mal chargés', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (42, 'Problème électrique', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (43, 'Retard dû à des conditions météo', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (44, 'Voie endommagée par vandalisme', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (45, 'Problème de porte', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (46, 'Système de communication défectueux', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (47, 'Retard dû à un problème de personnel', 'RETARD_TRAIN', 'MAJEUR', 'T67890'),
                                                                                          (48, 'Voie endommagée par un glissement de terrain', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (49, 'Problème de siège', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (50, 'Problème de moteur', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (51, 'Retard dû à un problème de signalisation', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (52, 'Voie endommagée par un accident', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (53, 'Problème de wifi', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (54, 'Problème de freinage', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (55, 'Retard dû à des travaux sur la voie', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (56, 'Voie ferrée endommagée par inondation', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (57, 'Problème de climatisation', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (58, 'Signalisation défectueuse', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (59, 'Retard dû à un incident précédent', 'RETARD_TRAIN', 'MAJEUR', 'T67890'),
                                                                                          (60, 'Voie obstruée par un arbre', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (61, 'Bagages mal chargés', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (62, 'Problème électrique', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (63, 'Retard dû à des conditions météo', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (64, 'Voie endommagée par vandalisme', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (65, 'Problème de porte', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (66, 'Système de communication défectueux', 'PANNE_TECHNIQUE', 'MODERE', 'T12345'),
                                                                                          (67, 'Retard dû à un problème de personnel', 'RETARD_TRAIN', 'MAJEUR', 'T67890'),
                                                                                          (68, 'Voie endommagée par un glissement de terrain', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (69, 'Problème de siège', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (70, 'Problème de moteur', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
                                                                                          (71, 'Retard dû à un problème de signalisation', 'RETARD_TRAIN', 'MODERE', 'T67890'),
                                                                                          (72, 'Voie endommagée par un accident', 'VOIE_ENDOMMAGEE', 'CRITIQUE', 'T12345'),
                                                                                          (73, 'Problème de wifi', 'INCIDENT_A_BORD', 'MINEUR', 'T67890'),
                                                                                          (74, 'individus sur les voies	\n', 'RETARD_TRAIN', 'MODERE', 'T12345'),
                                                                                          (75, 'testsetetstst', 'INCIDENT_A_BORD', 'CRITIQUE', 'T67890');

-- --------------------------------------------------------

--
-- Structure de la table `maintenance`
--

DROP TABLE IF EXISTS `maintenance`;
CREATE TABLE IF NOT EXISTS `maintenance` (
                                             `dateMaintenance` datetime DEFAULT NULL,
                                             `description` varchar(255) DEFAULT NULL,
    `probleme` enum('RETARD_TRAIN','PANNE_TECHNIQUE','VOIE_ENDOMMAGEE','INCIDENT_A_BORD') NOT NULL,
    `etat` enum('PANNE','MAINTENANCE','OPERATIONNEL') DEFAULT NULL,
    `incidentId` int(11) NOT NULL,
    `technicienId` int(11) DEFAULT NULL,
    PRIMARY KEY (`incidentId`),
    KEY `technicienId` (`technicienId`)
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `maintenance`
--

INSERT INTO `maintenance` (`dateMaintenance`, `description`, `probleme`, `etat`, `incidentId`, `technicienId`) VALUES
                                                                                                                   ('2025-01-22 10:00:00', 'Réparation du moteur', 'RETARD_TRAIN', 'MAINTENANCE', 1, 2),
                                                                                                                   ('2025-01-23 09:00:00', 'Vérification de la voie', 'RETARD_TRAIN', 'OPERATIONNEL', 2, 2),
                                                                                                                   ('2023-10-01 10:00:00', 'Maintenance régulière', 'RETARD_TRAIN', 'OPERATIONNEL', 3, 2),
                                                                                                                   ('2023-10-01 10:00:00', 'Maintenance régulière', 'RETARD_TRAIN', 'OPERATIONNEL', 4, 2),
                                                                                                                   ('2025-03-03 09:33:14', 'Maintenance régulière', 'RETARD_TRAIN', 'OPERATIONNEL', 5, 2),
                                                                                                                   ('2025-03-03 14:46:17', 'Maintenance régulière', 'VOIE_ENDOMMAGEE', 'OPERATIONNEL', 8, 2),
                                                                                                                   ('2025-03-04 08:53:01', 'test', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 6, 14),
                                                                                                                   ('2025-03-04 08:54:02', 'ok', 'PANNE_TECHNIQUE', 'OPERATIONNEL', 32, 2),
                                                                                                                   ('2025-03-04 09:36:09', 'TRAVEAUX en cours', 'VOIE_ENDOMMAGEE', 'PANNE', 10, 2),
                                                                                                                   ('2025-03-04 10:50:58', 'ok', 'VOIE_ENDOMMAGEE', 'OPERATIONNEL', 14, 2),
                                                                                                                   ('2025-03-04 11:01:25', 'OK', 'PANNE_TECHNIQUE', 'OPERATIONNEL', 16, 14),
                                                                                                                   ('2025-03-04 11:09:03', 'travaux', 'PANNE_TECHNIQUE', 'MAINTENANCE', 12, 2),
                                                                                                                   ('2025-03-04 11:13:09', 'test', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 18, 14),
                                                                                                                   ('2025-03-04 11:17:54', 'test	\n', 'PANNE_TECHNIQUE', 'MAINTENANCE', 20, 2),
                                                                                                                   ('2025-03-04 11:19:38', 'ok', 'VOIE_ENDOMMAGEE', 'PANNE', 22, 14),
                                                                                                                   ('2025-03-04 11:28:33', 'OK\n', 'PANNE_TECHNIQUE', 'MAINTENANCE', 24, 2),
                                                                                                                   ('2025-03-04 11:31:19', 'test', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 26, 14),
                                                                                                                   ('2025-03-04 11:34:32', 'test', 'PANNE_TECHNIQUE', 'OPERATIONNEL', 28, 14),
                                                                                                                   ('2025-03-04 11:37:19', 'TEST', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 30, 2),
                                                                                                                   ('2025-03-04 11:48:27', 'OK', 'PANNE_TECHNIQUE', 'OPERATIONNEL', 34, 14),
                                                                                                                   ('2025-03-04 11:58:27', 'test', 'VOIE_ENDOMMAGEE', 'OPERATIONNEL', 36, 14),
                                                                                                                   ('2025-03-04 12:47:08', 'test', 'PANNE_TECHNIQUE', 'MAINTENANCE', 38, 14),
                                                                                                                   ('2025-03-04 12:49:22', 'tes', 'VOIE_ENDOMMAGEE', 'OPERATIONNEL', 40, 14),
                                                                                                                   ('2025-03-04 12:54:10', 'e', 'PANNE_TECHNIQUE', 'OPERATIONNEL', 42, 14),
                                                                                                                   ('2025-03-04 12:57:25', 'r', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 44, 14),
                                                                                                                   ('2025-03-04 13:01:45', 'R', 'PANNE_TECHNIQUE', 'MAINTENANCE', 46, 14),
                                                                                                                   ('2025-03-04 13:02:27', 'D', 'VOIE_ENDOMMAGEE', 'OPERATIONNEL', 48, 14),
                                                                                                                   ('2025-03-04 13:03:43', 'z', 'PANNE_TECHNIQUE', 'MAINTENANCE', 50, 2),
                                                                                                                   ('2025-03-04 13:06:23', 't', 'PANNE_TECHNIQUE', 'MAINTENANCE', 58, 14),
                                                                                                                   ('2025-03-04 13:06:55', 'r', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 52, 14),
                                                                                                                   ('2025-03-04 13:16:23', 'r', 'PANNE_TECHNIQUE', 'MAINTENANCE', 54, 14),
                                                                                                                   ('2025-03-04 13:17:08', 'ok', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 56, 2),
                                                                                                                   ('2025-03-04 14:12:55', 'e', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 60, 14),
                                                                                                                   ('2025-03-04 14:18:14', 'R', 'VOIE_ENDOMMAGEE', 'OPERATIONNEL', 68, 14),
                                                                                                                   ('2025-03-04 14:21:00', 't', 'PANNE_TECHNIQUE', 'MAINTENANCE', 70, 14),
                                                                                                                   ('2025-03-07 18:16:56', 'en cours', 'PANNE_TECHNIQUE', 'MAINTENANCE', 62, 14),
                                                                                                                   ('2025-03-07 23:20:28', 'efeefefe', 'VOIE_ENDOMMAGEE', 'MAINTENANCE', 64, 14),
                                                                                                                   ('2025-03-17 08:43:02', 'test', 'PANNE_TECHNIQUE', 'MAINTENANCE', 66, 14);

-- --------------------------------------------------------

--
-- Structure de la table `technicien`
--

DROP TABLE IF EXISTS `technicien`;
CREATE TABLE IF NOT EXISTS `technicien` (
                                            `id` int(11) NOT NULL,
    `specialite` enum('MECANIQUE','ELECTRONIQUE','ELECTRIQUE','INFORMATIQUE','INFRASTRUCTURE') DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `technicien`
--

INSERT INTO `technicien` (`id`, `specialite`) VALUES
                                                  (2, 'ELECTRONIQUE'),
                                                  (5, 'ELECTRIQUE'),
                                                  (24, 'ELECTRONIQUE');

-- --------------------------------------------------------

--
-- Structure de la table `train`
--

DROP TABLE IF EXISTS `train`;
CREATE TABLE IF NOT EXISTS `train` (
                                       `immatriculation` varchar(50) NOT NULL,
    `marque` varchar(50) DEFAULT NULL,
    `modele` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`immatriculation`)
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `train`
--

INSERT INTO `train` (`immatriculation`, `marque`, `modele`) VALUES
                                                                ('T12345', 'Alstom', 'TGV'),
                                                                ('T67890', 'Siemens', 'ICE');

-- --------------------------------------------------------

--
-- Structure de la table `trajet`
--

DROP TABLE IF EXISTS `trajet`;
CREATE TABLE IF NOT EXISTS `trajet` (
                                        `id` int(11) NOT NULL AUTO_INCREMENT,
    `heureDepart` datetime DEFAULT NULL,
    `heureArrivee` datetime DEFAULT NULL,
    `arretDepart` enum('PARIS','LYON','MARSEILLE','BORDEAUX','TOULOUSE','LILLE','NANTES','STRASBOURG','NICE','RENNES','MONTPELLIER') DEFAULT NULL,
    `arretArrivee` enum('PARIS','LYON','MARSEILLE','BORDEAUX','TOULOUSE','LILLE','NANTES','STRASBOURG','NICE','RENNES','MONTPELLIER') DEFAULT NULL,
    `trainImmat` varchar(50) DEFAULT NULL,
    `conducteurId` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `trainImmat` (`trainImmat`),
    KEY `conducteurId` (`conducteurId`)
    ) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `trajet`
--

INSERT INTO `trajet` (`id`, `heureDepart`, `heureArrivee`, `arretDepart`, `arretArrivee`, `trainImmat`, `conducteurId`) VALUES
                                                                                                                            (1, '2025-01-21 08:00:00', '2025-01-21 10:00:00', 'PARIS', 'LYON', 'T12345', 3),
                                                                                                                            (2, '2025-01-21 14:00:00', '2025-01-21 16:00:00', 'LYON', 'MARSEILLE', 'T67890', 3),
                                                                                                                            (3, '2025-02-15 08:00:00', '2025-02-15 10:00:00', 'PARIS', 'LYON', 'T12345', 1),
                                                                                                                            (4, '2025-02-16 09:00:00', '2025-02-16 11:00:00', 'LYON', 'PARIS', 'T67890', 1),
                                                                                                                            (5, '2025-02-17 10:00:00', '2025-02-17 12:00:00', 'PARIS', 'LYON', 'T12345', 1),
                                                                                                                            (6, '2025-02-15 08:00:00', '2025-02-15 10:00:00', 'PARIS', 'LYON', 'T12345', 3),
                                                                                                                            (7, '2025-02-16 09:00:00', '2025-02-16 11:00:00', 'LYON', 'PARIS', 'T12345', 3),
                                                                                                                            (8, '2025-02-17 10:00:00', '2025-02-17 12:00:00', 'PARIS', 'LYON', 'T12345', 3),
                                                                                                                            (9, '2025-02-19 09:00:00', '2025-02-19 11:00:00', 'PARIS', 'LILLE', 'T12345', 3),
                                                                                                                            (10, '2025-02-20 10:00:00', '2025-02-20 12:00:00', 'LYON', 'STRASBOURG', 'T67890', 3),
                                                                                                                            (11, '2025-02-21 11:00:00', '2025-02-21 13:00:00', 'MARSEILLE', 'NICE', 'T12345', 3),
                                                                                                                            (12, '2025-02-22 08:00:00', '2025-02-22 10:00:00', 'BORDEAUX', 'TOULOUSE', 'T67890', 3),
                                                                                                                            (14, '2025-03-05 01:15:00', '2025-03-06 01:45:00', 'LYON', 'BORDEAUX', 'T12345', 0);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
                                             `id` int(11) NOT NULL AUTO_INCREMENT,
    `nom` varchar(50) DEFAULT NULL,
    `prenom` varchar(50) DEFAULT NULL,
    `email` varchar(100) DEFAULT NULL,
    `mdp` varchar(255) DEFAULT NULL,
    `role` enum('ADMIN','TECHNICIEN','CONDUCTEUR','OPERATEUR') DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `nom`, `prenom`, `email`, `mdp`, `role`) VALUES
                                                                              (1, 'Dupont', 'Jean', 'jean.dupont@example.com', 'mdp1', 'ADMIN'),
                                                                              (2, 'Martin', 'Lucie', 'lucie.martin@example.com', 'mdp2', 'TECHNICIEN'),
                                                                              (3, 'Durand', 'Pierre', 'pierre.durand@example.com', 'mdp3', 'CONDUCTEUR'),
                                                                              (4, 'Bernard', 'Marie', 'marie.bernard@example.com', 'mdp4', 'OPERATEUR'),
                                                                              (6, 'Test', 'User', 'test.user@example.com', 'password123', 'CONDUCTEUR'),
                                                                              (7, 'Test', 'User', 'test.user@example.com', 'password123', 'CONDUCTEUR'),
                                                                              (8, 'Test', 'User', 'test.user@example.com', 'password123', 'CONDUCTEUR'),
                                                                              (9, 'Test', 'User', 'test.user@example.com', 'password123', 'CONDUCTEUR'),
                                                                              (10, 'Test', 'User', 'test.user@example.com', 'password123', 'CONDUCTEUR'),
                                                                              (11, 'Test', 'User', 'test.user@example.com', 'password123', 'OPERATEUR'),
                                                                              (12, 'Test', 'User', 'test.user@example.com', 'password123', 'OPERATEUR'),
                                                                              (13, 'test', 'test', 'test', 'test', 'ADMIN'),
                                                                              (14, 'malika', 'amrani', 'malika@gmail.com', '12', 'TECHNICIEN'),
                                                                              (15, 'a', 'a', 'a', 'a', 'TECHNICIEN'),
                                                                              (16, 'Nouveau', 'Utilisateur', 'test.ajouter@test.com', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'TECHNICIEN'),
                                                                              (17, 'Test', 'User', 'test.user@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TECHNICIEN'),
                                                                              (18, 'Martin', 'DUPRES', 'martin.dupres@example.com', 'bb246837fe49b2b587113d7aa92afca4d9975bd8f5df46fe154ff2d31cf57d5d', 'CONDUCTEUR'),
                                                                              (19, 'amrani', 'kamal', 'kamal@gmail.com', 'a671bfa8e334ab780b1b7ff4db37e7faa25a4a7d0d17cbaa6f849474020366a2', 'TECHNICIEN'),
                                                                              (20, 'emma', 'dupuis', 'emma@example.com', 'b15807120385e50345de8c921aa8aef3eb846e76565faa29d44e6cbe40b1234e', 'CONDUCTEUR'),
                                                                              (21, 'louise', 'mide', 'test@ouise.com', '8b3ab91d41a20330fbc8949c0e517c958fcb8675a9b087aeee35f9d387bd6d06', 'OPERATEUR'),
                                                                              (22, 'Martin', 'DUPRES', 'martin.dupres@example.com', 'bb246837fe49b2b587113d7aa92afca4d9975bd8f5df46fe154ff2d31cf57d5d', 'CONDUCTEUR'),
                                                                              (23, 'sarah', 'srh', 'sar@exm.com', 'c7ad55481b24cc6b25dfb01056a07d32ceefa39fa0a4637f6cb55527e1afb5b8', 'TECHNICIEN'),
                                                                              (24, 'ts', 'ts', 'ts', '44ad63f60af0f6db6fdde6d5186ef78176367df261fa06be3079b6c80c8adba4', 'TECHNICIEN');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
